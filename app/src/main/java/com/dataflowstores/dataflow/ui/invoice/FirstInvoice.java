package com.dataflowstores.dataflow.ui.invoice;

import static com.dataflowstores.dataflow.App.theme;
import static com.dataflowstores.dataflow.pojo.invoice.InvoiceType.Purchase;
import static com.dataflowstores.dataflow.pojo.invoice.InvoiceType.ReturnPurchased;
import static com.dataflowstores.dataflow.pojo.invoice.InvoiceType.ReturnSales;
import static com.dataflowstores.dataflow.pojo.invoice.InvoiceType.Sales;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ViewModels.InvoiceViewModel;
import com.dataflowstores.dataflow.databinding.InvoiceFirstBinding;
import com.dataflowstores.dataflow.pojo.users.CustomerData;
import com.dataflowstores.dataflow.pojo.users.SalesManData;
import com.dataflowstores.dataflow.ui.AddProducts;
import com.dataflowstores.dataflow.ui.BaseActivity;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.fragments.BottomSheetFragment;
import com.dataflowstores.dataflow.ui.listeners.MyDialogCloseListener;

import java.util.ArrayList;
import java.util.Objects;

public class FirstInvoice extends BaseActivity implements MyDialogCloseListener {
    InvoiceFirstBinding binding;
    InvoiceViewModel invoiceVM;
    String uuid;
    int customerPriceType = 0;
    boolean resales = false;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.invoice_first);
            uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            invoiceVM = new ViewModelProvider(this).get(InvoiceViewModel.class);
            App.customer = new CustomerData();
            App.customerBalance = "";
            App.agent = new SalesManData();
            App.selectedProducts = new ArrayList<>();
            binding.salesNameCheck.setChecked(true);
            binding.clientNameCheck.setChecked(true);
            resales = getIntent().getBooleanExtra("resales", false);
            invoiceVM.toastErrorMutableLiveData.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());
            checkboxes();
            searchButtons();
            confirmButton();
            getPriceType();
        }
    }

    private void getPriceType() {
        invoiceVM.getPriceType(uuid);
        invoiceVM.priceTypeMutableLiveData.observe(this, priceTypeData -> {
            App.priceType = priceTypeData;
            binding.progress.setVisibility(View.GONE);
            binding.priceTypeSpinner.setVisibility(View.VISIBLE);
            handlePriceTypeSpinner();
            App.priceType = App.allPriceType.get(customerPriceType);
            if (App.selectedProducts.size() > 0) {
                for (int i = 0; i < App.allPriceType.size(); i++) {
                    if (App.allPriceType.get(i) == App.selectedProducts.get(0).getSelectedPriceType()) {
                        if (App.currentUser.getMobileChangeSellPrice() == 1)
                            binding.priceTypeSpinner.setSelection(i);
                        customerPriceType = i;
                        App.priceType = App.allPriceType.get(i);
                        Log.e("priceType", App.priceType.getPricesTypeName() + "2");
                    }
                }
            } else if (App.currentUser.getMobileChangeSellPrice() == 1) {
                binding.priceTypeSpinner.setSelection(customerPriceType);
                Log.e("priceType", App.priceType.getPricesTypeName() + "22");
            }
        });
    }

    public void checkboxes() {
        binding.salesNameCheck.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!compoundButton.isChecked()) {
                binding.searchAgent.setClickable(false);
                binding.searchAgent.setAlpha((float) 0.5);
                binding.getAgent.setEnabled(false);
                binding.getAgent.setText("");
                App.agent = new SalesManData();
            } else {
                binding.searchAgent.setClickable(true);
                binding.searchAgent.setAlpha(1);
                binding.getAgent.setEnabled(true);
            }
        });
        binding.clientNameCheck.setOnCheckedChangeListener((compoundButton, b) -> {
            if (!compoundButton.isChecked()) {
                binding.searchClient.setClickable(false);
                binding.searchClient.setAlpha((float) .5);
                binding.getClient.setEnabled(false);
                binding.getClient.setText("");
                App.customer = new CustomerData();
                App.customerBalance = "";
            } else {
                binding.searchClient.setClickable(true);
                binding.searchClient.setAlpha(1);
                binding.getClient.setEnabled(true);
            }
            customerPriceType = 0;
            handlePriceTypeSpinner();
            if (App.currentUser.getMobileChangeSellPrice() == 1) {
                binding.priceTypeSpinner.setSelection(customerPriceType);
            }
        });
        binding.back.setOnClickListener(view -> finish());
    }

    public void searchButtons() {
        binding.getAgent.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.searchAgent.performClick();
                return true; // Indicates that the action has been handled
            }
            return false;
        });
        binding.getClient.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.searchClient.performClick();
                return true; // Indicates that the action has been handled
            }
            return false;
        });


        binding.searchAgent.setOnClickListener(view -> {
            if (App.isNetworkAvailable(this)) {
                invoiceVM.getSalesMan(uuid, binding.getAgent.getText().toString(), App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN());
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            } else {
                App.noConnectionDialog(this);
            }
        });
        if (App.invoiceType == Sales || App.invoiceType == ReturnSales) {
            binding.searchClient.setText("إبحث عن العميل");
            binding.clientName.setText("إسم العميل");
            binding.title.setText("إختيار العميل والمندوب");
        } else {
            binding.searchClient.setText("إبحث عن المورد");
            binding.clientName.setText("إسم المورد");
            binding.title.setText("إختيار المورد والمندوب");
        }

        binding.searchClient.setOnClickListener(view -> {
            if (App.isNetworkAvailable(this)) {
                if (App.invoiceType == Sales || App.invoiceType == ReturnSales) {
                    invoiceVM.getCustomer(uuid, binding.getClient.getText().toString(), App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN());
                } else {
                    invoiceVM.getSupplier(uuid, binding.getClient.getText().toString(), App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN());
                }
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

            } else {
                App.noConnectionDialog(this);
            }
        });
    }

    public void confirmButton() {
        binding.confirm.setOnClickListener(view -> {
            if (binding.salesNameCheck.isChecked() && App.agent.getDealerName() == null) {
                new AlertDialog.Builder(this).
                        setTitle("إختر مندوب")
                        .setMessage("من فضلك اختر إسم المندوب او اختار بدون مندوب")
                        .setPositiveButton("حسنا", (dialogInterface, i) -> dialogInterface.dismiss())
                        .setNegativeButton("بدون مندوب", (dialogInterface, i) -> binding.salesNameCheck.setChecked(false)).show();
            } else if (binding.clientNameCheck.isChecked() && App.customer.getDealerName() == null) {
                new AlertDialog.Builder(this).setTitle((App.invoiceType == Sales || App.invoiceType == ReturnSales) ? "إختر عميل" : "إختر مورد").setMessage("من فضلك اختر إسم العميل او اختار بدون " + ((App.invoiceType == Sales || App.invoiceType == ReturnSales) ? "عميل" : "مورد")).setPositiveButton("حسنا", (dialogInterface, i) -> dialogInterface.dismiss()).setNegativeButton((App.invoiceType == Sales || App.invoiceType == ReturnSales) ? "بدون عميل" : "بدون مورد", (dialogInterface, i) -> binding.clientNameCheck.setChecked(false)).show();
            } else {
                Log.e("checkValidation", "skip");
                if (App.specialDiscount == 1) {
                    App.selectedProducts = new ArrayList<>();
                    App.specialDiscount = 0;
                }
                Log.e("priceType", App.priceType.getPricesTypeName() + "3");
                startActivity(new Intent(this, AddProducts.class));
            }
        });
    }

    private void handlePriceTypeSpinner() {
        ArrayList<String> priceType = new ArrayList<>();
        if (App.currentUser.getMobileChangeSellPrice() == 1) {
            for (int i = 0; i < App.allPriceType.size(); i++) {
                priceType.add(App.allPriceType.get(i).getPricesTypeName());
                if ((App.invoiceType == Purchase || App.invoiceType == ReturnPurchased) && App.allPriceType.get(i).getBasicPriceType() == 1) {
                    App.priceType = App.allPriceType.get(i);
                    customerPriceType = i;
                    Log.e("priceType", App.priceType.getPricesTypeName() + " 55");
                } else if (App.customer != null && App.customer.getDealerName() != null && !Objects.equals(App.customer.getDealerSellPriceTypeBranchISN(), "0") && !Objects.equals(App.customer.getDealerSellPriceTypeBranchISN(), "0")) {
                    if (String.valueOf(App.allPriceType.get(i).getPricesType_ISN()).equals(App.customer.getDealerSellPriceTypeISN()) && String.valueOf(App.allPriceType.get(i).getBranchISN()).equals(App.customer.getDealerSellPriceTypeBranchISN())) {
                        App.priceType = App.allPriceType.get(i);
                        Log.e("priceType", App.priceType.getPricesTypeName() + " 7");
                        Log.e("checkCustomerPrice", "here - " + i);
                        customerPriceType = i;
                    }
                } else {
                    if (App.allPriceType.get(i).getBranchISN() == App.currentUser.getCashierSellPriceTypeBranchISN() && App.allPriceType.get(i).getPricesType_ISN() == App.currentUser.getCashierSellPriceTypeISN()) {
                        App.priceType = App.allPriceType.get(i);
                        customerPriceType = i;
                        Log.e("priceType", App.priceType.getPricesTypeName() + " 51");
                    }
                }
            }
        } else if (App.customer != null && App.customer.getDealerName() != null && !Objects.equals(App.customer.getDealerSellPriceTypeBranchISN(), "0") && !Objects.equals(App.customer.getDealerSellPriceTypeBranchISN(), "0") && (App.invoiceType == Sales || App.invoiceType == ReturnSales)) {
            for (int i = 0; i < App.allPriceType.size(); i++) {
                if (App.customer != null && App.customer.getDealerName() != null && !Objects.equals(App.customer.getDealerSellPriceTypeBranchISN(), "0") && !Objects.equals(App.customer.getDealerSellPriceTypeBranchISN(), "0")) {
                    if (String.valueOf(App.allPriceType.get(i).getPricesType_ISN()).equals(App.customer.getDealerSellPriceTypeISN()) && String.valueOf(App.allPriceType.get(i).getBranchISN()).equals(App.customer.getDealerSellPriceTypeBranchISN())) {
                        App.priceType = App.allPriceType.get(i);
                        customerPriceType = i;
                        priceType.add(App.priceType.getPricesTypeName());
                    }
                }
            }
        } else {
            for (int i = 0; i < App.allPriceType.size(); i++) {
                if (App.invoiceType == Sales || App.invoiceType == ReturnSales) {
                    if (App.allPriceType.get(i).getBranchISN() == App.currentUser.getCashierSellPriceTypeBranchISN() && App.allPriceType.get(i).getPricesType_ISN() == App.currentUser.getCashierSellPriceTypeISN()) {
                        App.priceType = App.allPriceType.get(i);
                        customerPriceType = i;
                        priceType.add(App.priceType.getPricesTypeName());
                    }
                } else {
                    if (App.allPriceType.get(i).getBasicPriceType() == 1) {
                        App.priceType = App.allPriceType.get(i);
                        customerPriceType = i;
                        priceType.add(App.priceType.getPricesTypeName());
                    }
                }
            }
        }


        ArrayAdapter bb = new ArrayAdapter(this, android.R.layout.simple_spinner_item, priceType);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.priceTypeSpinner.setAdapter(bb);
        int finalCustomerPriceType = customerPriceType;
        binding.priceTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (App.currentUser.getMobileChangeSellPrice() == 1)
                    App.priceType = App.allPriceType.get(i);
                else {
                    App.priceType = App.allPriceType.get(customerPriceType);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                App.priceType = App.allPriceType.get(finalCustomerPriceType);

                Log.e("checkPriceType", App.priceType.getPricesTypeName() + " 10");
            }
        });
    }


    @Override
    public void handleDialogClose(DialogInterface dialog) {
        if (App.customer.getDealerName() != null) {
            binding.getClient.setText(App.customer.getDealerName());
            handlePriceTypeSpinner();
            if (App.currentUser.getMobileChangeSellPrice() == 1) {
                binding.priceTypeSpinner.setSelection(customerPriceType);
            }
            App.priceType = App.allPriceType.get(customerPriceType);
            Log.e("priceType", App.priceType.getPricesTypeName() + " 4");
        }
        if (App.agent.getDealerName() != null) {
            binding.getAgent.setText(App.agent.getDealerName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.customer.getDealerName() == null) {
            binding.getClient.setText("");
        }

    }
}
