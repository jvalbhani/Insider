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
    private var loadingDialog: LoadingDialog? = null

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

    override fun setGroupList(
        groups: HashMap<String, List<String>>?,
        selected: String?
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            groups?.let {
                var selectedKey = selected
                val groupTitles = groups.keys.toList()
                spGroup.adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    groupTitles
                )
                selected?.let { spGroup.setSelection(groupTitles.indexOf(it)) }
                spGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        groups[(view as TextView).text.toString()]?.let {
                            if (selectedKey.isNullOrBlank()) {
                                presenter.onGroupSelected(it)
                            }
                            selectedKey = null
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) = Unit
                }
            }
        }
    }

    override fun showErrorMessage() {
        GlobalScope.launch(Dispatchers.Main) {
            val errorDialog = ErrorDialog()
                errorDialog.show(requireFragmentManager(), "")
        }
    }

    override fun showLoadingDialog() {
        GlobalScope.launch(Dispatchers.Main) {
            if (loadingDialog == null) {
                loadingDialog = LoadingDialog()
            }
            if (!loadingDialog!!.isVisible) {
                loadingDialog!!.show(requireActivity().supportFragmentManager, "")
            }
        }
    }

    override fun hideLoadingDialog() {
        GlobalScope.launch(Dispatchers.Main) {
            loadingDialog?.dismissAllowingStateLoss()
        }
    }

    override fun onDestroyView() {
        presenter.detachView()
        super.onDestroyView()
    }
}