package com.cheayoung.car_talk_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import java.util.*

class BeaconAdapter(beacons: Vector<Beacon>?, layoutInflater: LayoutInflater) :
    BaseAdapter() {
    private val beacons: Vector<Beacon>
    private val layoutInflater: LayoutInflater

    init {
        this.beacons = beacons!!
        this.layoutInflater = layoutInflater
    }

    override fun getCount(): Int {
        return beacons.size
    }

    override fun getItem(position: Int): Any {
        return beacons.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView: View? = convertView
        val beaconHolder: BeaconHolder
        if (convertView == null) {
            beaconHolder = BeaconHolder()
            convertView = layoutInflater.inflate(R.layout.item_beacon, parent, false)
            beaconHolder.car_number = convertView.findViewById(R.id.car_number)
            beaconHolder.rail = convertView.findViewById(R.id.rail)
            beaconHolder.rssi = convertView.findViewById(R.id.rssi)
            beaconHolder.time = convertView.findViewById(R.id.time)
            beaconHolder.message = convertView.findViewById(R.id.message)
            beaconHolder.image = convertView.findViewById(R.id.iv_profile)
            convertView.setTag(beaconHolder)
        } else {
            beaconHolder = convertView.getTag() as BeaconHolder
        }
        beaconHolder.car_number?.setText( beacons.get(position).car_number)
        beaconHolder.time?.setText(beacons.get(position).now)
        beaconHolder.rssi?.setText("RSSI : " + beacons.get(position).rssi.toString() + " dBm")
        val change = beacons.get(position).uuid.toString()
        var ran1 = IntRange(change.indexOf("mManufacturerSpecificData")+27, change.indexOf("mServiceData")-4)
        var case_data : String = beacons.get(position).case
        if(case_data == "1"){
            beaconHolder.image?.setImageResource(R.drawable.red_bar)
            beaconHolder.message?.setText("주변에 응급차량이 있습니다. 양보 부탁드립니다.")
            beaconHolder.rail?.setText(beacons.get(position).rail.toString()+" 차선")
        } else if(case_data == "2"){
            beaconHolder.image?.setImageResource(R.drawable.blue_bar)
            beaconHolder.message?.setText(beacons.get(position).rail+ " 차선 비워주세요.")
            beaconHolder.rail?.setText(beacons.get(position).rail.toString()+" 차선")
        }else if(case_data == "3"){
            beaconHolder.image?.setImageResource(R.drawable.yellow_bar)
            beaconHolder.message?.setText("전방에 사고가 발생했습니다. 조심하세요.")
            beaconHolder.rail?.setText("  ")
        }else if(case_data == "4"){
            beaconHolder.image?.setImageResource(R.drawable.yellow_bar)
            beaconHolder.message?.setText("주변에 공사 중입니다. 비켜가세요.")
            beaconHolder.rail?.setText("  ")
        }else if(case_data == "5"){
            beaconHolder.image?.setImageResource(R.drawable.blue_bar)
            beaconHolder.message?.setText("앞에 차량이 있습니다. 천천히 가세요(비켜주세요)")
            beaconHolder.rail?.setText("  ")
        }
        return convertView
    }

    private inner class BeaconHolder {
        var car_number: TextView? = null
        var rssi: TextView? = null
        var time: TextView? = null
        var message: TextView? = null
        var rail: TextView? = null
        var image: ImageView? = null
    }
}

