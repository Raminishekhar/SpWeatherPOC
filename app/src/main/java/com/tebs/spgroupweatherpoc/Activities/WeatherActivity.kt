package com.tebs.spgroupweatherpoc.Activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tebs.spgroupweatherpoc.API.GetAPI
import com.tebs.spgroupweatherpoc.Database.DbWorkerThread
import com.tebs.spgroupweatherpoc.Database.SgData
import com.tebs.spgroupweatherpoc.Interface.OnTaskComplete
import com.tebs.spgroupweatherpoc.Model.City
import com.tebs.spgroupweatherpoc.R
import com.tebs.spgroupweatherpoc.Utils.APIConstantsUrl
import com.tebs.spgroupweatherpoc.Utils.AppUtil
import kotlinx.android.synthetic.main.activity_weather.*
import org.json.JSONObject
import java.net.URL

class WeatherActivity : AppCompatActivity(), OnTaskComplete {
    override fun onTaskCompleted(mStrResult: String) {
        parseAndDisplay(mStrResult)
    }

    private fun parseAndDisplay(mStrResult: String) {
        try {
            val jsonObj = JSONObject(mStrResult)
            val SearchObj = jsonObj.getJSONObject("data")
            val currentConditionArray = SearchObj.getJSONArray("current_condition")

            val currentObj = currentConditionArray.getJSONObject(0)

            insertWeatherDataInDb(city)

            mPb.visibility = View.GONE

            mTvTemp.text = "Temperature : " + currentObj.optString("temp_C") + "C"
            mTvWeatherDesc.text =
                "Weather Desc : " + currentObj.getJSONArray("weatherDesc").getJSONObject(0).optString(
                    "value"
                )
            mTvHumidity.text = "Humidity : " + currentObj.optString("humidity") + "%"

            DownLoadImageTask(mImgWeather)
                .execute(currentObj.getJSONArray("weatherIconUrl").getJSONObject(0).optString("value"))


        } catch (e: Exception) {
            mPb.visibility = View.GONE
            Toast.makeText(WeatherActivity@ this, "Something went wrong", Toast.LENGTH_LONG)
        }
    }

    private lateinit var mTvHumidity: TextView
    private lateinit var mImgWeather: ImageView
    private lateinit var mTvTemp: TextView
    private lateinit var mTvWeatherDesc: TextView
    private lateinit var mPb: ProgressBar
    private lateinit var mDbWorkerThread: DbWorkerThread
    private var mDb: SgData? = null
    private lateinit var city: City
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        mTvHumidity = this.tv_humidity
        mTvWeatherDesc = this.tv_weather_desc
        mTvTemp = this.tv_temp
        mImgWeather = this.img_weather
        mPb = this.pb
        mDb = SgData.getInstance(this)
        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        city = intent.getParcelableExtra("city")
        val isNetworkAvilable = AppUtil.isOnline(applicationContext)

        if (isNetworkAvilable) {
            var url =
                APIConstantsUrl.WeatherUrl + city.lati + "," + city.longi + APIConstantsUrl.Key
            GetAPI(this@WeatherActivity, url).execute()
            mPb.visibility = View.VISIBLE
        } else {
            Toast.makeText(applicationContext, getString(R.string.no_network), Toast.LENGTH_SHORT).show()
        }

    }

    private class DownLoadImageTask(internal val imageView: ImageView) :
        AsyncTask<String, Void, Bitmap?>() {
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
            if (result != null) {
                imageView.setImageBitmap(result)
            } else {
            }
        }
    }


    private fun insertWeatherDataInDb(city: City) {
        val task = Runnable { mDb?.mCityDAO()?.insertCity(city) }
        mDbWorkerThread.postTask(task)
    }
}
