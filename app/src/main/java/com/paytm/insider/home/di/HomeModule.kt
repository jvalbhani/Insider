package com.paytm.insider.home.di

import com.paytm.insider.common.room.database.InsiderDatabase
import com.paytm.insider.home.presenter.HomePresenter
import com.paytm.insider.home.repository.DataSource
import com.paytm.insider.home.repository.HomeRepository
import com.paytm.insider.home.repository.local.LocalDataSource
import com.paytm.insider.home.repository.remote.RemoteDataSource
import org.koin.dsl.module

val homeModule = module {
    single { get<InsiderDatabase>().getHomeDao() }
    single { RemoteDataSource(get()) as DataSource.Remote }
    single { LocalDataSource(get()) as DataSource.Local }
    single { HomeRepository(get(), get(), get()) }
    factory { HomePresenter(get(), get()) }
}