package com.paytm.insider.common.di

import android.content.Context
import androidx.room.Room
import com.paytm.insider.common.room.database.InsiderDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val storageModule = module {
    single {
        Room.databaseBuilder(androidContext(), InsiderDatabase::class.java, "InsiderDB").build()
    }
    single { androidContext().getSharedPreferences("insider_prefs", Context.MODE_PRIVATE) }
}