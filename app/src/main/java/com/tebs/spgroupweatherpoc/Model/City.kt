package com.tebs.spgroupweatherpoc.Model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.OffsetDateTime

@Entity(tableName = "city_table")@Parcelize
data class City (
    @PrimaryKey val areaName: String,
                val longi:String,
                val lati:String,
                val countryName:String

): Parcelable
