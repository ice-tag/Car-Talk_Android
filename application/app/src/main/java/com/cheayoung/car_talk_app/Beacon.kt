package com.cheayoung.car_talk_app

import android.bluetooth.le.ScanRecord

class Beacon(
    val car_number: String,
    val case: String,
    val rail: String,
    val rssi: Int,
    val now: String,
    val uuid: ScanRecord
)
