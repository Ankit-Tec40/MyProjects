package com.tecwec.getweather

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RV_adapter(var weatherData:ArrayList<RV_data>) : RecyclerView.Adapter<viewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var layoutInflater =LayoutInflater.from(parent.context)
        var view=layoutInflater.inflate(R.layout.rv_items,parent,false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        var data=weatherData[position]
        holder.time.text=data.time
        holder.temperature.text=data.temperature
        holder.windSpeed.text=data.windSped
        Picasso.get().load(data.icon).into(holder.icon)
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }
}

class viewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
    var temperature=itemView.findViewById<TextView>(R.id.rv_temperatureID)
    var time=itemView.findViewById<TextView>(R.id.rv_timeID)
    var windSpeed=itemView.findViewById<TextView>(R.id.rv_wind_speedID)
    var icon=itemView.findViewById<ImageView>(R.id.rv_condition_imageID)
}