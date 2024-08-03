package com.dataflowstores.dataflow.ui.shifts.ui.closeShift

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dataflowstores.dataflow.databinding.FragmentCloseShiftBinding
import com.dataflowstores.dataflow.ui.shifts.ui.openLoginShift.OpenLoginNewShift

class CloseShift : Fragment() {

    private val viewModel: CloseShiftViewModel by viewModels()
    private var _binding: FragmentCloseShiftBinding? = null
    private val binding get() = _binding!!

    var uuid: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCloseShiftBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OpenLoginNewShift().apply {
                arguments = Bundle().apply {}
            }
    }

    @SuppressLint("HardwareIds")
    private fun setupViews() {
        uuid =
            Settings.Secure.getString(requireActivity().contentResolver, Settings.Secure.ANDROID_ID)
        binding.back.setOnClickListener { findNavController().popBackStack() }
        binding.inputs.buttonSubmit.setOnClickListener {
            if (validateData()) {
                submitButton(
                    binding.inputs.editTextShiftPassword.text.toString(),
                    binding.inputs.editTextShiftISN.text.toString(),
                    binding.inputs.editTextCloseShiftAmount.text.toString(),
                    binding.inputs.editTextReceivedAmount.text.toString(),
                    0
                )
            }
        }

        binding.inputs.editTextOpenShiftAmount.visibility = View.GONE
        binding.inputs.editTextNotes.visibility = View.GONE

        observeSubmit()
    }

    private fun observeSubmit() {
        viewModel.shifsResponseLiveData.observe(viewLifecycleOwner) { data ->
            binding.progress.visibility = View.GONE
            run {
                when (data.status) {
                    1 -> {
                        alertDialog(data.message, 0)
                        emptyInput()
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
        viewModel.toastErrorMutableLiveData.observe(
            viewLifecycleOwner
        ) { s: String? ->
            Toast.makeText(
                requireActivity(),
                s,
                Toast.LENGTH_LONG
            ).show()
        }
        viewModel.toastErrorMutableLiveData.observe(
            viewLifecycleOwner
        ) { s: String? ->
            Toast.makeText(
                requireActivity(),
                s,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun validateData(): Boolean {
        when {
            binding.inputs.editTextShiftISN.text.toString().isEmpty() -> {
                binding.inputs.editTextOpenShiftAmount.error = "هـذا الحقل مطلوب"
                return false
            }

            binding.inputs.editTextShiftPassword.text.toString().isEmpty() -> {
                binding.inputs.editTextShiftPassword.error = "هـذا الحقل مطلوب"
                return false
            }

            binding.inputs.editTextReceivedAmount.text.toString().isEmpty() -> {
                binding.inputs.editTextShiftPassword.error = "هـذا الحقل مطلوب"
                return false
            }

            binding.inputs.editTextCloseShiftAmount.text.toString().isEmpty() -> {
                binding.inputs.editTextShiftPassword.error = "هـذا الحقل مطلوب"
                return false
            }
        }
        return true
    }

    private fun submitButton(
        loginPass: String,
        shiftISN: String,
        closeCash: String,
        receivedCash: String,
        skipConfirmation: Int
    ) {
        binding.progress.visibility = View.VISIBLE
        viewModel.closeShift(
            uuid = uuid,
            loginPass = loginPass,
            shiftISN = shiftISN.toLong(),
            closeCash = closeCash,
            receivedCash = receivedCash,
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
                        binding.inputs.editTextShiftPassword.text.toString(),
                        binding.inputs.editTextShiftISN.text.toString(),
                        binding.inputs.editTextCloseShiftAmount.text.toString(),
                        binding.inputs.editTextReceivedAmount.text.toString(),
                        skipConfirmation
                    )
                } else
                findNavController().popBackStack()

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

    private fun emptyInput() {
        binding.inputs.editTextCloseShiftAmount.setText("")
        binding.inputs.editTextReceivedAmount.setText("")
        binding.inputs.editTextShiftISN.setText("")
        binding.inputs.editTextShiftPassword.setText("")
    }
}