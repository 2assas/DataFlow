package com.example.dataflow.ui.invoice;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
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
import com.example.dataflow.pojo.users.CustomerData;
import com.example.dataflow.pojo.users.SalesManData;
import com.example.dataflow.ui.AddProducts;
import com.example.dataflow.ui.SplashScreen;
import com.example.dataflow.ui.fragments.BottomSheetFragment;
import com.example.dataflow.ui.listeners.MyDialogCloseListener;

import java.util.ArrayList;

public class FirstInvoice extends AppCompatActivity implements MyDialogCloseListener {
    InvoiceFirstBinding binding;
    InvoiceViewModel invoiceVM;
    String uuid;
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        }else{
        binding = DataBindingUtil.setContentView(this,R.layout.invoice_first);
        uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        invoiceVM =new  ViewModelProvider(this).get(InvoiceViewModel.class);
        App.customer= new CustomerData();
        App.agent = new SalesManData();
        App.selectedProducts = new ArrayList<>();
        binding.salesNameCheck.setChecked(true);
        binding.clientNameCheck.setChecked(true);
        checkboxes();
        searchButtons();
        confirmButton();
        }
    }

    public void checkboxes(){
        binding.salesNameCheck.setOnCheckedChangeListener((compoundButton, b) -> {
            if(!compoundButton.isChecked()){
                binding.searchAgent.setClickable(false);
                binding.searchAgent.setAlpha((float) 0.5);
                binding.getAgent.setEnabled(false);
                binding.getAgent.setText("");
                App.agent=new SalesManData();
            }else{
                binding.searchAgent.setClickable(true);
                binding.searchAgent.setAlpha(1);
                binding.getAgent.setEnabled(true);
            }
        });
        binding.clientNameCheck.setOnCheckedChangeListener((compoundButton, b) -> {
            if( !compoundButton.isChecked()){
                binding.searchClient.setClickable(false);
                binding.searchClient.setAlpha((float).5);
                binding.getClient.setEnabled(false);
                binding.getClient.setText("");
                App.customer=new CustomerData();
            }else{
                binding.searchClient.setClickable(true);
                binding.searchClient.setAlpha(1);
                binding.getClient.setEnabled(true);
            }
        });
        binding.back.setOnClickListener(view -> finish());
    }

    public void searchButtons(){
        binding.searchAgent.setOnClickListener(view -> {
            if(App.isNetworkAvailable(this))
                invoiceVM.getSalesMan(uuid, binding.getAgent.getText().toString(), App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN());
            else{
                App.noConnectionDialog(this);
            }
            BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        });
        binding.searchClient.setOnClickListener(view -> {
            if(App.isNetworkAvailable(this))
                invoiceVM.getCustomer(uuid, binding.getClient.getText().toString(), App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN());
            else{
                App.noConnectionDialog(this);
            }
            BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        });
    }

    public void confirmButton(){
        binding.confirm.setOnClickListener(view -> {
            if( binding.salesNameCheck.isChecked()&&App.agent.getDealerName()==null){
                new AlertDialog.Builder(this).
                        setTitle("إختر مندوب")
                        .setMessage("من فضلك اختر إسم المندوب او اختار بدون مندوب")
                        .setPositiveButton("حسنا", (dialogInterface, i) -> dialogInterface.dismiss())
                        .setNegativeButton("بدون مندوب", (dialogInterface, i) -> binding.salesNameCheck.setChecked(false)).show();
            }else if(  binding.clientNameCheck.isChecked()&&App.customer.getDealerName()==null){
                new AlertDialog.Builder(this).
                        setTitle("إختر عميل")
                        .setMessage("من فضلك اختر إسم العميل او اختار بدون عميل")
                        .setPositiveButton("حسنا", (dialogInterface, i) -> dialogInterface.dismiss())
                        .setNegativeButton("بدون عميل", (dialogInterface, i) -> binding.clientNameCheck.setChecked(false)).show();
            }else {
                Log.e("checkValidation", "skip");
                startActivity(new Intent(this, AddProducts.class));
            }});
    }
    @Override
    public void handleDialogClose(DialogInterface dialog) {
        if(App.customer.getDealerName()!=null){
            binding.getClient.setText(App.customer.getDealerName());
        }
        if(App.agent.getDealerName()!=null){
            binding.getAgent.setText(App.agent.getDealerName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(App.customer.getDealerName()==null){
            binding.getClient.setText("");
        }
    }
}
