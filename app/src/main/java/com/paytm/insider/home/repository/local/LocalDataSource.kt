package com.paytm.insider.home.repository.local

import com.paytm.insider.common.datamodel.Event
import com.paytm.insider.common.datamodel.Home
import com.paytm.insider.common.room.dao.HomeDao
import com.paytm.insider.common.room.mappers.map
import com.paytm.insider.home.repository.DataSource

class LocalDataSource(private val homeDao: HomeDao) : DataSource.Local {

    override suspend fun loadData(callback: DataSource.Callback<Home>) {
        val data = homeDao.loadAll().map { it.map() }
        if (data.isNullOrEmpty()) {
            callback.onFailure(-1, "Failed to load data from cache")
            return
        }
        callback.onSuccess(0, data[0])
    }

    override suspend fun save(data: Home) {
        homeDao.saveAll(data.map())
    }

    override suspend fun deleteAll() {
        homeDao.deleteAll()
    }

    override suspend fun loadEvents(
        slugs: List<String>,
        callback: DataSource.Callback<List<Event>>
    ) {
        val data = homeDao.loadEvents(slugs)
        callback.onSuccess(0, data.map { it.map() })
    }
}