package com.cheayoung.car_talk_app

import android.bluetooth.le.ScanRecord

class Beacon(val address: String, val rssi: Int, val now: String, val uuid: ScanRecord)
