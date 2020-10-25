package com.paytm.insider.home.repository

import com.paytm.insider.common.datamodel.Event
import com.paytm.insider.common.datamodel.Home

interface DataSource {
    fun getData(
        params: HashMap<String, String>,
        callback: Callback<Home>,
        forceRemoteFetch: Boolean = false
    )

    fun getEvents(slugs: List<String>, callback: Callback<List<Event>>)

    interface Remote {
        suspend fun fetchData(params: HashMap<String, String>, callback: Callback<Home>)
    }

    interface Local {
        suspend fun loadData(callback: Callback<Home>)
        suspend fun save(data: Home)
        suspend fun deleteAll()
        suspend fun loadEvents(slugs: List<String>, callback: Callback<List<Event>>)
    }

    interface Callback<T> {
        fun onSuccess(responseCode: Int, data: T?, message: String? = null)

        fun onFailure(errorCode: Int, message: String? = null)
    }
}