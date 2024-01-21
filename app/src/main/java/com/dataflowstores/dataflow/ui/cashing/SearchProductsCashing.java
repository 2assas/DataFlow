package com.dataflowstores.dataflow.ui.cashing;

import static android.telephony.MbmsDownloadSession.RESULT_CANCELLED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ViewModels.CheckoutVM;
import com.dataflowstores.dataflow.ViewModels.ProductVM;
import com.dataflowstores.dataflow.ViewModels.SettingVM;
import com.dataflowstores.dataflow.databinding.AddProductsBinding;
import com.dataflowstores.dataflow.pojo.product.SearchProductResponse;
import com.dataflowstores.dataflow.pojo.users.CustomerData;
import com.dataflowstores.dataflow.pojo.workStation.BranchData;
import com.dataflowstores.dataflow.ui.BaseActivity;
import com.dataflowstores.dataflow.ui.Checkout;
import com.dataflowstores.dataflow.ui.ScanBarCode;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.utils.SingleShotLocationProvider;
import com.dataflowstores.dataflow.utils.SwipeHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SearchProductsCashing extends BaseActivity {
    AddProductsBinding binding;
    ProductVM productVM;
    SettingVM settingVM;
    CheckoutVM checkoutVM;
    String uuid;
    SelectedProductsCashingAdapter selectedProductsAdapter;
    int moveType = 16;
    float lat = 0;
    float _long = 0;
    Integer AllowStoreMinusConfirm = 0;
    BranchData selectedBranch;
    List<BranchData> branchesList;
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
            settingVM = new ViewModelProvider(this).get(SettingVM.class);
            checkoutVM = new ViewModelProvider(this).get(CheckoutVM.class);
            uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            moveType = getIntent().getIntExtra("moveType", 16);
            App.isEditing = false;
            setupViews();
            settingVM.getStoresCashing(App.currentUser.getBranchISN(), uuid, moveType);
            observeStores();
            requiredData();
            searchProducts();
            recyclerSwipe();
            barCodeScan();
            observeBranch();
            observeSearching();
            observeSearchProduct();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        App.selectedProducts = new ArrayList<>();
        finish();
    }

    public void setupViews() {
        settingVM.toastErrorMutableLiveData.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());
        binding.notes.setVisibility(View.VISIBLE);
        writeSaveButton();
        if (App.selectedProducts.size() > 0) {
            setOrderSummary(false);
        }
        binding.invoice.setOnClickListener(view -> {
            if (App.selectedProducts.size() > 0) {
                if (requiredData()) {
                    binding.invoice.setClickable(false);
                    if (lat == 0 && _long == 0) {
                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            setOrderSummary(true);
                        }, 1000);
                    } else {
                        setOrderSummary(true);
                    }
                    binding.progress.setVisibility(View.VISIBLE);
                }
            }
        });
        binding.branch.setOnClickListener(v -> {
            binding.addBranch.getRoot().setVisibility(View.VISIBLE);
            if (branchesList != null)
                branchSpinner();
        });
        binding.notes.setOnClickListener(v -> {
            binding.addNotes.getRoot().setVisibility(View.VISIBLE);
            binding.addNotes.notes.setText(App.headerNotes);
        });
        binding.addNotes.saveNotes.setOnClickListener(v -> {
            if (!binding.addNotes.notes.getText().toString().isEmpty()) {
                App.headerNotes = binding.addNotes.notes.getText().toString();
            }
            binding.addNotes.getRoot().setVisibility(View.GONE);
        });
        binding.addBranch.saveBranch.setOnClickListener(v -> {
            App.currentBranch = selectedBranch;
            binding.addBranch.getRoot().setVisibility(View.GONE);
        });
        binding.addNotes.close.setOnClickListener(v -> binding.addNotes.getRoot().setVisibility(View.GONE));
        binding.addBranch.close.setOnClickListener(v -> binding.addBranch.getRoot().setVisibility(View.GONE));
    }

    @SuppressLint("SetTextI18n")
    private void setOrderSummary(boolean showDialog) {
        int count = 0;
        double quantity = 0;
        binding.orderSummaryContainer.setVisibility(View.VISIBLE);
        if (App.selectedProducts.size() > 0) {
            for (int i = 0; i < App.selectedProducts.size(); i++) {
                quantity += App.selectedProducts.get(i).getQuantity();
            }
            count = App.selectedProducts.size();
        }
        if (showDialog) {
            new androidx.appcompat.app.AlertDialog.Builder(this).setMessage("لديك عدد "+"(" + count + ")" + " سطور بإجمالى كمية " +"("+ String.format(Locale.ENGLISH, "%.2f", quantity) + ")" +
                    " هل أنت متأكد؟").setPositiveButton("تأكيد", ((dialogInterface, i) -> {
                invoicePost();
                dialogInterface.dismiss();
            })).setNegativeButton("إلغاء", ((dialogInterface, i) -> {
                dialogInterface.dismiss();
            })).setCancelable(false).show();
        } else {
            binding.cartLinesCount.setText("السطور: " + count);
            binding.cartLinesQuantity.setText("الكمية: " + String.format(Locale.ENGLISH, "%.2f", quantity));
            binding.cartLinesTotal.setVisibility(View.GONE);
        }
    }

    public void observeBranch() {
        settingVM.branchesMutableLiveData.observe(this, branches -> {
            if (branches.getBranchData().size() > 0) {
                branchesList = branches.getBranchData();
            }
        });
    }


    private void branchSpinner() {
        ArrayList<String> branchesName = new ArrayList<>();
        for (int i = 0; i < branchesList.size(); i++) {
            if (i == 0) {
                branchesName.add("إختر الفرع");
            }
            branchesName.add(branchesList.get(i).getBranchName());
        }
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, branchesName);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.addBranch.branchSpinner.setAdapter(aa);
        binding.addBranch.branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    selectedBranch = null;
                } else
                    selectedBranch = branchesList.get(i - 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedBranch = branchesList.get(0);
            }
        });
        if (App.currentBranch != null) {
            for (int i = 0; i < branchesList.size(); i++) {
                if (App.currentBranch.getBranchISN() == branchesList.get(i).getBranchISN() && Objects.equals(App.currentBranch.getBranchName(), branchesList.get(i).getBranchName())) {
                    binding.addBranch.branchSpinner.setSelection(i + 1);
                }
            }
        }
    }


    public void searchProducts() {
        binding.searchProducts.setOnClickListener(view -> {
            binding.searchProducts.onActionViewExpanded(); // Expand the SearchView
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.searchProducts, InputMethodManager.SHOW_IMPLICIT);
        });
        binding.searchProducts.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (App.isNetworkAvailable(SearchProductsCashing.this))
                    productVM.getProduct(s, uuid, null, moveType, null);
                else {
                    App.noConnectionDialog(SearchProductsCashing.this);
                }
                BottomSheetFragmentCashing bottomSheetFragment = new BottomSheetFragmentCashing(moveType);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                writeSaveButton();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                binding.invoice.setText("بحث عن صنف");
                productVM.setSearchQuery(s);

                binding.invoice.setOnClickListener(view -> {
                    if (App.isNetworkAvailable(SearchProductsCashing.this))
                        productVM.getProduct(s, uuid, null, moveType, null);
                    else {
                        App.noConnectionDialog(SearchProductsCashing.this);
                    }
                    BottomSheetFragmentCashing bottomSheetFragment = new BottomSheetFragmentCashing(moveType);
                    bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                    writeSaveButton();
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
                        productVM.getProduct(suggestion, uuid, null, moveType, itemCode);
                        binding.progress.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }
        });

        binding.productsRecycler.setLayoutManager(new LinearLayoutManager(this));
        selectedProductsAdapter = new SelectedProductsCashingAdapter(App.selectedProducts, this, moveType);
        selectedProductsAdapter.notifyDataSetChanged();
        binding.productsRecycler.setAdapter(selectedProductsAdapter);

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
                            productVM.getProduct(App.product.getItemName(), uuid, binding.serialDialog.serialNumberInput.getText().toString(), moveType, itemCode);
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
                    Intent intent = new Intent(this, ProductScreenCashing.class);
                    App.editingPos = 0;
                    intent.putExtra("moveType", moveType);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(this, ProductScreenCashing.class);
                    App.editingPos = 0;
                    intent.putExtra("moveType", moveType);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void barCodeScan() {
        binding.scan.setOnClickListener(view -> {
            Intent intent = new Intent(this, ScanBarCode.class);
            startActivityForResult(intent, 0);
        });
    }

    public void observeStores() {
        settingVM.storesMutableLiveData.observe(this, stores -> {
            if (stores.getStatus() == 1) {
                App.storesCashing = stores;
                Log.e("checkStoresSize", stores.getData().size() + " == ");
            }
        });

        checkoutVM.responseDataMutableLiveData.observe(this, response -> {
            binding.invoice.setClickable(true);
            binding.progress.setVisibility(View.GONE);
            String errorMessage = response.getMessage();
            if (response.getMessage().equals("Not saved ... please save again")) {
                errorMessage = "لا يوجد الكمية الكافية من هذا الصنف";
            }
            if (response.getStatus() == 0 && App.currentUser.getAllowStoreMinus() == 2) {
                new AlertDialog.Builder(this).
                        setTitle("binding.invoiceTemplate.ص فالمخزن")
                        .setMessage(errorMessage)
                        .setCancelable(false)
                        .setPositiveButton("متابعة", (dialogInterface, i) -> {
                            AllowStoreMinusConfirm = 1;
                            setOrderSummary(true);
                        }).setNegativeButton("إلغاء", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        }).show();
            } else if (response.getStatus() == 0 && (App.currentUser.getAllowStoreMinus() == 1 || App.currentUser.getAllowStoreMinus() == 4)) {
                String error = response.getMessage();
                if (response.getMessage().equals("Not saved ... please save again")) {
                    error = "لا يوجد الكمية الكافية من هذا الصنف";
                }

                new AlertDialog.Builder(this)
                        .setMessage(error)
                        .setCancelable(false)
                        .setNegativeButton("إلغاء", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        }).show();
            } else {
                if (App.selectedProducts.size() > 0) {
                    App.selectedProducts = new ArrayList<>();
                    App.invoiceResponse = response;
                    App.customer = new CustomerData();
                    App.currentBranch = null;
                    App.headerNotes = "";
                    Intent intent = new Intent(this, CashingPrinting.class);
                    intent.putExtra("moveType", moveType);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                if (App.isNetworkAvailable(SearchProductsCashing.this))
                    productVM.getProduct(contents, uuid, null, moveType, null);
                else {
                    App.noConnectionDialog(SearchProductsCashing.this);
                }
                BottomSheetFragmentCashing bottomSheetFragment = new BottomSheetFragmentCashing(moveType);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
                writeSaveButton();
            }
            if (resultCode == RESULT_CANCELLED) {
                //handle cancel
            }
        }
    }

    public SwipeHelper recyclerSwipe() {
        return new SwipeHelper(SearchProductsCashing.this, binding.productsRecycler, 250) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List buffer) {
                buffer.add(new MyButton(SearchProductsCashing.this, R.drawable.delete, Color.RED,
                        pos -> {
                            selectedProductsAdapter.callDeleteFunction(pos);
                            setOrderSummary(false);
                        }

                ));

                buffer.add(new MyButton(SearchProductsCashing.this, R.drawable.ic_baseline_edit_24, Color.GRAY,
                        pos -> selectedProductsAdapter.callEditFunction(pos)
                ));
            }
        };
    }


    public void writeSaveButton() {
        switch (moveType) {
            case 14:
                binding.invoice.setText("حفظ التحويل المخزنى");
                break;
            case 16:
                binding.invoice.setText("حفظ إذن الصرف");
                binding.branch.setVisibility(View.VISIBLE);
                binding.addBranch.title.setText("فرع الإستلام");
                settingVM.getBranches(uuid);
                break;
            case 17:
                binding.invoice.setText("حفظ إذن الإستلام");
                binding.branch.setVisibility(View.VISIBLE);
                binding.addBranch.title.setText("فرع الصرف");
                settingVM.getBranches(uuid);
                break;
            case 15:
                binding.invoice.setText("حفظ تكوين الصنف");
                break;
            case 12:
                binding.invoice.setText("حفظ كميات أول مدة");
                break;
            case 8:
                binding.invoice.setText("حفظ إهلاكات الأصناف");
                break;
            case 7:
                binding.invoice.setText("حفظ جرد الأصناف");
                break;

        }
    }

    public void invoicePost() {
        int dealerBranchISN = 0;
        long dealerISN = 0;
        int dealerType = 0;
        int salesManBranchISN = 0;
        int salesManISN = 0;
        double paidValue = 0;
        double remainValue = 0;
        long safeDepositBranchISN = 0;
        long safeDepositISN = 0;
        long bankBranchISN = 0;
        long bankISN = 0;
        String tableNum = "";
        String deliveryPhone = "";
        String deliveryAddress = "";
        if (App.customer.getDealerName() != null) {
            dealerBranchISN = App.customer.getBranchISN();
            dealerISN = App.customer.getDealer_ISN();
            dealerType = App.customer.getDealerType();
            deliveryPhone = App.customer.getDealerPhone();
            deliveryAddress = App.customer.getDealerAddress();
        }
        if (App.agent.getDealerName() != null) {
            salesManBranchISN = App.agent.getBranchISN();
            salesManISN = App.agent.getDealer_ISN();
        }
        ArrayList<Long> ItemBranchISN = new ArrayList<>();
        ArrayList<Long> ItemISN = new ArrayList<>();
        ArrayList<Long> PriceTypeBranchISN = new ArrayList<>();
        ArrayList<Long> PriceTypeISN = new ArrayList<>();
        ArrayList<Long> StoreBranchISN = new ArrayList<>();
        ArrayList<Long> StoreISN = new ArrayList<>();
        ArrayList<Float> BasicQuantity = new ArrayList<>();
        ArrayList<Float> BonusQuantity = new ArrayList<>();
        ArrayList<Float> TotalQuantity = new ArrayList<>();
        ArrayList<Double> Price = new ArrayList<>();
        ArrayList<Long> MeasureUnitBranchISN = new ArrayList<>();
        ArrayList<Long> MeasureUnitISN = new ArrayList<>();
        ArrayList<Long> BasicMeasureUnitBranchISN = new ArrayList<>();
        ArrayList<Long> BasicMeasureUnitISN = new ArrayList<>();
        ArrayList<String> ItemSerial = new ArrayList<>();
        ArrayList<String> ExpireDate = new ArrayList<>();
        ArrayList<Long> ColorBranchISN = new ArrayList<>();
        ArrayList<Long> ColorISN = new ArrayList<>();
        ArrayList<Long> SizeBranchISN = new ArrayList<>();
        ArrayList<Long> SizeISN = new ArrayList<>();
        ArrayList<Long> SeasonBranchISN = new ArrayList<>();
        ArrayList<Long> SeasonISN = new ArrayList<>();
        ArrayList<Long> Group1BranchISN = new ArrayList<>();
        ArrayList<Long> Group1ISN = new ArrayList<>();
        ArrayList<Long> Group2BranchISN = new ArrayList<>();
        ArrayList<Long> Group2ISN = new ArrayList<>();
        ArrayList<String> LineNotes = new ArrayList<>();
        long numberOFItems = 0;
        ArrayList<Double> netPrices = new ArrayList<>();
        ArrayList<Double> basicMeasureUnitQuantity = new ArrayList<>();
        ArrayList<Boolean> expireDateBool = new ArrayList<>();
        ArrayList<Boolean> colorsBool = new ArrayList<>();
        ArrayList<Boolean> sizesBool = new ArrayList<>();
        ArrayList<Boolean> serialBool = new ArrayList<>();
        ArrayList<Boolean> seasonsBool = new ArrayList<>();
        ArrayList<Boolean> group1Bool = new ArrayList<>();
        ArrayList<Boolean> group2Bool = new ArrayList<>();
        ArrayList<Boolean> serviceItem = new ArrayList<>();
        ArrayList<Double> itemTax = new ArrayList<>();//percentage
        ArrayList<Double> itemTaxValue = new ArrayList<>();//value
        ArrayList<String> itemName = new ArrayList<>();//value
        ArrayList<Double> discount1 = new ArrayList<>();
        ArrayList<Long> toStoreISN = new ArrayList<>();
        ArrayList<Long> toStoreBranchISN = new ArrayList<>();
        ArrayList<Integer> allowStoreMinus = new ArrayList<>();
        ArrayList<String> productStoreName = new ArrayList<>();

        for (int i = 0; i < App.selectedProducts.size(); i++) {
            itemName.add(App.selectedProducts.get(i).getItemName());
            ItemBranchISN.add((long) App.selectedProducts.get(i).getBranchISN());
            allowStoreMinus.add(App.selectedProducts.get(i).getAllowStoreMinus());
            productStoreName.add(App.selectedProducts.get(i).getSelectedStore().getStoreName());
            ItemISN.add((long) App.selectedProducts.get(i).getItemISN());
            if (App.selectedProducts.get(i).getSelectedPriceType() != null) {
                PriceTypeBranchISN.add((long) App.selectedProducts.get(i).getSelectedPriceType().getBranchISN());
                PriceTypeISN.add((long) App.selectedProducts.get(i).getSelectedPriceType().getPricesType_ISN());
            } else {
                PriceTypeBranchISN.add(0L);
                PriceTypeISN.add(0L);
            }
            if (App.selectedProducts.get(i).getSelectedStore() != null) {
                StoreBranchISN.add((long) App.selectedProducts.get(i).getSelectedStore().getBranchISN());
                StoreISN.add((long) App.selectedProducts.get(i).getSelectedStore().getStore_ISN());
            } else {
                StoreBranchISN.add(0L);
                StoreISN.add(0L);
            }
            BasicQuantity.add(App.selectedProducts.get(i).getActualQuantity());
            TotalQuantity.add(App.selectedProducts.get(i).getQuantity());
            netPrices.add(App.selectedProducts.get(i).getNetPrice());
            MeasureUnitBranchISN.add((long) App.selectedProducts.get(i).getSelectedUnit().getMeasureUnitBranchISN());
            MeasureUnitISN.add((long) App.selectedProducts.get(i).getSelectedUnit().getMeasureUnitISN());
            basicMeasureUnitQuantity.add(App.selectedProducts.get(i).getSelectedUnit().getBasicUnitQuantity());
            serviceItem.add(App.selectedProducts.get(i).getServiceItem());
            BasicMeasureUnitBranchISN.add((long) App.selectedProducts.get(i).getBasicMeasureUnit().getMeasureUnitBranchISN());
            BasicMeasureUnitISN.add((long) App.selectedProducts.get(i).getBasicMeasureUnit().getMeasureUnitISN());
            if (App.selectedProducts.get(i).getSelectedColor() != null) {
                ColorBranchISN.add((long) App.selectedProducts.get(i).getSelectedColor().getBranchISN());
                ColorISN.add((long) App.selectedProducts.get(i).getSelectedColor().getStoreColorISN());
                colorsBool.add(true);
            } else {
                colorsBool.add(false);
                ColorBranchISN.add(0L);
                ColorISN.add(0L);
            }
            if (App.selectedProducts.get(i).getSelectedSize() != null) {
                SizeBranchISN.add((long) App.selectedProducts.get(i).getSelectedSize().getBranchISN());
                SizeISN.add((long) App.selectedProducts.get(i).getSelectedSize().getStoreSizeISN());
                sizesBool.add(true);
            } else {
                sizesBool.add(false);
                SizeBranchISN.add(0L);
                SizeISN.add((long) 0);
            }
            if (App.selectedProducts.get(i).getSelectedSeason() != null) {
                seasonsBool.add(true);
                SeasonBranchISN.add((long) App.selectedProducts.get(i).getSelectedSeason().getBranchISN());
                SeasonISN.add((long) App.selectedProducts.get(i).getSelectedSeason().getStoreSeasonISN());
            } else {
                seasonsBool.add(false);
                SeasonBranchISN.add(0L);
                SeasonISN.add(0L);
            }
            if (App.selectedProducts.get(i).getSelectedGroup1() != null) {
                group1Bool.add(true);
                Group1BranchISN.add((long) App.selectedProducts.get(i).getSelectedGroup1().getBranchISN());
                Group1ISN.add((long) App.selectedProducts.get(i).getSelectedGroup1().getStoreGroup1ISN());
            } else {
                group1Bool.add(false);
                Group1BranchISN.add(0L);
                Group1ISN.add(0L);
            }
            if (App.selectedProducts.get(i).getSelectedGroup2() != null) {
                group2Bool.add(true);
                Group2BranchISN.add((long) App.selectedProducts.get(i).getSelectedGroup2().getBranchISN());
                Group2ISN.add((long) App.selectedProducts.get(i).getSelectedGroup2().getStoreGroup2ISN());
            } else {
                group2Bool.add(false);
                Group2BranchISN.add(0L);
                Group2ISN.add(0L);
            }
            LineNotes.add(App.selectedProducts.get(i).getUserNote());
//            ExpireDate.add(App.selectedProducts.get(i).getSelectedExpireDate());
            if (App.selectedProducts.get(i).getExpireDate()) {
                ExpireDate.add(App.selectedProducts.get(i).getSelectedExpireDate());
                expireDateBool.add(true);
            } else {
                ExpireDate.add("");
                expireDateBool.add(false);
            }
            if (App.selectedProducts.get(i).getSerial()) {
                ItemSerial.add(App.selectedProducts.get(i).getSelectedSerial());
                serialBool.add(true);
            } else {
                ItemSerial.add("");
                serialBool.add(false);
            }
            if (moveType == 14) {
                toStoreISN.add((long) App.selectedProducts.get(i).getSelectedToStore().getStore_ISN());
                toStoreBranchISN.add((long) App.selectedProducts.get(i).getSelectedToStore().getBranchISN());
            }
        }
        numberOFItems = ((long) App.selectedProducts.size());
        try {
            if (App.isNetworkAvailable(this)) {
                Log.e("checkInvoice", "start requesting...");
                checkoutVM.placeInvoice(
                        App.currentUser.getBranchISN(), uuid, -1,
                        -1, dealerType, dealerBranchISN, dealerISN, salesManBranchISN, salesManISN,
                        App.headerNotes, 0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        safeDepositBranchISN, safeDepositISN, bankBranchISN, bankISN, tableNum,
                        deliveryPhone, deliveryAddress, App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN(), "0",
                        "", 0, 0, ItemBranchISN, ItemISN, null, null, StoreBranchISN, StoreISN,
                        BasicQuantity, BonusQuantity, TotalQuantity, Price, MeasureUnitBranchISN, MeasureUnitISN, BasicMeasureUnitBranchISN, BasicMeasureUnitISN, ItemSerial,
                        ExpireDate, ColorBranchISN, ColorISN, SizeBranchISN, SizeISN, SeasonBranchISN, SeasonISN, Group1BranchISN, Group1ISN, Group2BranchISN, Group2ISN, LineNotes, numberOFItems,
                        netPrices, basicMeasureUnitQuantity, expireDateBool, colorsBool, seasonsBool, sizesBool, serialBool, group1Bool, group2Bool, serviceItem, itemTax, itemTaxValue, 0,
                        App.currentUser.getAllowStoreMinus(), itemName, discount1, AllowStoreMinusConfirm, lat, _long, "exchange_permission", moveType, moveType == 14 ? toStoreBranchISN : null, moveType == 14 ? toStoreISN : null,
                        App.currentBranch != null ? String.valueOf(App.currentBranch.getBranchISN()) : null, allowStoreMinus, productStoreName);
            } else {
                App.noConnectionDialog(this);
            }
        } catch (Exception e) {
            Log.e("ERRORRR!", String.valueOf(e));
        }
    }

    public boolean requiredData() {
        if (!checkPermission()) {
            Log.e("permission", "permission needed");
            requestPermission();
            return false;
        } else {
            getLocation(this);
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
    }

    public boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED && SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults.length > 0) {
                    boolean fineLocation = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean coarseLocation = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (fineLocation && coarseLocation) {
                        if (checkPermission()) {
                            getLocation(this);
                        } else {
                            requestPermission();
                        }
                    } else {
                        new AlertDialog.Builder(this).setTitle("الموقع مطلوب")
                                .setMessage("لابد من السماح لأخذ الموقع الخاص بك")
                                .setPositiveButton("حسنا", (dialog, which) -> {
                                    requestPermission();
                                    dialog.dismiss();
                                });
                    }
                }
                break;
        }
    }

    public void getLocation(final Context context) {
        SingleShotLocationProvider.requestCurrentLocation(context,
                location -> {
                    lat = location.latitude;
                    _long = location.longitude;
                    Log.e("checkLocation", lat + " , " + _long);
                });
    }

}
