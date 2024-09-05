package com.dataflowstores.dataflow.ui.shifts.ui.openLoginShift

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
import com.dataflowstores.dataflow.App
import com.dataflowstores.dataflow.databinding.FragmentOpenLoginShiftBinding

/**
 * A simple [Fragment] subclass.
 * Use the [OpenLoginNewShift.newInstance] factory method to
 * create an instance of this fragment.
 */
class OpenLoginNewShift : Fragment() {
    private val viewModel: OpenLoginShiftViewModel by viewModels()
    private var _binding: FragmentOpenLoginShiftBinding? = null
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
        _binding = FragmentOpenLoginShiftBinding.inflate(inflater, container, false)
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
                    binding.inputs.editTextOpenShiftAmount.text.toString(),
                    binding.inputs.editTextNotes.text.toString(),
                    0
                )
            }
        }

        binding.inputs.editTextCloseShiftAmount.visibility = View.GONE
        binding.inputs.editTextReceivedAmount.visibility = View.GONE
        binding.inputs.editTextShiftISN.visibility = View.GONE

        observeSubmit()
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
    }

    private fun validateData(): Boolean {
        when {
            binding.inputs.editTextOpenShiftAmount.text.toString().isEmpty() -> {
                binding.inputs.editTextOpenShiftAmount.error = "هـذا الحقل مطلوب"
                return false
            }

            binding.inputs.editTextShiftPassword.text.toString().isEmpty() -> {
                binding.inputs.editTextShiftPassword.error = "هـذا الحقل مطلوب"
                return false
            }
        }
        return true
    }

    private fun submitButton(
        loginPass: String,
        openCash: String,
        notes: String,
        skipConfirmation: Int
    ) {
        binding.progress.visibility = View.VISIBLE
        viewModel.openLoginShift(
            uuid = uuid,
            loginPass = loginPass,
            openCash = openCash,
            notes = notes,
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
                        binding.inputs.editTextOpenShiftAmount.text.toString(),
                        binding.inputs.editTextNotes.text.toString(),
                        skipConfirmation
                    )
                }
            else
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
        binding.inputs.editTextNotes.setText("")
        binding.inputs.editTextOpenShiftAmount.setText("")
        binding.inputs.editTextShiftPassword.setText("")
    }
}