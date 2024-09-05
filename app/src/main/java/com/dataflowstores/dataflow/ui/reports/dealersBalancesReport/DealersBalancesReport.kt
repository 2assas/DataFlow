package com.dataflowstores.dataflow.ui.reports.dealersBalancesReport

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.dataflowstores.dataflow.App
import com.dataflowstores.dataflow.R
import com.dataflowstores.dataflow.ViewModels.InvoiceViewModel
import com.dataflowstores.dataflow.databinding.SearchCustomerBalanceBinding
import com.dataflowstores.dataflow.pojo.report.Branches
import com.dataflowstores.dataflow.pojo.report.dealersBalancesReport.DealerCategoriesResponse
import com.dataflowstores.dataflow.pojo.users.CustomerData
import com.dataflowstores.dataflow.pojo.workStation.BranchData
import com.dataflowstores.dataflow.ui.BaseActivity
import com.dataflowstores.dataflow.ui.SplashScreen

class DealersBalancesReport : BaseActivity() {
    lateinit var binding: SearchCustomerBalanceBinding
    var invoiceVM: InvoiceViewModel? = null
    var uuid: String? = null
    var branches: Branches? = null
    private var dealerCategory: DealerCategoriesResponse? = null
    var selectedBranch: BranchData? = null
    var selectedDealerCategory: String? = null
    private val VISIBLE_THRESHOLD = 7  // Adjust this value as needed
    var dealerType: Int = 1

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.search_customer_balance)
        if (savedInstanceState != null) {
            startActivity(Intent(this, SplashScreen::class.java))
            finishAffinity()
        } else {
            uuid = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            invoiceVM = ViewModelProvider(this).get(InvoiceViewModel::class.java)
            binding
            initViews()
            searchButtons()
            observer()
            dealerTypeRadio()
        }
    }

    private fun initViews() {
        binding.title.text = "تقرير حسابات المتعاملين"
        binding.typeContainer.visibility = View.VISIBLE
        invoiceVM!!.getBranches(uuid)
        invoiceVM!!.getDealerCategory(uuid)
        binding.balanceAvailableCheckBox.visibility = View.VISIBLE
        if (App.currentUser.mobileDealersBalancesReport == 0) {
            binding.customerRadio.setEnabled(false)
            binding.customerRadio.setAlpha(.5f)
            binding.supplierRadio.setChecked(true)
            dealerType = 2
        } else {
            binding.customerRadio.setChecked(true)
        }
        if (App.currentUser.mobileSuppliersBalancesReport == 0) {
            binding.supplierRadio.setEnabled(false)
            binding.supplierRadio.setAlpha(.5f)
            dealerType = 1
        }
        binding.searchClient.requestFocus()
        binding.back.setOnClickListener { view -> finish() }
        App.customer = CustomerData()
        invoiceVM!!.toastErrorMutableLiveData.observe(
            this
        ) { s: String? ->
            Toast.makeText(
                this,
                s,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun observer() {
        invoiceVM!!.branchesMutableLiveData.observe(
            this
        ) { branches: Branches ->
            branchSpinner(
                branches
            )
        }
        invoiceVM!!.dealerCategoriesResponseMutableLiveData.observe(
            this
        ) {
            if (it.status == 1 && (it.categories?.size ?: 0) > 0) {
                this.dealerCategory = it
                dealerTypesSpinner(it)
            }
        }
    }

    private fun searchButtons() {
        binding.getClient.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId === EditorInfo.IME_ACTION_SEARCH) {
                binding.searchClient.performClick()
                return@setOnEditorActionListener true // Indicates that the action has been handled
            }
            false
        }
        binding.searchClient.text = "عرض التقرير"
        binding.searchClient.setOnClickListener {
            showDealerResultsFragment(
                if (binding.branchCheckBox.isChecked) selectedBranch!!.branchISN.toString() else null,
                if (binding.branchCheckBox.isChecked) selectedBranch!!.branchName.toString() else null,
                dealerType,
                App.currentUser.workerBranchISN.toString()
            )
        }
    }

    fun branchSpinner(branches: Branches) {
        this.branches = branches
        val branchList = ArrayList<String?>()
        var position = 0
        for (i in branches.branchData.indices) {
            branchList.add(branches.branchData[i].branchName)
            if (App.currentUser.branchISN == branches.branchData[i].branchISN) {
                position = i
            }
        }
        val aa: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, branchList as List<Any?>)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.branchSpinner.setAdapter(aa)
        selectedBranch = branches.branchData[0]
        binding.branchSpinner.setSelection(position)
        val finalPosition = position
        binding.branchSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    selectedBranch = branches.branchData[i]
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {
                    selectedBranch = branches.branchData[finalPosition]
                }
            }
    }

    private fun dealerTypesSpinner(dealerTypes: DealerCategoriesResponse) {

        val typeList = ArrayList<String?>()

        typeList.add("")
        dealerTypes.categories?.let {
            var filteredList: MutableList<String?> = mutableListOf()
            if (dealerType == 1) {
                filteredList = it.filter { category -> category?.dealerType == "1" }
                    .map { category -> category?.dealerCategory }.toMutableList()
            } else {
                filteredList = it.filter { category -> category?.dealerType == "2" }
                    .map { category -> category?.dealerCategory }.toMutableList()
            }

            for (i in filteredList.indices) {
                typeList.add(filteredList[i])
            }
        }
        val aa: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, android.R.layout.simple_spinner_item, typeList as List<Any?>)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.typeSpinner.setAdapter(aa)
        binding.typeSpinner.setSelection(0)
        binding.typeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    selectedDealerCategory = typeList[i]
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {
                    selectedDealerCategory = typeList[0]
                }
            }
    }

    private fun dealerTypeRadio() {
        binding.customerRadio.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                dealerType = 1
                this.dealerCategory?.let {
                    dealerTypesSpinner(it);
                }
            }
        }
        binding.supplierRadio.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                dealerType = 2
                this.dealerCategory?.let {
                    dealerTypesSpinner(it);
                }
            }

        }
    }

    private fun showDealerResultsFragment(
        branchISN: String?,
        branchName: String?,
        dealerType: Int,
        workerBranchISN: String
    ) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = currentFocus
        currentFocus?.let {
            inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }

        val dialogFragment = DealerResultsDialogFragment()
        val dealerName = binding.getClient.text.toString().let {
            it.ifEmpty { null }
        }
        val bundle = Bundle()
        bundle.putString("uuid", uuid)
        bundle.putString("branchISN", branchISN)
        bundle.putString("branchName", branchName)
        bundle.putInt("dealerType", dealerType)
        bundle.putString("workerBranchISN", workerBranchISN)
        bundle.putString("dealerName", dealerName)
        bundle.putString("DealerCategory", selectedDealerCategory)
        bundle.putBoolean("excludeZeroBalance", binding.balanceAvailableCheckBox.isChecked)
        dialogFragment.arguments = bundle
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(
            R.id.container_view,
            dialogFragment
        ) // Replace with your container view ID
        fragmentTransaction.addToBackStack(null) // Add to backstack to handle dismiss
        fragmentTransaction.commit()
    }

    override fun onResume() {
        super.onResume()
        if (App.customer.dealerName == null) {
            binding.getClient.setText("")
        } else {
            binding.searchResult.text = ""
            showDealerResultsFragment(
                selectedBranch?.branchISN.toString(),
                selectedBranch?.branchName,
                dealerType,
                App.currentUser.workerBranchISN.toString(),
            )
        }
    }
}
