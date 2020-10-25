package com.paytm.insider

import android.app.Application
import com.paytm.insider.common.di.networkModule
import com.paytm.insider.common.di.storageModule
import com.paytm.insider.home.di.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class InsiderApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@InsiderApplication)
            modules(listOf(networkModule, storageModule, homeModule))
        }
    }
}