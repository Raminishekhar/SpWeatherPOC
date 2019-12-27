package com.tebs.spgroupweatherpoc.Utils

interface APIConstantsUrl {
    companion object {
        val BaseUrl: String = "https://api.worldweatheronline.com/premium/v1/"
        val SearchUrl: String = BaseUrl + "search.ashx?query="
        val WeatherUrl: String = BaseUrl + "weather.ashx?query="
        val Key: String = "&format=json&key=686e7daf43c54574bed65256191912"

    }
}