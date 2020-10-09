
package com.cheayoung.car_talk_app

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        var push_state :Int = 4
        var sound_state :Int = 1
        var vibrate_state :Int = 1
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        if(intent.hasExtra("push")) push_state = intent.getIntExtra("push", 1)
        if(intent.hasExtra("sound")) sound_state = intent.getIntExtra("sound", 1)
        if(intent.hasExtra("vibrate")) vibrate_state = intent.getIntExtra("vibrate", 1)
        if(push_state == 1){
            button_push_alam.setChecked(true)
            text_push_alarm.setTypeface(null, Typeface.BOLD)
        }
        if(sound_state == 1){
            button_sound.setChecked(true)
            text_sound.setTypeface(null, Typeface.BOLD)
        }
        if(vibrate_state == 1){
            button_vibrate.setChecked(true)
            text_vibrate.setTypeface(null, Typeface.BOLD)
        }

        val ab: androidx.appcompat.app.ActionBar? = supportActionBar
        ab!!.title = "Settings"

        button_push_alam.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked -> // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
            if (isChecked) {
                push_state = 1
                text_push_alarm.setTypeface(null, Typeface.BOLD)
            } else {
                push_state = 0
                text_push_alarm.setTypeface(null, Typeface.NORMAL)
            }
        })

        button_sound.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked -> // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
            if (isChecked) {
                text_sound.setTypeface(null, Typeface.BOLD)
                sound_state = 1
            } else {
                sound_state = 0
                text_sound.setTypeface(null, Typeface.NORMAL)
            }
        })

        button_vibrate.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked -> // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
            if (isChecked) {
                vibrate_state = 1
                text_vibrate.setTypeface(null,Typeface.BOLD)
            } else {
                vibrate_state = 0
                text_vibrate.setTypeface(null, Typeface.NORMAL)
            }
        })

        go_scan_page.setOnClickListener {
            val intent = Intent(this, ScanActivity::class.java)// 다음 화면으로 이동
            intent.putExtra("vibrate", vibrate_state)
            intent.putExtra("push", push_state)
            intent.putExtra("sound", sound_state)
            startActivity(intent)
            finish()
        }
    }
}
