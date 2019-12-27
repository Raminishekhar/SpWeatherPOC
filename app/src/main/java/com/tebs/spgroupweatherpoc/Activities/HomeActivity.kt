package com.tebs.spgroupweatherpoc.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tebs.spgroupweatherpoc.API.GetAPI
import com.tebs.spgroupweatherpoc.Adapter.CityAdapter
import com.tebs.spgroupweatherpoc.Database.DbWorkerThread
import com.tebs.spgroupweatherpoc.Database.SgData
import com.tebs.spgroupweatherpoc.Interface.OnTaskComplete
import com.tebs.spgroupweatherpoc.Model.City
import com.tebs.spgroupweatherpoc.R
import com.tebs.spgroupweatherpoc.Utils.APIConstantsUrl.Companion.Key
import com.tebs.spgroupweatherpoc.Utils.APIConstantsUrl.Companion.SearchUrl
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity(), OnTaskComplete {


    public override fun onTaskCompleted(mStrResult: String) {
        parseAndPopulate(mStrResult)
    }

    fun parseAndPopulate(mStrResult: String) {
        try {
            val jsonObj = JSONObject(mStrResult)
            val SearchObj = jsonObj.getJSONObject("search_api")
            val resultArray = SearchObj.getJSONArray("result")

            var areaArray = ArrayList<City>()
            for (i in 0..resultArray!!.length() - 1) {
                val innerObj = resultArray.getJSONObject(i)
                val name = innerObj.getJSONArray("areaName")
                val country_name = innerObj.getJSONArray("country")
                val city = City(
                    name.getJSONObject(0).optString("value"),
                    innerObj.optString("longitude"),
                    innerObj.optString("latitude"),
                    country_name.getJSONObject(0).optString("value")
                )
                areaArray.add(city)
            }
            mListCity.adapter = CityAdapter(areaArray, this,
                {
                    val intent = Intent(this, WeatherActivity::class.java)
                    intent.putExtra("city", it)
                    startActivity(intent)
                })

            val imm =
                applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(mEtSearch.windowToken, 0)

            if (areaArray.size == 0) {
                mTvHeading.visibility = View.VISIBLE
                mTvHeading.text = getString(R.string.empty)
            } else {
                mTvHeading.visibility = View.VISIBLE
                mTvHeading.text = getString(R.string.search_result)
            }

        } catch (e: Exception) {

        }
    }


    lateinit var mEtSearch: EditText
    private lateinit var mTvHeading: TextView
    private lateinit var mListCity: RecyclerView
    private lateinit var timer: Timer

    private lateinit var mDbWorkerThread: DbWorkerThread
    private var mDb: SgData? = null
    private val mUiHandler = Handler()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mEtSearch = this.edt_search
        mTvHeading = this.tv_heading
        mListCity = this.rv_city_list
        mEtSearch.addTextChangedListener(searchTextWatcher)
        mDb = SgData.getInstance(this)
        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()
        mListCity.layoutManager = LinearLayoutManager(this)
        fetchWeatherDataFromDb()
    }


    fun fetchWeatherDataFromDb() {
        val task = Runnable {
            val cities =
                mDb?.mCityDAO()?.getAllCities()
            mUiHandler.post({
                if (cities != null) {
                    var areaArray = ArrayList<City>()

                    for (city in cities) {
                        areaArray.add(city)
                    }

                    mListCity.adapter = CityAdapter(areaArray, this,
                        {
                            val intent = Intent(this, WeatherActivity::class.java)
                            intent.putExtra("city", it)
                            startActivity(intent)
                        })

                    val imm =
                        applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(mEtSearch.windowToken, 0)

                    if (areaArray.size == 0) {
                        mTvHeading.visibility = View.GONE
                    } else {
                        mTvHeading.visibility = View.VISIBLE
                        mTvHeading.text = getString(R.string.history)
                    }
                }

            })
        }
        mDbWorkerThread.postTask(task)
    }


    private val searchTextWatcher = object : TextWatcher {
        override fun afterTextChanged(arg0: Editable) {
            timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    if (mEtSearch.text.isEmpty()) {
                        fetchWeatherDataFromDb()
                    } else {
                        var url = SearchUrl + mEtSearch.text + Key
                        GetAPI(this@HomeActivity, url).execute()
                    }

                }
            }, 600)
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            try {
                timer.cancel()
            } catch (e: Exception) {
            }
        }
    }
}
