package com.paytm.insider.common.retrofit

import com.paytm.insider.common.datamodel.Home
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface HomeApi {
    @GET("/home")
    fun getData(@QueryMap params: HashMap<String, String>): Call<Home>
}