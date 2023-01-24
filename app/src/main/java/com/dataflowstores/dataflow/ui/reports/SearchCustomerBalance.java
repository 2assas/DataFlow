package com.dataflowstores.dataflow.ui.reports;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ViewModels.InvoiceViewModel;
import com.dataflowstores.dataflow.databinding.SearchCustomerBalanceBinding;
import com.dataflowstores.dataflow.pojo.users.CustomerData;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.fragments.BottomSheetFragment;
import com.dataflowstores.dataflow.ui.listeners.MyDialogCloseListener;

public class SearchCustomerBalance extends AppCompatActivity implements MyDialogCloseListener {
    SearchCustomerBalanceBinding binding;
    InvoiceViewModel invoiceVM;
    String uuid;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.search_customer_balance);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        }else {
            uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            invoiceVM = new ViewModelProvider(this).get(InvoiceViewModel.class);
            binding.back.setOnClickListener(view -> finish());
            App.customer = new CustomerData();
            invoiceVM.toastErrorMutableLiveData.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());
            searchButtons();
            observer();
        }}

    private void observer() {
        invoiceVM.customerBalanceLiveData.observe(this, customerBalance -> {
            binding.progress.setVisibility(View.GONE);
            binding.searchResult.setText(customerBalance.getMessage());
        });
    }

    public void searchButtons() {
        binding.searchClient.setOnClickListener(view -> {
            if (App.isNetworkAvailable(this))
                invoiceVM.getCustomer(uuid, binding.getClient.getText().toString(), App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN());
            else {
                App.noConnectionDialog(this);
            }
            BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        if (App.customer.getDealerName() != null) {
            binding.getClient.setText(App.customer.getDealerName());
            binding.searchResult.setText("");
            binding.progress.setVisibility(View.VISIBLE);
            invoiceVM.getCustomerBalance(uuid, String.valueOf(App.customer.getDealer_ISN()), String.valueOf(App.customer.getBranchISN()), String.valueOf(App.customer.getDealerType()), String.valueOf(App.customer.getDealerName()));
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
