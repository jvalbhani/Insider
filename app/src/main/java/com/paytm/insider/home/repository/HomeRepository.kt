package com.paytm.insider.home.repository

import android.content.SharedPreferences
import com.paytm.insider.common.datamodel.Event
import com.paytm.insider.common.datamodel.Home
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeRepository(
    private val localDataSource: DataSource.Local,
    private val remoteDataSource: DataSource.Remote,
    private val sharedPrefs: SharedPreferences
) : DataSource {

    override fun getData(
        params: HashMap<String, String>,
        callback: DataSource.Callback<Home>,
        forceRemoteFetch: Boolean
    ) {
        if (!forceRemoteFetch && isLocalCacheValid(params["city"])) {
            fetchLocally(params, callback)
            return
        }
        fetchRemotely(params, callback)
    }

    override fun getEvents(
        slugs: List<String>,
        callback: DataSource.Callback<List<Event>>
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            localDataSource.loadEvents(slugs, callback)
        }
    }

    /*
        modify to read from remote config / API
        returns list of cities
     */
    fun getSupportedCities(): List<String> {
        return listOf("Mumbai", "Pune", "Bangalore", "Hyderabad", "Ahmedabad", "Delhi")
    }

    fun getSelectedCity(): String? = sharedPrefs.getString(KEY_CITY, null)

    private fun fetchLocally(params: HashMap<String, String>, callback: DataSource.Callback<Home>) {
        GlobalScope.launch {
            localDataSource.loadData(object : DataSource.Callback<Home> {

                override fun onSuccess(responseCode: Int, data: Home?, message: String?) {
                    callback.onSuccess(responseCode, data, message)
                }

                override fun onFailure(errorCode: Int, message: String?) {
                    fetchRemotely(params, callback)
                }
            })
        }
    }

    private fun fetchRemotely(
        params: HashMap<String, String>,
        callback: DataSource.Callback<Home>
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            remoteDataSource.fetchData(params, object : DataSource.Callback<Home> {
                override fun onSuccess(responseCode: Int, data: Home?, message: String?) {
                    if (data != null) {
                        saveAll(data, params["city"])
                        callback.onSuccess(responseCode, data, message)
                        return
                    }
                    callback.onFailure(responseCode, message)
                }

                override fun onFailure(errorCode: Int, message: String?) {
                    callback.onFailure(errorCode, message)
                }
            })
        }
    }

    private fun isLocalCacheValid(city: String?): Boolean {
        val cityInPrefs = sharedPrefs.getString(KEY_CITY, null) ?: "NoSelection"
        return cityInPrefs.equals(city, true)
    }

    private fun saveAll(data: Home, city: String?) {
        sharedPrefs.edit().putString(KEY_CITY, city).apply()
        GlobalScope.launch(Dispatchers.IO) {
            localDataSource.deleteAll()
            localDataSource.save(data)
        }
    }

    companion object {
        private const val KEY_CITY = "city"
    }
}