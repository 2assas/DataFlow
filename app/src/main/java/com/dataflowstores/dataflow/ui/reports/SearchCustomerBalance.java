package com.dataflowstores.dataflow.ui.reports;

import static com.dataflowstores.dataflow.App.theme;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ViewModels.InvoiceViewModel;
import com.dataflowstores.dataflow.databinding.SearchCustomerBalanceBinding;
import com.dataflowstores.dataflow.pojo.report.Branches;
import com.dataflowstores.dataflow.pojo.users.CustomerData;
import com.dataflowstores.dataflow.pojo.workStation.BranchData;
import com.dataflowstores.dataflow.ui.BaseActivity;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.fragments.BottomSheetFragment;
import com.dataflowstores.dataflow.ui.listeners.MyDialogCloseListener;

import java.util.ArrayList;

public class SearchCustomerBalance extends BaseActivity implements MyDialogCloseListener {
    SearchCustomerBalanceBinding binding;
    InvoiceViewModel invoiceVM;
    String uuid;
    Branches branches;
    BranchData selectedBranch;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.search_customer_balance);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else {
            uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            invoiceVM = new ViewModelProvider(this).get(InvoiceViewModel.class);
            initViews();
            searchButtons();
            observer();
        }
    }

    private void initViews() {
        invoiceVM.getBranches(uuid);
        if (App.currentUser.getMobileDealersBalanceEnquiry() == 0) {
            binding.customerRadio.setEnabled(false);
            binding.customerRadio.setAlpha(.5F);
            binding.supplierRadio.setChecked(true);
        } else {
            binding.customerRadio.setChecked(true);
        }
        if (App.currentUser.getMobileSuppliersBalanceEnquiry() == 0) {
            binding.supplierRadio.setEnabled(false);
            binding.supplierRadio.setAlpha(.5F);
        }

        binding.back.setOnClickListener(view -> finish());
        App.customer = new CustomerData();

        invoiceVM.toastErrorMutableLiveData.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());
    }

    private void observer() {
        invoiceVM.customerBalanceLiveData.observe(this, customerBalance -> {
            binding.progress.setVisibility(View.GONE);
            binding.searchResult.setText(customerBalance.getMessage());
        });
        invoiceVM.branchesMutableLiveData.observe(this, this::branchSpinner);

    }

    public void searchButtons() {
        binding.getClient.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.searchClient.performClick();
                return true; // Indicates that the action has been handled
            }
            return false;
        });

        binding.searchClient.setOnClickListener(view -> {
            if (App.isNetworkAvailable(this)) if (binding.customerRadio.isChecked())
                invoiceVM.getCustomer(uuid, binding.getClient.getText().toString(), App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN());
            else
                invoiceVM.getSupplier(uuid, binding.getClient.getText().toString(), App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN());
            else {
                App.noConnectionDialog(this);
            }
            BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        });
    }

    void branchSpinner(Branches branches) {
        this.branches = branches;
        ArrayList<String> branchList = new ArrayList<>();
        int position = 0;
        for (int i = 0; i < branches.getBranchData().size(); i++) {
            branchList.add(branches.getBranchData().get(i).getBranchName());
            if (App.currentUser.getBranchISN() == branches.getBranchData().get(i).getBranchISN()) {
                position = i;
            }
        }
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, branchList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.branchSpinner.setAdapter(aa);
        selectedBranch = branches.getBranchData().get(0);
        binding.branchSpinner.setSelection(position);
        int finalPosition = position;
        binding.branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBranch = branches.getBranchData().get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedBranch = branches.getBranchData().get(finalPosition);
            }
        });
    }


    @Override
    public void handleDialogClose(DialogInterface dialog) {
        if (App.customer.getDealerName() != null) {
            binding.getClient.setText(App.customer.getDealerName());
            binding.searchResult.setText("");
            binding.progress.setVisibility(View.VISIBLE);
            invoiceVM.getCustomerBalance(uuid, String.valueOf(App.customer.getDealer_ISN()), String.valueOf(App.customer.getBranchISN()), String.valueOf(App.customer.getDealerType()), binding.branchCheckBox.isChecked() ? String.valueOf(selectedBranch.getBranchISN()) : null);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.customer.getDealerName() == null) {
            binding.getClient.setText("");
        }else{
            binding.searchResult.setText("");
            binding.progress.setVisibility(View.VISIBLE);
            invoiceVM.getCustomerBalance(uuid, String.valueOf(App.customer.getDealer_ISN()), String.valueOf(App.customer.getBranchISN()), String.valueOf(App.customer.getDealerType()), String.valueOf(App.customer.getDealerName()));
        }
    }


}
