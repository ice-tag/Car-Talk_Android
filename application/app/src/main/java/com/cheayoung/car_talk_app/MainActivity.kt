package com.cheayoung.car_talk_app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startLoading();
    }

    private fun startLoading() {
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(applicationContext, ScanButtonActivity::class.java)
            startActivity(intent) //Loagin화면을 띄운다.
            finish() //현재 액티비티 종료
        }, 1000)
    }
}