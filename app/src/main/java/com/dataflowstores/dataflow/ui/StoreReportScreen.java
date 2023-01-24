package com.dataflowstores.dataflow.ui;

import static android.telephony.MbmsDownloadSession.RESULT_CANCELLED;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dataflowstores.dataflow.ui.invoice.PrintScreen;
import com.dataflowstores.dataflow.ui.reports.ReportViewModel;
import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ViewModels.ProductVM;
import com.dataflowstores.dataflow.pojo.product.ProductData;
import com.dataflowstores.dataflow.pojo.users.CustomerData;
import com.dataflowstores.dataflow.ui.fragments.BottomSheetFragment;
import com.dataflowstores.dataflow.ui.listeners.MyDialogCloseListener;
import com.dataflowstores.dataflow.databinding.StoreReportBinding;
import com.dataflowstores.dataflow.pojo.settings.StoresData;
import com.dataflowstores.dataflow.ui.adapters.StoreReportAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class StoreReportScreen extends AppCompatActivity implements MyDialogCloseListener {
    StoreReportBinding binding;
    StoresData storesData = new StoresData();
    StoresData selectedStoresData = null;

    ReportViewModel reportViewModel;
    String uuid;
    ProductVM productVM;
    ProductData selectedProduct = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.store_report);
            storeSpinner();
            reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
            productVM = new ViewModelProvider(this).get(ProductVM.class);
            uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            productVM.toastErrorMutableLiveData.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());

            searchProducts();
            barCodeScan();
            handleCheckboxes();
            binding.showButton.setOnClickListener(v -> {
                if (selectedProduct != null && !Objects.equals(selectedProduct.getItemName(), binding.searchProducts.getQuery().toString())) {
                    selectedProduct = null;
                }
                binding.progress.setVisibility(View.VISIBLE);
                if (selectedStoresData != null && selectedProduct != null)
                    reportViewModel.getStoreReport(uuid, selectedStoresData.getBranchISN(), selectedStoresData.getStore_ISN(), selectedProduct.getBranchISN(), selectedProduct.getItemISN(), null);
                else if (selectedStoresData != null && binding.searchProducts.getQuery().length() > 0)
                    reportViewModel.getStoreReport(uuid, selectedStoresData.getBranchISN(), selectedStoresData.getStore_ISN(), null, null, binding.searchProducts.getQuery().toString());
                else if (selectedStoresData != null)
                    reportViewModel.getStoreReport(uuid, selectedStoresData.getBranchISN(), selectedStoresData.getStore_ISN(), null, null, null);
                else if (selectedProduct != null)
                    reportViewModel.getStoreReport(uuid, null, null, selectedProduct.getBranchISN(), selectedProduct.getItemISN(), null);
                else if (binding.searchProducts.getQuery().length() > 0) {
                    reportViewModel.getStoreReport(uuid, null, null, null, null, binding.searchProducts.getQuery().toString());
                } else if (App.currentUser.getPermission() == 1) {
                    reportViewModel.getStoreReport(uuid, null, null, null, null, null);
                } else {
                    Toast.makeText(this, "من فضلك قم بإختيار مخزن أو صنف أولا", Toast.LENGTH_LONG).show();
                    binding.progress.setVisibility(View.GONE);
                }
                observeReport();
            });
            reportViewModel.toastErrorMutableLiveData.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());
        }

    }

    private void handleCheckboxes() {
        if (App.currentUser.getPermission() == 0) {
            binding.storeCheckBox.setChecked(true);
            binding.storeCheckBox.setEnabled(false);
            binding.storesSpinner.setEnabled(false);
        } else {
            binding.storeCheckBox.setOnCheckedChangeListener((compoundButton, checked) -> {
                if (checked) {
                    selectedStoresData = storesData;
                    binding.storesSpinner.setEnabled(true);
                } else {
                    selectedStoresData = null;
                    binding.storesSpinner.setEnabled(false);
                }
            });
        }
        binding.itemCheckBox.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                selectedProduct = App.product;
                binding.searchProducts.setEnabled(true);
                binding.scan.setEnabled(true);
            } else {
                selectedProduct = null;
                binding.searchProducts.setEnabled(false);
                binding.scan.setEnabled(false);
                binding.searchButton.setVisibility(View.GONE);
            }
        });
    }

    private void searchProducts() {
        App.customer = new CustomerData();
        binding.searchProducts.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (binding.itemCheckBox.isChecked()) {
                    if (App.isNetworkAvailable(StoreReportScreen.this))
                        productVM.getProduct(s, uuid, null, 1);
                    else {
                        App.noConnectionDialog(StoreReportScreen.this);
                    }
                    BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                    bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                } else {
                    Toast.makeText(StoreReportScreen.this, "قم بتفعيل صنف محدد أولا", Toast.LENGTH_SHORT).show();
                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String s) {
                binding.searchButton.setVisibility(View.VISIBLE);
                binding.searchButton.setOnClickListener(view -> {
                    if (binding.itemCheckBox.isChecked()) {
                        if (App.isNetworkAvailable(StoreReportScreen.this))
                            productVM.getProduct(s, uuid, null, 1);
                        else {
                            App.noConnectionDialog(StoreReportScreen.this);
                        }
                        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                        binding.searchButton.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(StoreReportScreen.this, "قم بتفعيل صنف محدد أولا", Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
        });
    }

    public void barCodeScan() {
        binding.scan.setOnClickListener(view -> {
            Intent intent = new Intent(this, ScanBarCode.class);
            startActivityForResult(intent, 0);
        });
    }


    private void storeSpinner() {
        ArrayList<String> stores = new ArrayList<>();
        for (int i = 0; i < App.stores.getData().size(); i++) {
            stores.add(App.stores.getData().get(i).getStoreName());
        }
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stores);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.storesSpinner.setAdapter(aa);

        binding.storesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                storesData = App.stores.getData().get(i);
                selectedStoresData = storesData;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                storesData = App.stores.getData().get(0);
                selectedStoresData = storesData;
            }
        });
        for (int i = 0; i < App.stores.getData().size(); i++) {
            if (App.currentUser.getCashierStoreISN() == App.stores.getData().get(i).getStore_ISN()
                    && App.currentUser.getCashierStoreBranchISN() == App.stores.getData().get(i).getBranchISN()) {
                binding.storesSpinner.setSelection(i);
            }
        }

    }

    private void observeReport() {
        reportViewModel.storeReportModelMutableLiveData.observe(this, storeReportModel -> {
            binding.progress.setVisibility(View.GONE);
            binding.headerContainer.setVisibility(View.VISIBLE);
            binding.reportRecycler.setVisibility(View.VISIBLE);
            binding.printReportButton.setVisibility(View.VISIBLE);
            Log.e("reportLong", storeReportModel.getData().size() + "");
            StoreReportAdapter storeReportAdapter = new StoreReportAdapter(storeReportModel.getData(), this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            binding.reportRecycler.setLayoutManager(layoutManager);
            binding.reportRecycler.setAdapter(storeReportAdapter);
        });
    }

    @SuppressLint("SetTextI18n")
    public void printReport(View view) {
        binding.printReportButton.setVisibility(View.GONE);
        binding.printReportName.setVisibility(View.VISIBLE);
        binding.printReportName.setText("تقرير " + storesData.getStoreName());
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            takeScreenShot();
            startActivity(new Intent(StoreReportScreen.this, PrintScreen.class));
            finish();
        }, 1000);
    }

    private void takeScreenShot() {
        View u = binding.reportScroll;
        int totalHeight = binding.reportScroll.getChildAt(0).getHeight();
        int totalWidth = binding.reportScroll.getChildAt(0).getWidth();
        Bitmap b = getBitmapFromView(u, totalHeight, totalWidth);
        App.printBitmap = b;
    }

    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {
        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        binding.searchProducts.setQuery(App.product.getItemName(), false);
        selectedProduct = App.product;
        binding.searchButton.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                if (App.isNetworkAvailable(StoreReportScreen.this))
                    productVM.getProduct(contents, uuid, null, 1);
                else {
                    App.noConnectionDialog(StoreReportScreen.this);
                }
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                binding.searchProducts.setQuery(App.product.getItemName(), false);
            }
            if (resultCode == RESULT_CANCELLED) {
                //handle cancel
            }
        }
    }
}
