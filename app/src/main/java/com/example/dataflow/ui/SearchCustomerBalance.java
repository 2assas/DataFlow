package com.example.dataflow.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.dataflow.App;
import com.example.dataflow.R;
import com.example.dataflow.ViewModels.InvoiceViewModel;
import com.example.dataflow.databinding.InvoiceFirstBinding;
import com.example.dataflow.databinding.SearchCustomerBalanceBinding;
import com.example.dataflow.pojo.users.CustomerData;
import com.example.dataflow.pojo.users.SalesManData;
import com.example.dataflow.ui.AddProducts;
import com.example.dataflow.ui.fragments.BottomSheetFragment;
import com.example.dataflow.ui.listeners.MyDialogCloseListener;

import java.util.ArrayList;

public class SearchCustomerBalance extends AppCompatActivity implements MyDialogCloseListener {
    SearchCustomerBalanceBinding binding;
    InvoiceViewModel invoiceVM;
    String uuid;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.search_customer_balance);
        uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        invoiceVM = new ViewModelProvider(this).get(InvoiceViewModel.class);
        binding.back.setOnClickListener(view -> finish());
        App.customer=new CustomerData();
        searchButtons();
        observer();
    }

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
