package com.tecwec.getweather

import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity() {
    lateinit var cityNameBox:EditText
    lateinit var searchBtn:ImageView
    lateinit var cityNameText:TextView
    lateinit var tempratureText:TextView
    lateinit var mainIcon:ImageView
    lateinit var condition:TextView
    lateinit var recyclerView: RecyclerView
    lateinit var rvData:ArrayList<RV_data>
    var defaultCity="delhi"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView=findViewById(R.id.rv_containerID)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)


        //when app start
        var sharedPreferences=getSharedPreferences("weatherData", MODE_PRIVATE)
        if (sharedPreferences.contains("currentCity")) {
            getWeatherInfo(sharedPreferences.getString("currentCity", "delhi").toString())
        }else{
            getWeatherInfo("delhi")
        }


        //when click search btn
        searchBtn=findViewById(R.id.idIVSearch)
        searchBtn.setOnClickListener {
            cityNameBox = findViewById(R.id.idEdtCity)
            var cityName = cityNameBox.text.toString()
            //save current city
            val sharedPreferences=getSharedPreferences("weatherData", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply {
                putString("currentCity", cityName)
                apply()
            }
            //get weather info
            getWeatherInfo(cityName)
        }

    }





//
//    fun getCityName(){
//
//    }

    fun getWeatherInfo(cityName:String) {
        val url = "http://api.weatherapi.com/v1/forecast.json?key=15a701cbb84e403285982054210709&q=$cityName&days=1&aqi=yes&alerts"
        var requestQueue = Volley.newRequestQueue(this)
        val requestData = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            cityNameText=findViewById(R.id.idTVCityName)
            tempratureText=findViewById(R.id.idTVTemperature)
            mainIcon=findViewById(R.id.idIVIcon)
            condition=findViewById(R.id.idTVCondition)
            cityNameText.text=response.getJSONObject("location").getString("name")
            tempratureText.text=response.getJSONObject("current").getString("temp_c").toFloat().toInt().toString()+"°C"
            condition.text=response.getJSONObject("current").getJSONObject("condition").getString("text")
            var condIcon=response.getJSONObject("current").getJSONObject("condition").getString("icon")
            Picasso.get().load("http:$condIcon").into(mainIcon)

            //forecast
            var forecastobj=response.getJSONObject("forecast")
            var forecastArr=forecastobj.getJSONArray("forecastday").getJSONObject(0)
            var arrHour=forecastArr.getJSONArray("hour")
            rvData=ArrayList()
            for(i in 0 until arrHour.length()){
                var hourobj=arrHour.getJSONObject(i)
                var timeText=hourobj.getString("time")
                val timeLst = timeText.split(" ").toTypedArray()
                var time=timeLst[1]
                var temp=hourobj.getString("temp_c").toFloat().toInt().toString()+"°C"
                var windSpeed=hourobj.getString("wind_kph")+" km/h"
                var icon="http:"+hourobj.getJSONObject("condition").getString("icon")
                rvData.add(RV_data(time,temp,windSpeed,icon))
            }

            //add data to recycler view
            var rvAdapter=RV_adapter(rvData)
            recyclerView.adapter=rvAdapter


        }, {
            Toast.makeText(this, "City Not Found", Toast.LENGTH_SHORT).show()
        })
        requestQueue.add(requestData)

    }

}