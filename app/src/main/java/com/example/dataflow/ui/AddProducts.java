package com.example.dataflow.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dataflow.App;
import com.example.dataflow.R;
import com.example.dataflow.ViewModels.ProductVM;
import com.example.dataflow.databinding.AddProductsBinding;
import com.example.dataflow.ui.adapters.SelectedProductsAdapter;
import com.example.dataflow.ui.fragments.BottomSheetFragment;
import com.example.dataflow.ui.listeners.MyButtonClickListener;
import com.example.dataflow.utils.SwipeHelper;

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
        binding = DataBindingUtil.setContentView(this, R.layout.add_products);
        productVM = new ViewModelProvider(this).get(ProductVM.class);
        uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        binding.searchProducts.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (App.isNetworkAvailable(AddProducts.this))
                    productVM.getProduct(s, uuid, null);
                else {
                    App.noConnectionDialog(AddProducts.this);
                }
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                binding.invoice.setText("متابعة الفاتورة");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                binding.invoice.setText("بحث عن صنف");
                binding.invoice.setOnClickListener(view -> {
                    if (App.isNetworkAvailable(AddProducts.this))
                        productVM.getProduct(s, uuid, null);
                    else {
                        App.noConnectionDialog(AddProducts.this);
                    }
                    BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                    bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                    binding.invoice.setText("متابعة الفاتورة");
                });
                return false;
            }
        });
        if (App.selectedProducts.size() > 0) {
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
                if (App.isNetworkAvailable(AddProducts.this))
                    productVM.getProduct(contents, uuid, null);
                else {
                    App.noConnectionDialog(AddProducts.this);
                }
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                binding.invoice.setText("متابعة الفاتورة");
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
