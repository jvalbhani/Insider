package com.paytm.insider.common.di

import com.paytm.insider.common.utility.NetworkUtility
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://api.insider.in")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single { NetworkUtility(get()) }
}