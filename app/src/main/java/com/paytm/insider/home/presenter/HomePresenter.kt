package com.paytm.insider.home.presenter

import android.util.Log
import com.paytm.insider.common.datamodel.Event
import com.paytm.insider.common.datamodel.Home
import com.paytm.insider.common.utility.NetworkUtility
import com.paytm.insider.home.HomeContract
import com.paytm.insider.home.repository.DataSource
import com.paytm.insider.home.repository.HomeRepository
import org.koin.core.KoinComponent
import java.util.*
import kotlin.collections.HashMap

class HomePresenter(
    private val repository: HomeRepository,
    private val networkUtility: NetworkUtility
) : HomeContract.Presenter, KoinComponent {

    private var view: HomeContract.View? = null

    override fun init() {
        Log.d("HomePresenter", "init")
        view?.showLoadingDialog()
        view?.enableLocationPicker(networkUtility.isDeviceConnectedToInternet())
        val cities = repository.getSupportedCities()
        val selected = cities.indexOf(repository.getSelectedCity()?.capitalize())
        view?.setLocation(cities, selected)
        view?.hideLoadingDialog()
    }

    override fun loadHomePage(city: String) {
        Log.d("HomePresenter", "loadHomePage $city")
        view?.showLoadingDialog()
        repository.getData(
            hashMapOf(
                Pair("norm", "1"),
                Pair("filterBy", "go-out"),
                Pair("city", city.toLowerCase(Locale.getDefault()))
            ),
            object : DataSource.Callback<Home> {
                override fun onSuccess(responseCode: Int, data: Home?, message: String?) {
                    setGroupList(data)
                }

                override fun onFailure(errorCode: Int, message: String?) {
                    Log.d("HomePresenter", "loadHomePage error $message")
                    view?.hideLoadingDialog()
                    view?.showErrorMessage()
                }
            }
        )
    }

    override fun onGroupSelected(list: List<String>) {
        view?.showLoadingDialog()
        repository.getEvents(list, object : DataSource.Callback<List<Event>> {
            override fun onSuccess(responseCode: Int, data: List<Event>?, message: String?) {
                Log.d("HomePresenter", "events $data")
                data?.let {
                    view?.setEvents(data)
                } ?: view?.showErrorMessage()
                view?.hideLoadingDialog()
            }

            override fun onFailure(errorCode: Int, message: String?) {
                Log.d("HomePresenter", "events error $message")
                view?.hideLoadingDialog()
                view?.showErrorMessage()
            }
        })
    }

    override fun setView(view: HomeContract.View) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

    private fun setGroupList(home: Home?) {
        Log.d("HomePresenter", "groupList")
        val hashMap = HashMap<String, List<String>>()
        home?.featured?.map { it.slug }?.let {
            hashMap["Featured"] = it
        }
        home?.popular?.map { it.slug }?.let {
            hashMap["Popular"] = it
        }
        home?.list?.groupwiseList?.let {
            hashMap.putAll(it)
        }
        view?.hideLoadingDialog()
        view?.setGroupList(hashMap, "Featured")
        view?.setEvents(getFeaturedEvents(home))
    }

    private fun getFeaturedEvents(home: Home?): List<Event> {
        val events = mutableListOf<Event>()
        home?.featured?.forEach {
            home.list?.masterList?.get(it.slug)?.let { event ->
                events.add(event)
            }
        }
        return events
    }
}