package com.paytm.insider.home

import com.paytm.insider.common.datamodel.Event

class HomeContract {
    interface View {
        fun setEvents(eventList: List<Event>)
        fun setLocation(list: List<String>, selected: Int)
        fun enableLocationPicker(enable: Boolean)
        fun setGroupList(groups: HashMap<String, List<String>>?)
        fun showErrorMessage()
        fun showLoadingDialog()
        fun hideLoadingDialog()
    }

    interface Presenter {
        fun init()
        fun loadHomePage(city: String)
        fun onGroupSelected(list: List<String>)
        fun detachView()
        fun setView(view: View)
    }
}