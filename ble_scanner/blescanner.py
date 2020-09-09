import os
import sys
import struct
import bluetooth._bluetooth as bluez

LE_META_EVENT = 0x3e
LE_PUBLIC_ADDRESS=0x00
LE_RANDOM_ADDRESS=0x01
LE_SET_SCAN_PARAMETERS_CP_SIZE=7
OGF_LE_CTL=0x08
OCF_LE_SET_SCAN_PARAMETERS=0x000B
OCF_LE_SET_SCAN_ENABLE=0x000C
OCF_LE_CREATE_CONN=0x000D

LE_ROLE_MASTER = 0x00
LE_ROLE_SLAVE = 0x01

# these are actually subevents of LE_META_EVENT
EVT_LE_CONN_COMPLETE=0x01
EVT_LE_ADVERTISING_REPORT=0x02
EVT_LE_CONN_UPDATE_COMPLETE=0x03
EVT_LE_READ_REMOTE_USED_FEATURES_COMPLETE=0x04

# Advertisment event types
ADV_IND=0x00
ADV_DIRECT_IND=0x01
ADV_SCAN_IND=0x02
ADV_NONCONN_IND=0x03
ADV_SCAN_RSP=0x04


class Beacon:
    def __init__(self):
        self.uuid, self.major, self.minor = None, None, None
        self.mac, self.unknown, self.rssi = None, None, None
    
    def __eq__(self, other):
        return self.mac == other.mac and self.uuid == other.uuid and self.major == other.major and self.minor == other.minor
    
    def __hash__(self): return hash('{}{}{}{}'.format(self.mac, self.uuid, self.major, self.minor))
    
    def __repr__(self):
        return '<Beacon; {}; {}; {}/{}; RSSI: {}>'.format(self.mac, self.uuid, self.major, self.minor, self.rssi)

def to_char(c, signed=False):
    if type(c) is int:
        return -1 * (256-c) if signed and c > 127 else c
    else:
        return struct.unpack('b' if signed else 'B', c)[0]

def returnnumberpacket(pkt):
    myInteger = 0
    multiple = 256
    for c in pkt:
        myInteger += to_char(c) * multiple
        multiple = 1
    return myInteger 

def returnstringpacket(pkt):
    myString = "";
    for c in pkt:
        myString +=  "%02x" % to_char(c)
    return myString 

def printpacket(pkt):
    for c in pkt:
        sys.stdout.write("%02x " % to_char(c))

def get_packed_bdaddr(bdaddr_string):
    packable_addr = []
    addr = bdaddr_string.split(':')
    addr.reverse()
    for b in addr: 
        packable_addr.append(int(b, 16))
    return struct.pack("<BBBBBB", *packable_addr)

def packed_bdaddr_to_string(bdaddr_packed):
    return ':'.join('%02x'%i for i in struct.unpack("<BBBBBB", bdaddr_packed[::-1]))

def hci_enable_le_scan(sock):
    hci_toggle_le_scan(sock, 0x01)

def hci_disable_le_scan(sock):
    hci_toggle_le_scan(sock, 0x00)

def hci_toggle_le_scan(sock, enable):
#    hci_le_set_scan_enable(dd, 0x01, filter_dup, 1000);
#    memset(&scan_cp, 0, sizeof(scan_cp));
#    uint8_t         enable;
#    uint8_t         filter_dup;
#    scan_cp.enable = enable;
#    scan_cp.filter_dup = filter_dup;
#
#    memset(&rq, 0, sizeof(rq));
#    rq.ogf = OGF_LE_CTL;
#    rq.ocf = OCF_LE_SET_SCAN_ENABLE;
#    rq.cparam = &scan_cp;
#    rq.clen = LE_SET_SCAN_ENABLE_CP_SIZE;
#    rq.rparam = &status;
#    rq.rlen = 1;

#    if (hci_send_req(dd, &rq, to) < 0)
#        return -1;
    cmd_pkt = struct.pack("<BB", enable, 0x00)
    bluez.hci_send_cmd(sock, OGF_LE_CTL, OCF_LE_SET_SCAN_ENABLE, cmd_pkt)

def hci_le_set_scan_parameters(sock):
    old_filter = sock.getsockopt( bluez.SOL_HCI, bluez.HCI_FILTER, 14)

    SCAN_RANDOM = 0x01
    OWN_TYPE = SCAN_RANDOM
    SCAN_TYPE = 0x01

def parse_events(sock, loop_count=100):
    old_filter = sock.getsockopt( bluez.SOL_HCI, bluez.HCI_FILTER, 14)

    # perform a device inquiry on bluetooth device #0
    # The inquiry should last 8 * 1.28 = 10.24 seconds
    # before the inquiry is performed, bluez should flush its cache of
    # previously discovered devices
    flt = bluez.hci_filter_new()
    bluez.hci_filter_all_events(flt)
    bluez.hci_filter_set_ptype(flt, bluez.HCI_EVENT_PKT)
    sock.setsockopt( bluez.SOL_HCI, bluez.HCI_FILTER, flt )
    results = []
    for i in range(0, loop_count):
        pkt = sock.recv(255)
        ptype, event, plen = struct.unpack("BBB", pkt[:3])
        
        if event == bluez.EVT_INQUIRY_RESULT_WITH_RSSI: i = 0
        elif event == bluez.EVT_NUM_COMP_PKTS: i = 0 
        elif event == bluez.EVT_DISCONN_COMPLETE: i = 0 
        if event == LE_META_EVENT:
            subevent = to_char(pkt[3])
            pkt = pkt[4:]
            if subevent == EVT_LE_CONN_COMPLETE:
                le_handle_connection_complete(pkt)
            elif subevent == EVT_LE_ADVERTISING_REPORT:
                num_reports = to_char(pkt[0])
                report_pkt_offset = 0
                for i in range(0, num_reports):
                    b = Beacon()
                    uuid = returnstringpacket(pkt[report_pkt_offset -22: report_pkt_offset - 6]).upper()
                    b.uuid = '{}-{}-{}-{}-{}'.format(uuid[:8], uuid[8:12], uuid[12:16], uuid[16:20], uuid[20:])
                    b.major = "%i" % returnnumberpacket(pkt[report_pkt_offset -6: report_pkt_offset - 4])
                    b.minor = "%i" % returnnumberpacket(pkt[report_pkt_offset -4: report_pkt_offset - 2])
                    b.mac = packed_bdaddr_to_string(pkt[report_pkt_offset + 3:report_pkt_offset + 9])
                    b.unknown = "%i" % to_char(pkt[report_pkt_offset -2], signed=True)
                    b.rssi = "%i" % to_char(pkt[report_pkt_offset -1], signed=True)
                    if b in results:
                        ob = results[results.index(b)]
                        ob.unknown = b.unknown
                        ob.rssi = b.rssi
                    else:
                        results.append(b)
    sock.setsockopt( bluez.SOL_HCI, bluez.HCI_FILTER, old_filter )
    return results 