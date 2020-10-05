package com.cheayoung.car_talk_app

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.*
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.ParcelUuid
import android.util.Log
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_scan.*
import java.text.SimpleDateFormat
import java.util.*


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class ScanActivity : AppCompatActivity() {
    var mBluetoothAdapter: BluetoothAdapter? = null
    lateinit var mBluetoothLeScanner: BluetoothLeScanner
    var mBluetoothLeAdvertiser: BluetoothLeAdvertiser? = null
    var beacon: Vector<Beacon>? = null
    var beaconAdapter: BeaconAdapter? = null
    var beaconListView: ListView? = null
    var mScanSettings: ScanSettings.Builder? = null
    var scanFilters: MutableList<ScanFilter>? = null
    var simpleDateFormat = SimpleDateFormat("MM-dd HH:mm:ss", Locale.KOREAN)
    var push_state = 1
    var sound_state = 1
    var vibrate_state = 1
    var beacon_object : MutableList<Any>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        if(intent.hasExtra("push")) push_state = intent.getIntExtra("push",1)
        if(intent.hasExtra("sound")) sound_state = intent.getIntExtra("sound",1)
        if(intent.hasExtra("vibrate")) vibrate_state = intent.getIntExtra("vibrate",1)

        ActivityCompat.requestPermissions(
            this, arrayOf<String>(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), PERMISSIONS
        )
        beaconListView = findViewById(R.id.beaconListView) as ListView?
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        mBluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner()
        mBluetoothLeAdvertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser()
        beacon = Vector<Beacon>()
        mScanSettings = ScanSettings.Builder()
        mScanSettings!!.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        // 얘는 스캔 주기를 2초로 줄여주는 Setting입니다.
        // 공식문서에는 위 설정을 사용할 때는 다른 설정을 하지 말고
        // 위 설정만 단독으로 사용하라고 되어 있네요 ^^
        // 위 설정이 없으면 테스트해 본 결과 약 10초 주기로 스캔을 합니다.
        val scanSettings = mScanSettings!!.build()
        scanFilters = Vector()
        val scanFilter: ScanFilter.Builder = ScanFilter.Builder()
        //scanFilter.setDeviceAddress("특정 기기의 MAC 주소") //ex) 00:00:00:00:00:00
        scanFilter.setDeviceAddress("00:00:00:00:00:00") //ex) 00:00:00:00:00:00
        val scan: ScanFilter = scanFilter.build()
        (scanFilters as Vector<ScanFilter>).add(scan)
        //mBluetoothLeScanner.startScan(scanFilters, scanSettings, mScanCallback)
        mBluetoothLeScanner.startScan(mScanCallback)
        /* filter와 settings 기능을 사용하지 않을 때는
        mBluetoothLeScanner.startScan(mScanCallback)
        var 사용하시면: 처럼
        돼요.*/

        scan_stop.setOnClickListener {
            mBluetoothLeScanner?.stopScan(mScanCallback)
            val intent = Intent(this, MainActivity::class.java)// 다음 화면으로 이동
            startActivity(intent)
            finish()
        }

        setting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)// 다음 화면으로 이동
            intent.putExtra("vibrate",vibrate_state)
            intent.putExtra("push",push_state)
            intent.putExtra("sound",sound_state)
            startActivity(intent)
            finish()
        }
    }

    private fun buildAdvertiseData(): AdvertiseData {
        val dataBuilder: AdvertiseData.Builder = AdvertiseData.Builder()
        //Define a service UUID according to your needs
        dataBuilder.addServiceUuid(ParcelUuid(UUID(0x123abcL, -1L)))
        dataBuilder.setIncludeDeviceName(true)
        return dataBuilder.build()
    }

    var mScanCallback: ScanCallback = object : ScanCallback() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            try {
                val scanRecord: ScanRecord? = result.getScanRecord()
                Log.d("getTxPowerLevel()", scanRecord?.getTxPowerLevel().toString() + "")
                Log.d(
                    "onScanResult()",
                    result.getDevice().getAddress()
                        .toString() + "\n" + result.getRssi() + "\n" + result.getDevice().getName()
                            + "\n" + result.getDevice().getBondState() + "\n" + result.getDevice()
                        .getType()
                )
                scanRecord!!.getServiceUuids()
                val scanResult: ScanResult = result
                // 4c 00 이후부터 뒤에 00 전까지 mManufacturerSpecificData
                Thread {
                    runOnUiThread {
                        val change = scanRecord!!.toString()
                        var ran1 = IntRange(change.indexOf("mManufacturerSpecificData")+27, change.indexOf("mServiceData")-4)
                        var uuid_data = change.slice(ran1)
                        var data_list = uuid_data.split(",")
                        var ran2 = IntRange(0, data_list[0].indexOf("=")-1)
                        var start_data = uuid_data.slice(ran2)
                        var end_data : String
                        var case_data : String
                        var car_number10 : String
                        var car_rail: String
                        var car_number10_one : String
                        try {
                            end_data = data_list[data_list.size - 1].slice(IntRange(1,3))
                            case_data = data_list[2].slice(IntRange(1,1))
                            car_number10 = Integer.toHexString(data_list[4].slice(IntRange(1,2)).toInt())+Integer.toHexString(data_list[5].slice(IntRange(1,2)).toInt())
                            car_rail = data_list[7]
                        }
                        catch(e: Exception){
                            end_data = "-1"
                            case_data = "-1"
                            car_number10 = ""
                            car_rail = "-1"
                        }
                        Log.d(
                            "onScan",
                            start_data + "\n" + end_data + "\n /"
                        )
                        if(start_data == "76" && end_data == "-56" && data_list.size == 23) {
                            beacon!!.add(
                                0,
                                Beacon(
                                    car_number10,
                                    case_data,
                                    car_rail,
                                    scanResult.getRssi(),
                                    simpleDateFormat.format(Date()),
                                    scanRecord!!
                                )
                            )

                            // Adapter로 가기 전에 걸러줘야 할 것 같다.
                            // 조건문 들어가야 되는 곳
                            // state를 놓아서 경우마다 beacon 사용자 커스텀으로 볼 수 있도록 추가시켜주기
                            // 알람 들어갈 함수
                            beacon_object?.add(beacon!!)
                            beaconAdapter = BeaconAdapter(beacon, layoutInflater)
                            beaconListView?.setAdapter(beaconAdapter)
                            beaconAdapter?.notifyDataSetChanged()

                        }
                    }
                }.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onBatchScanResults(results: List<ScanResult?>) {
            super.onBatchScanResults(results)
            Log.d("onBatchScanResults", results.size.toString() + "")
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.d("onScanFailed()", errorCode.toString() + "")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //mBluetoothLeScanner?.stopScan(mScanCallback)
    }

    companion object {
        private val PERMISSIONS = 100
    }
}