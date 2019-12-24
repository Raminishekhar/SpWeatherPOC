package com.tebs.spgroupweatherpoc.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tebs.spgroupweatherpoc.API.SearchAPI
import com.tebs.spgroupweatherpoc.Adapter.CityAdapter
import com.tebs.spgroupweatherpoc.Interface.OnTaskComplete
import com.tebs.spgroupweatherpoc.Model.City
import com.tebs.spgroupweatherpoc.R
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject
import android.text.Editable
import android.text.TextWatcher
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import android.widget.TextView
import com.tebs.spgroupweatherpoc.Utils.APIConstantsUrl.Companion.BaseUrl
import com.tebs.spgroupweatherpoc.Utils.APIConstantsUrl.Companion.SearchUrl
import java.util.*
import kotlin.collections.ArrayList




class HomeActivity : AppCompatActivity(), OnTaskComplete {


    override fun onTaskCompleted(mStrResult: String) {
        try {
            val jsonObj = JSONObject(mStrResult)
            val SearchObj = jsonObj.getJSONObject("search_api")
            val resultArray = SearchObj.getJSONArray("result")

            var areaArray = ArrayList<City>()
            for (i in 0..resultArray!!.length() - 1) {
                val innerObj=resultArray.getJSONObject(i)
                val name = innerObj.getJSONArray("areaName")
                val country_name = innerObj.getJSONArray("country")
                val city= City(name.getJSONObject(0).optString("value"),
                    innerObj.optString("longitude"),
                    innerObj.optString("latitude"),
                    country_name.getJSONObject(0).optString("value"))
                areaArray.add(city)
            }
            mListCity.layoutManager = LinearLayoutManager(this)
            mListCity.adapter = CityAdapter(areaArray, this)

            if(areaArray.size==0)
            {
                mTvEmpty.visibility= View.VISIBLE
                mListCity.visibility=View.GONE
            }
            else{
                mTvEmpty.visibility= View.GONE
                mListCity.visibility=View.VISIBLE
            }
        } catch (e: Exception) {
            mTvEmpty.visibility= View.VISIBLE
            mListCity.visibility=View.GONE
        }
    }

    private lateinit var mEtSearch: EditText
    private lateinit var mTvEmpty: TextView
    private lateinit var mListCity: RecyclerView
    private lateinit var timer: Timer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        mEtSearch=this.edt_search
        mTvEmpty=this.tv_empty_list
        mListCity=this.rv_city_list
        mEtSearch.addTextChangedListener(searchTextWatcher)
    }


    private val searchTextWatcher = object : TextWatcher {
        override fun afterTextChanged(arg0: Editable) {
            timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    var url =SearchUrl+mEtSearch.text+"&format=json&key=686e7daf43c54574bed65256191912"
                    SearchAPI(this@HomeActivity,url).execute();
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
