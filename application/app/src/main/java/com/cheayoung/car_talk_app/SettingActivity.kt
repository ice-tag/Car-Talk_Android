package com.cheayoung.car_talk_app

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kyleduo.switchbutton.SwitchButton
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        var push_state :Int = 4
        var sound_state :Int = 1
        var vibrate_state :Int = 1
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        if(intent.hasExtra("push")) push_state = intent.getIntExtra("push",1)
        if(intent.hasExtra("sound")) sound_state = intent.getIntExtra("sound",1)
        if(intent.hasExtra("vibrate")) vibrate_state = intent.getIntExtra("vibrate",1)
        if(push_state == 1){
            button_push_alam.setChecked(true)
        }
        if(sound_state == 1){
            button_sound.setChecked(true)
        }
        if(vibrate_state == 1){
            button_vibrate.setChecked(true)
        }

        button_push_alam.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked -> // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
            if (isChecked) {
                push_state = 1
            } else {
                push_state = 0
            }
        })

        button_sound.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked -> // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
            if (isChecked) {
                sound_state = 1
            } else {
                sound_state = 0
            }
        })

        button_vibrate.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked -> // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
            if (isChecked) {
                vibrate_state = 1
            } else {
                vibrate_state = 0
            }
        })

        go_scan_page.setOnClickListener {
            val intent = Intent(this, ScanActivity::class.java)// 다음 화면으로 이동
            intent.putExtra("vibrate",vibrate_state)
            intent.putExtra("push",push_state)
            intent.putExtra("sound",sound_state)
            startActivity(intent)
            finish()
        }
    }
}