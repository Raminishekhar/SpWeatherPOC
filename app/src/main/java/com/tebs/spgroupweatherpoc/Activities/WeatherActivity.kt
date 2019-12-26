package com.tebs.spgroupweatherpoc.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.tebs.spgroupweatherpoc.API.GetAPI
import com.tebs.spgroupweatherpoc.Adapter.CityAdapter
import com.tebs.spgroupweatherpoc.Interface.OnTaskComplete
import com.tebs.spgroupweatherpoc.Model.City
import com.tebs.spgroupweatherpoc.R
import com.tebs.spgroupweatherpoc.Utils.APIConstantsUrl
import kotlinx.android.synthetic.main.activity_weather.*
import org.json.JSONObject
import java.net.URL

class WeatherActivity : AppCompatActivity() , OnTaskComplete {
    override fun onTaskCompleted(mStrResult: String) {
        try {
            val jsonObj = JSONObject(mStrResult)
            val SearchObj = jsonObj.getJSONObject("data")
            val CurrentConditionArray = SearchObj.getJSONArray("current_condition")

            val CurrentObj=CurrentConditionArray.getJSONObject(0);
            pb.visibility=View.GONE

            mTvTemp.text="Temp : "+CurrentObj.optString("temp_C")+"C"
            mTvWeatherDesc.text="Weather Desc : "+CurrentObj.getJSONArray("weatherDesc").getJSONObject(0).optString("value")
            DownLoadImageTask(mImgWeather)
                .execute(CurrentObj.getJSONArray("weatherIconUrl").getJSONObject(0).optString("value"))

            mTvHumidity.text="Humidity : "+CurrentObj.optString("humidity")+"%"

        } catch (e: Exception) {
            pb.visibility=View.GONE
            Toast.makeText(WeatherActivity@this,"Something went wrong",Toast.LENGTH_LONG)
        }
    }

    private lateinit var mTvHumidity: TextView
    private lateinit var mImgWeather: ImageView
    private lateinit var mTvTemp: TextView
    private lateinit var mTvWeatherDesc: TextView
    private lateinit var mPb: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        mTvHumidity=this.tv_humidity
        mTvWeatherDesc=this.tv_weather_desc
        mTvTemp=this.tv_temp
        mImgWeather=this.img_weather
        mPb=this.pb
        val longi=intent.getStringExtra("longi");
        val lati=intent.getStringExtra("lati");
        val name=intent.getStringExtra("");
        var url = APIConstantsUrl.WeatherUrl +lati+","+longi+APIConstantsUrl.Key
        GetAPI(this@WeatherActivity,url).execute();
        pb.visibility=View.VISIBLE
    }

    private class DownLoadImageTask(internal val imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
        override fun doInBackground(vararg urls: String): Bitmap? {
            val urlOfImage = urls[0]
            return try {
                val inputStream = URL(urlOfImage).openStream()
                BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        override fun onPostExecute(result: Bitmap?) {
            if(result!=null){
                imageView.setImageBitmap(result)
            }else{
            }
        }
    }
}
