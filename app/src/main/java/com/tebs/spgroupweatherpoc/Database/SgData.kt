package com.tebs.spgroupweatherpoc.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tebs.spgroupweatherpoc.Interface.CityDAO
import com.tebs.spgroupweatherpoc.Model.City


@Database(entities = arrayOf(City::class), version = 1)
abstract class SgData : RoomDatabase() {

    abstract fun mCityDAO(): CityDAO

    companion object {
        private var INSTANCE: SgData? = null

        fun getInstance(context: Context): SgData? {
            if (INSTANCE == null) {
                synchronized(SgData::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        SgData::class.java, "SgData.db"
                    )
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}