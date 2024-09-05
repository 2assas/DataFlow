package com.dataflowstores.dataflow.ui.shifts.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dataflowstores.dataflow.App
import com.dataflowstores.dataflow.R
import com.dataflowstores.dataflow.databinding.FragmentShiftsMainBinding
import com.dataflowstores.dataflow.ui.shifts.ui.openNewShift.LogoutShiftViewModel
import com.dataflowstores.dataflow.ui.shifts.ui.openNewShift.ReopenShiftViewModel


class ShiftsMain : Fragment() {

    companion object {
        fun newInstance() = ShiftsMain()
    }

    private var _binding: FragmentShiftsMainBinding? = null

    private val viewModel: LogoutShiftViewModel by viewModels()

    private val binding get() = _binding!!
    var uuid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShiftsMainBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    @SuppressLint("HardwareIds")
    private fun setupViews() {
        uuid =
            Settings.Secure.getString(requireActivity().contentResolver, Settings.Secure.ANDROID_ID)
        permissions()
        navigation()
        observeSubmit()
    }

    private fun navigation() {
        binding.back.setOnClickListener { requireActivity().finish() }
        binding.openLoginNewShift.setOnClickListener { findNavController().navigate(R.id.action_shiftsMain_to_openLoginNewShift) }
        binding.openNewShift.setOnClickListener { findNavController().navigate(R.id.action_shiftsMain_to_openNewShift) }
        binding.loginToShift.setOnClickListener { findNavController().navigate(R.id.action_shiftsMain_to_enterShift) }
        binding.closeShift.setOnClickListener { findNavController().navigate(R.id.action_shiftsMain_to_closeShift) }
        binding.openClosedShift.setOnClickListener { findNavController().navigate(R.id.action_shiftsMain_to_reopenShift) }
        binding.logoutFromShift.setOnClickListener {
            submitButton(
                0
            )
        }
    }


    private fun permissions() {
        with(App.currentUser) {
            if (mobileOpenLogInNewShift == 0) binding.openLoginNewShift.isEnabled = false
            if (mobileOpenNewShift == 0) binding.openNewShift.isEnabled = false
            if (mobileLogInToShift == 0) binding.loginToShift.isEnabled = false
            if (mobileCloseShift == 0) binding.closeShift.isEnabled = false
            if (mobileOpenClosedShift == 0) binding.openClosedShift.isEnabled = false
            if (mobileLogOutFromShift == 0) binding.logoutFromShift.isEnabled = false
        }
    }


    private fun observeSubmit() {
        viewModel.shifsResponseLiveData.observe(viewLifecycleOwner) { data ->
            binding.progress.visibility = View.GONE
            run {
                when (data.status) {
                    1 -> {
                        alertDialog(data.message, 0)
                        App.currentUser.logIn_ShiftBranchISN =
                            data.data.LogIn_ShiftBranchISN.toInt()
                        App.currentUser.logIn_ShiftISN = data.data.LogIn_ShiftISN.toInt()
                    }

                    5 -> {
                        alertDialog(data.message, 1)
                    }

                    0 -> {
                        alertDialog(data.message, 0)
                    }
                }
            }
        }
    }

    private fun submitButton(
        skipConfirmation: Int
    ) {
        binding.progress.visibility = View.VISIBLE
        viewModel.logoutFromShift(
            uuid = uuid,
            skipConfirmation = skipConfirmation
        )
    }

    private fun alertDialog(message: String, skipConfirmation: Int) {
        val builder = AlertDialog.Builder(context)
            .setMessage(message)
        builder.setPositiveButton("موافق ") { dialog, which ->
            if (skipConfirmation == 1)
                run {
                    submitButton(
                        skipConfirmation
                    )
                }
            dialog.dismiss()
        }
        // Show or hide the cancel button based on skipConfirmation
        if (skipConfirmation == 1) {
            builder.setNegativeButton("إلغاء") { dialog, which ->
                dialog.dismiss() // Dismiss the dialog on cancel
            }
        }

        builder.create().show()
    }
}