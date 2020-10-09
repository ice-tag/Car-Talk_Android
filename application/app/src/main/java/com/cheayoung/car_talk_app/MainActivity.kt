package com.cheayoung.car_talk_app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed(Runnable {
            //여기에 딜레이 후 시작할 작업들을 입력
        }, 1000)
        val intent = Intent(this, ScanButtonActivity::class.java)// 다음 화면으로 이동
        startActivity(intent)
        finish()
    }
}