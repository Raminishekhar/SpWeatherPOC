package com.tebs.spgroupweatherpoc.Interface

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tebs.spgroupweatherpoc.Model.City

@Dao
interface CityDAO {
    @Query("SELECT * from city_table ORDER BY rowid Desc limit 10")
    fun getAllCities(): List<City>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(city: City)

}