
package com.dataflowstores.dataflow.ui.cashing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ViewModels.InvoiceViewModel;
import com.dataflowstores.dataflow.ViewModels.ProductVM;
import com.dataflowstores.dataflow.databinding.BottomSheetBinding;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.adapters.AgentAdapter;
import com.dataflowstores.dataflow.ui.adapters.CustomerAdapter;
import com.dataflowstores.dataflow.ui.listeners.MyDialogCloseListener;
import com.dataflowstores.dataflow.utils.SingleLiveEvent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragmentCashing extends BottomSheetDialogFragment implements ProductAdapterCashing.ClickListener {
    BottomSheetBinding binding;
    InvoiceViewModel invoiceVM;
    ProductVM productVM;
    String uuid = "";
    boolean isSerial = false;
    int moveType;

    public BottomSheetFragmentCashing(int moveType) {
        this.moveType = moveType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            startActivity(new Intent(requireActivity(), SplashScreen.class));
            requireActivity().finishAffinity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet, container, false);
        firstInvoice();
        getProduct();
        return binding.getRoot();
    }

    @SuppressLint("HardwareIds")
    public void firstInvoice() {
        invoiceVM = new ViewModelProvider(getActivity()).get(InvoiceViewModel.class);
        invoiceVM.salesManLiveData = new MutableLiveData<>();
        invoiceVM.customerLiveData = new MutableLiveData<>();
        uuid = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        invoiceVM.customerLiveData.observe(getActivity(), customer -> {
            binding.progressBar.setVisibility(View.GONE);

            if (customer.getStatus() == 1) {
                CustomerAdapter customerAdapter = new CustomerAdapter(customer.getData(), this);
                binding.resultRV.setAdapter(customerAdapter);
                binding.resultRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });
        invoiceVM.salesManLiveData.observe(getActivity(), salesMan -> {
            binding.progressBar.setVisibility(View.GONE);
            if (salesMan.getStatus() == 1) {
                AgentAdapter agentAdapter = new AgentAdapter(salesMan.getData(), this);
                binding.resultRV.setAdapter(agentAdapter);
                binding.resultRV.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });
    }

    public void getProduct() {
        productVM = new ViewModelProvider(getActivity()).get(ProductVM.class);
        productVM.toastErrorMutableLiveData.observe(this, s -> Toast.makeText(requireActivity(), s, Toast.LENGTH_LONG).show());
        productVM.productMutableLiveData = new SingleLiveEvent<>();
        productVM.productMutableLiveData.observe(getActivity(), product -> {
            binding.serialDialog.getRoot().setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.GONE);
            if (product.getStatus() == 1) {
                if (isSerial) {
                    App.product = product.getData().get(0);
                    if (!App.product.getxBarCodeSerial().isEmpty())
                        binding.serialDialog.serialNumberInput.setText(App.product.getxBarCodeSerial());
                    Log.e("checkSerialSheet", "1");
                    App.serialNumber = binding.serialDialog.serialNumberInput.getText().toString();
                    isSerial = false;
                    Intent intent = new Intent(requireActivity(), ProductScreenCashing.class);
                    App.editingPos=0;
                    intent.putExtra("moveType", moveType);
                    requireActivity().startActivity(intent);
                    getActivity().finish();
                    dismiss();
                } else {
                    Log.e("checkSerialSheet", "2");
                    ProductAdapterCashing productAdapter = new ProductAdapterCashing(product.getData(), this, getActivity(), this, moveType);
                    binding.resultRV.setAdapter(productAdapter);
                    binding.resultRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        binding.serialDialog.getRoot().setVisibility(View.GONE);
        productVM.productMutableLiveData = new SingleLiveEvent<>();
        if (activity instanceof MyDialogCloseListener)
            ((MyDialogCloseListener) activity).handleDialogClose(dialog);
            productVM.compositeDisposable.clear();
    }

    @Override
    public void serialClicked(int position) {
        binding.serialDialog.getRoot().setVisibility(View.VISIBLE);
        Log.e("checkSerialSheet", "3");
        binding.serialDialog.confirm.setOnClickListener(view -> {
            if (!binding.serialDialog.serialNumberInput.getText().toString().isEmpty())
                productVM.getProduct(App.product.getItemName(), uuid, binding.serialDialog.serialNumberInput.getText().toString(), moveType, null);
            else
                binding.serialDialog.serialNumberInput.setError("مطلوب");
            isSerial = true;
        });
    }

}