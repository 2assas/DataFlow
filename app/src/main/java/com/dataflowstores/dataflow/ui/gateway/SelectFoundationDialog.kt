package com.dataflowstores.dataflow.ui.gateway

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.dataflowstores.dataflow.App
import com.dataflowstores.dataflow.R
import com.dataflowstores.dataflow.databinding.SelectFoundationDialogBinding
import com.dataflowstores.dataflow.ui.SplashScreen


class SelectFoundationDialog : DialogFragment() {

        private lateinit var binding: SelectFoundationDialogBinding
        private var selectedNumber = 1
        private var mListener: DialogListener? = null

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
        ): View {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setCancelable(false)
            binding =
                DataBindingUtil.inflate(inflater, R.layout.select_foundation_dialog, container, false)
            return binding.root
        }

        fun setListener(listener: DialogListener) {
            mListener = listener
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            if (savedInstanceState != null) {
                startActivity(Intent(requireActivity(), SplashScreen::class.java))
                requireActivity().finishAffinity()
            } else {
                assert(arguments != null)
                arguments?.let { foundationSpinner(it.getInt("FCount")) }
                binding.save.setOnClickListener {
                    App.selectedFoundation = selectedNumber
                    dialog?.dismiss()
                }

            }
        }

        override fun onAttach(context: Context) {
            super.onAttach(context)
            mListener = try {
                context as DialogListener
            } catch (e: ClassCastException) {
                throw ClassCastException("$context must implement DialogListener")
            }
        }

        override fun onDismiss(dialog: DialogInterface) {
            super.onDismiss(dialog)
            mListener?.onDialogDismissed()
        }

        override fun onStart() {
            super.onStart()
            dialog?.window?.setLayout(
                context?.resources?.displayMetrics?.widthPixels!! - 60,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }


        private fun foundationSpinner(count: Int) {
        val foundationName = ArrayList<String>()
        var i = 0
        while (i < count) {
            foundationName.add("المؤسسة ${i + 1}")
            i++
        }
        val aa: ArrayAdapter<String> = ArrayAdapter<String>(
            requireActivity(), android.R.layout.simple_spinner_item, foundationName
        )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.foundationSpinner.adapter = aa
        binding.foundationSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    selectedNumber = i + 1
                    Log.e("checkFoundation", "Number = ${i + 1}")
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {
                    selectedNumber = 1
                }
            }

    }

    interface DialogListener {
        fun onDialogDismissed()
    }

}