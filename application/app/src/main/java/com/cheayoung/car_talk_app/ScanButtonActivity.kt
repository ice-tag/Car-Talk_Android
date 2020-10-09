package com.cheayoung.car_talk_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_scan_button.*

class ScanButtonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        
        //둥근 직사각형 버튼으로
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_button)
        mode_start_button.setOnClickListener {
            val intent = Intent(this, ScanActivity::class.java)// 다음 화면으로 이동
            startActivity(intent)
            finish()
        }
    }
}