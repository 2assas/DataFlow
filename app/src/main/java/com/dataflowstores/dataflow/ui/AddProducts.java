package com.dataflowstores.dataflow.ui;

import static android.telephony.MbmsDownloadSession.RESULT_CANCELLED;
import static com.dataflowstores.dataflow.App.getMoveType;
import static com.dataflowstores.dataflow.App.priceType;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ViewModels.ProductVM;
import com.dataflowstores.dataflow.databinding.AddProductsBinding;
import com.dataflowstores.dataflow.pojo.product.SearchProductResponse;
import com.dataflowstores.dataflow.ui.adapters.SelectedProductsAdapter;
import com.dataflowstores.dataflow.ui.fragments.BottomSheetFragment;
import com.dataflowstores.dataflow.ui.products.ProductDetails;
import com.dataflowstores.dataflow.utils.SwipeHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddProducts extends BaseActivity {
    AddProductsBinding binding;
    ProductVM productVM;
    String uuid;
    SelectedProductsAdapter selectedProductsAdapter;
    private SimpleCursorAdapter adapter;
    private final String[] suggestionsColumns = {"_id", "suggestion"};
    Boolean isSerial = false;
    List<SearchProductResponse> searchProductList = new ArrayList<>();
    String itemCode = "";

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
            Log.e("checkType1", App.invoiceType.name());
            invoiceName();
            productVM.toastErrorMutableLiveData.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());
            App.isEditing = false;
            if (App.selectedProducts.size() > 0) {
                if (App.priceType != App.selectedProducts.get(0).getSelectedPriceType()) {
                    App.selectedProducts = new ArrayList<>();
                    return;
                }
                binding.productsRecycler.setLayoutManager(new LinearLayoutManager(this));
                selectedProductsAdapter = new SelectedProductsAdapter(App.selectedProducts, this);
                selectedProductsAdapter.notifyDataSetChanged();
                binding.productsRecycler.setAdapter(selectedProductsAdapter);
                setOrderSummary(false);
                binding.invoice.setOnClickListener(view -> {
                    setOrderSummary(true);
                });
            }
            recyclerSwipe();
            barCodeScan();
            searchProducts();
            observeSearching();
            observeSearchProduct();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setOrderSummary(boolean showDialog) {
        int count = 0;
        double quantity = 0;
        double total = 0;
        binding.orderSummaryContainer.setVisibility(View.VISIBLE);
        if (App.selectedProducts.size() > 0) {
            for (int i = 0; i < App.selectedProducts.size(); i++) {
                quantity += App.selectedProducts.get(i).getQuantity();
                total += App.selectedProducts.get(i).getNetPrice();
            }
            count = App.selectedProducts.size();
        }
        if (showDialog) {
            new androidx.appcompat.app.AlertDialog.Builder(this).setMessage("لديك عدد " + "(" + count + ")" + " سطور بإجمالى كمية " + "(" + String.format(Locale.ENGLISH, "%.2f", quantity) + ")" +
                    " بإجمالى مبلغ " + "(" + String.format(Locale.ENGLISH, "%.2f", total) + ")" +
                    " هل أنت متأكد؟").setPositiveButton("تأكيد", ((dialogInterface, i) -> {
                startActivity(new Intent(this, Checkout.class));
                finish();
                dialogInterface.dismiss();
            })).setNegativeButton("إلغاء", ((dialogInterface, i) -> {
                dialogInterface.dismiss();
            })).setCancelable(false).show();
        } else {
            binding.cartLinesCount.setText("السطور: " + count);
            binding.cartLinesQuantity.setText("الكمية: " + String.format(Locale.ENGLISH, "%.2f", quantity));
            binding.cartLinesTotal.setText("الاجمالي: " + String.format(Locale.ENGLISH, "%.2f", total));
        }
    }

    private void searchProducts() {

        binding.searchProducts.setOnClickListener(view -> {
            binding.searchProducts.onActionViewExpanded(); // Expand the SearchView
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.searchProducts, InputMethodManager.SHOW_IMPLICIT);
        });
        // Create an ArrayAdapter for suggestions
        binding.searchProducts.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (App.isNetworkAvailable(AddProducts.this)) {
                    productVM.getProduct(s, uuid, null, getMoveType(), null);
                } else {
                    App.noConnectionDialog(AddProducts.this);
                }
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                Log.e("checkType2", App.invoiceType.name());
                invoiceName();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                binding.invoice.setText("بحث عن صنف");
                productVM.setSearchQuery(s);
                binding.invoice.setOnClickListener(view -> {
                    if (App.isNetworkAvailable(AddProducts.this)) {
                        productVM.getProduct(s, uuid, null, getMoveType(), null);
                        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                    } else {
                        App.noConnectionDialog(AddProducts.this);
                    }
                    Log.e("checkType3", App.invoiceType.name());
                    invoiceName();

                });
                return false;
            }
        });

        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_dropdown_item_1line, null, new String[]{"suggestion"}, new int[]{android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        binding.searchProducts.setSuggestionsAdapter(adapter);
        binding.searchProducts.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = adapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    int suggestionIndex = cursor.getColumnIndex("suggestion");
                    if (suggestionIndex != -1) {
                        String suggestion = cursor.getString(suggestionIndex);
                        itemCode = searchProductList.get(position).getItemCode();
                        productVM.getProduct(suggestion, uuid, null, getMoveType(), itemCode);
                        binding.progress.setVisibility(View.VISIBLE);

                    }
                }
                return true;
            }
        });

    }

    private void observeSearching() {
        productVM.getSearchResults().observe(this, searchProductResponses -> {
            if (searchProductResponses != null) {
                searchProductList = searchProductResponses;
                MatrixCursor cursor = new MatrixCursor(suggestionsColumns);
                for (int i = 0; i < searchProductResponses.size(); i++) {
                    String[] rowData = {String.valueOf(i), searchProductResponses.get(i).getItemName()};
                    cursor.addRow(rowData);
                }
                adapter.changeCursor(cursor);
            } else {
                MatrixCursor cursor = new MatrixCursor(suggestionsColumns);
                adapter.changeCursor(cursor);
            }
        });

    }

    private void observeSearchProduct() {
        productVM.productMutableLiveData.observe(this, product -> {
            if (product != null) {
                App.product = product.getData().get(0);
                if (product.getData().get(0).getSerial() && !isSerial) {
                    binding.serialDialog.getRoot().setVisibility(View.VISIBLE);
                    binding.serialDialog.confirm.setOnClickListener(view -> {
                        binding.serialDialog.confirm.setVisibility(View.GONE);
                        binding.serialDialog.progress.setVisibility(View.VISIBLE);
                        if (!binding.serialDialog.serialNumberInput.getText().toString().isEmpty())
                            productVM.getProduct(App.product.getItemName(), uuid, binding.serialDialog.serialNumberInput.getText().toString(), getMoveType(), itemCode);
                        else
                            binding.serialDialog.serialNumberInput.setError("مطلوب");
                        isSerial = true;
                    });
                } else if (isSerial) {
                    if (!App.product.getxBarCodeSerial().isEmpty())
                        binding.serialDialog.serialNumberInput.setText(App.product.getxBarCodeSerial());
                    App.serialNumber = binding.serialDialog.serialNumberInput.getText().toString();
                    binding.serialDialog.confirm.setVisibility(View.VISIBLE);
                    binding.serialDialog.progress.setVisibility(View.GONE);
                    isSerial = false;
                    startActivity(new Intent(this, ProductDetails.class));
                    App.editingPos = 0;
                    finish();
                } else {
                    startActivity(new Intent(this, ProductDetails.class));
                    App.editingPos = 0;
                    finish();
                }
            }
        });
    }

    private void invoiceName() {
        switch (App.invoiceType) {
            case Sales:
                binding.invoice.setText("متابعة فاتورة المبيعات");
                Log.e("checkType", "Sales");
                break;
            case ReturnSales:
                binding.invoice.setText("متابعة مرتجع المبيعات");
                Log.e("checkType", "ReturnSales");
                break;
            case Purchase:
                binding.invoice.setText("متابعة فاتورة المشتريات");
                Log.e("checkType", "Purchase");
                break;
            case ReturnPurchased:
                binding.invoice.setText("متابعة مرتجع المشتريات");
                Log.e("checkType", "ReturnPurchased");
                break;

            default:
                binding.invoice.setText("متابعة فاتورة المبيعات");
                Log.e("checkType10", "Sales");
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
                if (App.isNetworkAvailable(AddProducts.this)) {
                    productVM.getProduct(contents, uuid, null, getMoveType(), null);
                } else {
                    App.noConnectionDialog(AddProducts.this);
                }
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                Log.e("checkType4", App.invoiceType.name());
                invoiceName();

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
                buffer.add(new MyButton(AddProducts.this, R.drawable.delete, Color.RED, pos -> {
                    if (selectedProductsAdapter.getItemCount() == 1) {
                        App.selectedProducts = new ArrayList<>();
                    }
                    selectedProductsAdapter.callDeleteFunction(pos);
                    setOrderSummary(false);
                }));
                buffer.add(new MyButton(AddProducts.this, R.drawable.ic_baseline_edit_24, Color.GRAY, pos -> {
                    selectedProductsAdapter.callEditFunction(pos);
                }));
            }
        };
    }


}
