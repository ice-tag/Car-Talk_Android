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
    var push_state = 1
    var sound_state = 1
    var vibrate_state = 1

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

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

        button_push_alam.setOnClickListener{
            val NOTIFICATION_ID = 1001;
            createNotificationChannel(
                this, NotificationManagerCompat.IMPORTANCE_DEFAULT,
                false, getString(R.string.app_name), "App notification channel"
            ) // 1

            val channelId = "$packageName-${getString(R.string.app_name)}" // 2
            val title =            "Car-Talk"
            val content = "Get 3+ Alarm "

            val intent = Intent(baseContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(
                baseContext, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT
            )    // 3

            val style = NotificationCompat.InboxStyle()
            style.addLine("Car no.1234 - Interfere")
            style.addLine("Car no.0922 - Emergency")
            style.addLine("Car no.1337 - Accident")
            style.addLine("Car no.1112 - Interfere")


            val builder = NotificationCompat.Builder(this, channelId)  // 4
            builder.setSmallIcon(R.drawable.robot)    // 5
            builder.setContentTitle(title)    // 6
            builder.setContentText(content)    // 7
            builder.setStyle(style)
            builder.priority = NotificationCompat.PRIORITY_DEFAULT    // 8
            builder.setAutoCancel(true)   // 9
            builder.setContentIntent(pendingIntent)   // 10
            builder.setDefaults(Notification.DEFAULT_VIBRATE) // 진동음이 울리게 하는 것
            // 소리 알람은 DEFAULT_SOUND
            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.notify(NOTIFICATION_ID, builder.build())    // 11
        }
    }

    private fun createNotificationChannel(
        context: Context, importance: Int, showBadge: Boolean,
        name: String, description: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}