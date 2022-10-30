package com.dataflowstores.dataflow.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ViewModels.ProductVM;
import com.dataflowstores.dataflow.databinding.AddProductsBinding;
import com.dataflowstores.dataflow.ui.adapters.SelectedProductsAdapter;
import com.dataflowstores.dataflow.ui.fragments.BottomSheetFragment;
import com.dataflowstores.dataflow.utils.SwipeHelper;

import java.util.ArrayList;
import java.util.List;

import static android.telephony.MbmsDownloadSession.RESULT_CANCELLED;

public class AddProducts extends AppCompatActivity {
    AddProductsBinding binding;
    ProductVM productVM;
    String uuid;
    SelectedProductsAdapter selectedProductsAdapter;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.add_products);
            productVM = new ViewModelProvider(this).get(ProductVM.class);
            uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            if (App.resales == 1)
                binding.invoice.setText("متابعة مرتجع المبيعات");
            else
                binding.invoice.setText("متابعة فاتورة المبيعات");
            binding.searchProducts.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    if (App.isNetworkAvailable(AddProducts.this))
                        productVM.getProduct(s, uuid, null, 1);
                    else {
                        App.noConnectionDialog(AddProducts.this);
                    }
                    BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                    bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                    if (App.resales == 1)
                        binding.invoice.setText("متابعة مرتجع المبيعات");
                    else
                        binding.invoice.setText("متابعة فاتورة المبيعات");

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    binding.invoice.setText("بحث عن صنف");
                    binding.invoice.setOnClickListener(view -> {
                        if (App.isNetworkAvailable(AddProducts.this))
                            productVM.getProduct(s, uuid, null, 1);
                        else {
                            App.noConnectionDialog(AddProducts.this);
                        }
                        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                        if (App.resales == 1)
                            binding.invoice.setText("متابعة مرتجع المبيعات");
                        else
                            binding.invoice.setText("متابعة فاتورة المبيعات");

                    });
                    return false;
                }
            });
            if (App.selectedProducts.size() > 0) {
                if (App.priceType != App.selectedProducts.get(0).getSelectedPriceType()) {
                    App.selectedProducts = new ArrayList<>();
                    return;
                }
                binding.productsRecycler.setLayoutManager(new LinearLayoutManager(this));
                selectedProductsAdapter = new SelectedProductsAdapter(App.selectedProducts, this);
                selectedProductsAdapter.notifyDataSetChanged();
                binding.productsRecycler.setAdapter(selectedProductsAdapter);
                binding.invoice.setOnClickListener(view -> {
                    startActivity(new Intent(this, Checkout.class));
                    finish();
                });
            }
            recyclerSwipe();
            barCodeScan();
        }
    }



    public void barCodeScan() {
        binding.scan.setOnClickListener(view -> {
            Intent intent = new Intent(this, ScanBarCode.class);
            startActivityForResult(intent, 0);
        });
    }

    @Override
    public void onBackPressed() {
        if (App.specialDiscount == 1) {
            new AlertDialog.Builder(this).setTitle("تحذير")
                    .setMessage("لقد تم إضافة منتجات بأسعار خاصة، فاذا تم تغيير العميل سيتم حذف المنتجات المضافة حاليا من الفاتورة")
                    .setPositiveButton("إلغاء", (dialogInterface, i) -> dialogInterface.dismiss())
                    .setNegativeButton("متابعة", ((dialogInterface, i) -> {
                        super.onBackPressed();
                    })).show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                if (App.isNetworkAvailable(AddProducts.this))
                    productVM.getProduct(contents, uuid, null, 1);
                else {
                    App.noConnectionDialog(AddProducts.this);
                }
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                if (App.resales == 1)
                    binding.invoice.setText("متابعة مرتجع المبيعات");
                else
                    binding.invoice.setText("متابعة فاتورة المبيعات");

            }
            if (resultCode == RESULT_CANCELLED) {
                //handle cancel
            }
        }
    }

    public SwipeHelper recyclerSwipe() {
        return new SwipeHelper(AddProducts.this, binding.productsRecycler, 250) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List buffer) {
                buffer.add(new MyButton(AddProducts.this, R.drawable.delete, Color.RED,
                        pos -> {
                            if (selectedProductsAdapter.getItemCount() == 1) {
                                App.selectedProducts = new ArrayList<>();
                            }
                            selectedProductsAdapter.callDeleteFunction(pos);
                        }
                ));
                buffer.add(new MyButton(AddProducts.this, R.drawable.ic_baseline_edit_24, Color.GRAY,
                        pos -> {
                            selectedProductsAdapter.callEditFunction(pos);
                        }
                ));
            }
        };
    }
}
