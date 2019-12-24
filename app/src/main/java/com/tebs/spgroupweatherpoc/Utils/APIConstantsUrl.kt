package com.tebs.spgroupweatherpoc.Utils

interface APIConstantsUrl {
    companion object {
        val BaseUrl: String = "https://api.worldweatheronline.com/premium/v1/"
        val SearchUrl: String = BaseUrl +"search.ashx?query="
    }
}