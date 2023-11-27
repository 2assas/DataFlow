package com.dataflowstores.dataflow.ui.searchItemPrice;

import static android.telephony.MbmsDownloadSession.RESULT_CANCELLED;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ViewModels.ProductVM;
import com.dataflowstores.dataflow.databinding.SearchItemPriceBinding;
import com.dataflowstores.dataflow.pojo.product.ProductData;
import com.dataflowstores.dataflow.pojo.searchItemPrice.ItemPriceItem;
import com.dataflowstores.dataflow.pojo.users.CustomerData;
import com.dataflowstores.dataflow.ui.ScanBarCode;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.fragments.BottomSheetFragment;
import com.dataflowstores.dataflow.ui.listeners.MyDialogCloseListener;

public class SearchItemPrice extends AppCompatActivity implements MyDialogCloseListener, ItemPriceAdapter.ItemClickListener {
    SearchItemPriceBinding binding;
    ProductVM productVM;
    String uuid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.search_item_price);
            productVM = new ViewModelProvider(this).get(ProductVM.class);
            uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            setupViews();
            barCodeScan();
        }
    }

    private void setupViews() {
        App.customer = new CustomerData();
        if (App.currentUser.getMobileItemPricesEnquiry() == 0) {
            binding.sellingPrice.setEnabled(false);
            binding.sellingPrice.setAlpha(.5F);
            binding.buyingPrice.setChecked(true);
        } else {
            binding.sellingPrice.setChecked(true);
        }

        if (App.currentUser.getMobileItemPricesEnquiryBuy() == 0) {
            binding.buyingPrice.setEnabled(false);
            binding.buyingPrice.setAlpha(.5F);
        }

        productVM.toastErrorMutableLiveData.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());
        binding.searchProducts.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (App.isNetworkAvailable(SearchItemPrice.this))
                    productVM.getProduct(s, uuid, null, 1);
                else {
                    App.noConnectionDialog(SearchItemPrice.this);
                }
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                binding.invoice.setVisibility(View.VISIBLE);
                binding.invoice.setText("بحث عن صنف");
                binding.invoice.setOnClickListener(view -> {
                    if (App.isNetworkAvailable(SearchItemPrice.this))
                        productVM.getProduct(s, uuid, null, 1);
                    else {
                        App.noConnectionDialog(SearchItemPrice.this);
                    }
                    BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                    bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                    binding.invoice.setVisibility(View.GONE);
                });
                return false;
            }
        });
    }

    public void getItemPrice(ProductData productData) {
        productVM.getItemPrice(uuid, productData.getBranchISN(), productData.getItemISN(), binding.sellingPrice.isChecked() ? 2 : 1);
        binding.itemName.setText(productData.getItemName());
        binding.itemNameCon.setVisibility(View.VISIBLE);
        binding.progress.setVisibility(View.VISIBLE);
        productVM.itemPriceResponseMutableLiveData.observe(this, itemPriceResponse -> {
            binding.progress.setVisibility(View.GONE);
            if (itemPriceResponse.getData() != null) {
                ItemPriceAdapter adapter = new ItemPriceAdapter(itemPriceResponse.getData(), this, this);
                binding.pricesRecycler.setLayoutManager(new LinearLayoutManager(this));
                binding.pricesRecycler.setAdapter(adapter);
            }
        });
    }

    public void barCodeScan() {
        binding.scan.setOnClickListener(view -> {
            Intent intent = new Intent(this, ScanBarCode.class);
            startActivityForResult(intent, 0);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                if (App.isNetworkAvailable(SearchItemPrice.this))
                    productVM.getProduct(contents, uuid, null, 1);
                else {
                    App.noConnectionDialog(SearchItemPrice.this);
                }
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                binding.invoice.setVisibility(View.GONE);
            }
            if (resultCode == RESULT_CANCELLED) {
                //handle cancel
            }
        }
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        if (App.product.getItemName() != null) {
            getItemPrice(App.product);
            binding.searchProducts.setQuery(App.product.getItemName(), false);
        }
        binding.invoice.setVisibility(View.GONE);
    }

    @Override
    public void itemClicked(ItemPriceItem itemPriceItem) {

    }
}
