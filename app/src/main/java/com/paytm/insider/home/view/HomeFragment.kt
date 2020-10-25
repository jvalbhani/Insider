package com.paytm.insider.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.paytm.insider.R
import com.paytm.insider.common.adapter.event.EventAdapter
import com.paytm.insider.common.datamodel.Event
import com.paytm.insider.common.dialog.ErrorDialog
import com.paytm.insider.common.dialog.LoadingDialog
import com.paytm.insider.home.HomeContract
import com.paytm.insider.home.presenter.HomePresenter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class HomeFragment : Fragment(), HomeContract.View {

    private val presenter: HomePresenter by inject()
    private val events = mutableListOf<Event>()
    private val groups = mutableMapOf<String, List<String>>()
    private val loadingDialog = LoadingDialog()
    private val errorDialog = ErrorDialog()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        presenter.init()
    }

    private fun initViews() {
        presenter.setView(this)
        rvEvents.let {
            val adapter = EventAdapter(events)
            adapter.onItemClickListener = object : EventAdapter.OnItemClickListener {
                override fun onClick(event: Event) {
                    //Todo: Implement Detail View
                }
            }
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }

        spLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) = presenter.loadHomePage((view as TextView).text.toString())

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        spGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                groups[(view as TextView).text.toString()]?.let { presenter.onGroupSelected(it) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
    }

    override fun setEvents(eventList: List<Event>) {
        GlobalScope.launch(Dispatchers.Main) {
            events.clear()
            events.addAll(eventList)
            rvEvents.adapter?.notifyDataSetChanged()
        }
    }

    override fun setLocation(list: List<String>, selected: Int) {
        spLocation.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list)
        if (selected != -1) {
            spLocation.setSelection(selected)
        }
    }

    override fun enableLocationPicker(enable: Boolean) {
        Log.d("locationPicker", "$enable")
        spLocation.isEnabled = enable
    }

    override fun setGroupList(groupsMap: HashMap<String, List<String>>?) {
        GlobalScope.launch(Dispatchers.Main) {
            groups.clear()
            groupsMap?.let {
                groups.putAll(it)
                spGroup.adapter =
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        groupsMap.keys.toList()
                    )
            }
        }
    }

    override fun showErrorMessage() {
        GlobalScope.launch(Dispatchers.Main) {
            if (!loadingDialog.isVisible) {
                errorDialog.show(requireFragmentManager(), "")
            }
        }
    }

    override fun showLoadingDialog() {
        GlobalScope.launch(Dispatchers.Main) {
            if (!loadingDialog.isVisible) {
                loadingDialog.show(requireFragmentManager(), "")
            }
        }
    }

    override fun hideLoadingDialog() {
        GlobalScope.launch(Dispatchers.Main) {
            if (loadingDialog.isVisible) {
                loadingDialog.dismiss()
            }
        }
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }
}