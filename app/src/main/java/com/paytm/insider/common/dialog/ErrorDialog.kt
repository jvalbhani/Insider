package com.paytm.insider.common.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.paytm.insider.R
import kotlinx.android.synthetic.main.dialog_error.view.*

class ErrorDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.dialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        isCancelable = false
        return inflater.inflate(R.layout.dialog_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.btnExit.setOnClickListener {
            dismiss()
            activity?.finish()
        }
    }
}