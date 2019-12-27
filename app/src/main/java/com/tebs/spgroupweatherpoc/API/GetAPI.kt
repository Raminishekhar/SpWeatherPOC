package com.tebs.spgroupweatherpoc.API

import android.os.AsyncTask
import com.tebs.spgroupweatherpoc.Interface.OnTaskComplete
import org.json.JSONObject
import java.net.URL

class GetAPI() : AsyncTask<Unit, Unit, String>() {
    lateinit var listener: OnTaskComplete
    lateinit var url: String

    constructor(listener: OnTaskComplete, url: String) : this() {
        this.listener = listener
        this.url = url

    }

    override fun doInBackground(vararg params: Unit?): String? {
        return URL(url).readText(Charsets.UTF_8)
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        listener.onTaskCompleted(JSONObject(result).toString())
    }
}