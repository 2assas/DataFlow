package com.dataflowstores.dataflow.ui.searchItemPrice;

import static android.telephony.MbmsDownloadSession.RESULT_CANCELLED;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.Settings;
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

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ViewModels.ProductVM;
import com.dataflowstores.dataflow.databinding.SearchItemPriceBinding;
import com.dataflowstores.dataflow.pojo.product.ProductData;
import com.dataflowstores.dataflow.pojo.product.SearchProductResponse;
import com.dataflowstores.dataflow.pojo.searchItemPrice.ItemPriceItem;
import com.dataflowstores.dataflow.pojo.users.CustomerData;
import com.dataflowstores.dataflow.ui.BaseActivity;
import com.dataflowstores.dataflow.ui.ScanBarCode;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.fragments.BottomSheetFragment;
import com.dataflowstores.dataflow.ui.listeners.MyDialogCloseListener;

import java.util.ArrayList;
import java.util.List;

public class SearchItemPrice extends BaseActivity implements MyDialogCloseListener, ItemPriceAdapter.ItemClickListener {
    SearchItemPriceBinding binding;
    ProductVM productVM;
    String uuid;
    private SimpleCursorAdapter adapter;
    private final String[] suggestionsColumns = {"_id", "suggestion"};
    Boolean isSerial = false;
    List<SearchProductResponse> searchProductList = new ArrayList<>();
    String itemCode = "";


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
            observeItemPrice();
            searchProducts();
            observeSearching();
            observeSearchProduct();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            productVM.compositeDisposable.clear();

    }

    @Override
    protected void onStop() {
        super.onStop();
            productVM.compositeDisposable.clear();

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
        binding.searchProducts.setOnClickListener(view -> {
            binding.searchProducts.onActionViewExpanded(); // Expand the SearchView
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.searchProducts, InputMethodManager.SHOW_IMPLICIT);
        });
    }

    private void searchProducts() {

        binding.searchProducts.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (App.isNetworkAvailable(SearchItemPrice.this))
                    productVM.getProduct(s, uuid, null, 1, null);
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
                productVM.setSearchQuery(s);
                binding.invoice.setText("بحث عن صنف");
                binding.invoice.setOnClickListener(view -> {
                    if (App.isNetworkAvailable(SearchItemPrice.this))
                        productVM.getProduct(s, uuid, null, 1, null);
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
                        productVM.getProduct(suggestion, uuid, null, 1, itemCode);
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
            if (product.getData() != null) {
                App.product = product.getData().get(0);
                if (App.product.getItemName() != null) {
                    getItemPrice(App.product);
                }
            }
        });
    }

    public void getItemPrice(ProductData productData) {
        productVM.getItemPrice(uuid, productData.getBranchISN(), productData.getItemISN(), binding.sellingPrice.isChecked() ? 2 : 1);
        binding.itemName.setText(productData.getItemName());
        binding.itemNameCon.setVisibility(View.VISIBLE);
        binding.progress.setVisibility(View.VISIBLE);
    }

    private void observeItemPrice() {
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
                    productVM.getProduct(contents, uuid, null, 1, null);
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
