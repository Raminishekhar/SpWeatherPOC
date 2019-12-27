package com.tebs.spgroupweatherpoc.Database

import android.os.Handler
import android.os.HandlerThread
import android.util.Log

class DbWorkerThread(threadName: String) : HandlerThread(threadName) {

    private lateinit var mWorkerHandler: Handler

    override fun onLooperPrepared() {
        super.onLooperPrepared()
        Log.e("in thread class", "loop method")
        mWorkerHandler = Handler(looper)
    }

    fun postTask(task: Runnable) {
        mWorkerHandler = Handler(this.looper)
        Log.e("in thread class", "post task method")

        mWorkerHandler.post(task)
    }

}
