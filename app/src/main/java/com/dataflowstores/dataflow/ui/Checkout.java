package com.dataflowstores.dataflow.ui;

import static com.dataflowstores.dataflow.App.getMoveType;
import static com.dataflowstores.dataflow.pojo.invoice.InvoiceType.Purchase;
import static com.dataflowstores.dataflow.pojo.invoice.InvoiceType.ReturnPurchased;
import static com.dataflowstores.dataflow.pojo.invoice.InvoiceType.Sales;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ViewModels.CheckoutVM;
import com.dataflowstores.dataflow.databinding.CheckoutBinding;
import com.dataflowstores.dataflow.pojo.product.ProductData;
import com.dataflowstores.dataflow.pojo.settings.BanksData;
import com.dataflowstores.dataflow.pojo.settings.SafeDepositData;
import com.dataflowstores.dataflow.ui.invoice.PrintInvoice;
import com.dataflowstores.dataflow.utils.SingleShotLocationProvider;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Checkout extends BaseActivity implements LocationListener {
    private static final int REQUEST_CHECK_SETTINGS = 111;
    CheckoutBinding binding;
    int paymentMethod = 0;
    int saleType = 1;
    Calendar myCalendar = Calendar.getInstance();
    boolean dateValidation = false;
    SafeDepositData safeDepositData = new SafeDepositData();
    BanksData banksCheckData = new BanksData();
    BanksData banksCreditData = new BanksData();
    double totalLineTaxes = 0;
    private boolean isUpdatingFields = false; // Flag to track updates
    double productsTotal = 0;
    int numberOfItems = 0;
    DecimalFormat df = new DecimalFormat("#.##");
    double totalAfterService = 0;
    double totalAfterDiscount = 0;
    double totalAfterTax = 0;
    CheckoutVM checkoutVM;
    String uuid;
    double serviceValue = 0;
    double deliveryValue = 0;
    double servicePercent = 0;
    double discountValue = 0;
    double discountPercent = 0;
    double taxValue = 0;
    double taxPercent = 0;
    String paymentMethodText = "";
    String selectedDate = "";
    Integer AllowStoreMinusConfirm = 0;
    float lat = 0;
    float _long = 0;
    boolean isLoading = false;
    private boolean isUpdatingField = false;
    private Double customerDiscount = null;
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.checkout);
            checkoutVM = new ViewModelProvider(this).get(CheckoutVM.class);
            uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            getProductsTotal();
            safeDepositSpinner();
            userRestricts();
            customer_agent();
            calculations();
            checkoutVM.toastErrorMutableLiveData.observe(this, s -> {
                isLoading = false;
                binding.progress.setVisibility(View.GONE);
                Toast.makeText(this, s, Toast.LENGTH_LONG).show();
            });

            binding.checkout.setOnClickListener(view -> {
                binding.progress.setVisibility(View.VISIBLE);
                if (requiredData())
                    if (App.currentUser.getMobileGPSMust() == 0 || lat != 0 || _long != 0) {
                        invoicePost();
                        binding.checkout.setClickable(false);
                    } else {
                        binding.checkout.setClickable(true);
                        new androidx.appcompat.app.AlertDialog.Builder(this).
                                setTitle("الموقع مطلوب")
                                .setMessage("لإتمام العملية يرجى السماح لإذن أخذ الموقع الحالى للجهاز")
                                .setPositiveButton("حسنا", (dialogInterface, i) -> {
                                    binding.progress.setVisibility(View.GONE);
                                    dialogInterface.dismiss();
                                    requestPermission();
                                    settingsRequest();
                                }).show();
                    }
            });

            checkoutVM.responseDataMutableLiveData.observe(this, response -> {
                isLoading = false;
                binding.progress.setVisibility(View.GONE);
                String errorMessage = response.getMessage();
                if (response.getMessage().equals("Not saved ... please save again")) {
                    errorMessage = "لا يوجد الكمية الكافية من هذا الصنف";
                    binding.checkout.setClickable(true);
                }
                if (response.getStatus() == 0 && App.currentUser.getAllowStoreMinus() == 2 && (App.invoiceType == Sales || App.invoiceType == ReturnPurchased)) {
                    new AlertDialog.Builder(this)
                            .setMessage(errorMessage)
                            .setCancelable(false)
                            .setPositiveButton("متابعة", (dialogInterface, i) -> {
                                AllowStoreMinusConfirm = 1;
                                totalAfterTax -= totalLineTaxes;
                                invoicePost();
                            }).setNegativeButton("إلغاء", (dialogInterface, i) -> {
                                dialogInterface.dismiss();

                                startActivity(new Intent(Checkout.this, AddProducts.class));
                                finish();
                            }).show();
                } else if (response.getStatus() == 0 && (App.currentUser.getAllowStoreMinus() == 1 || App.currentUser.getAllowStoreMinus() == 4)
                        && (App.invoiceType == Sales || App.invoiceType == ReturnPurchased)) {
                    String error = response.getMessage();
                    if (response.getMessage().equals("Not saved ... please save again")) {
                        error = "لا يوجد الكمية الكافية من هذا الصنف";
                    }
                    binding.checkout.setClickable(true);
                    new AlertDialog.Builder(this)
                            .setMessage(error)
                            .setCancelable(false)
                            .setNegativeButton("إلغاء", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                                startActivity(new Intent(Checkout.this, AddProducts.class));
                                finish();
                            }).show();
                } else if (response.getStatus() == 0) {
                    new AlertDialog.Builder(this)
                            .setMessage(response.getMessage())
                            .setCancelable(false)
                            .setNegativeButton("إلغاء", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                                startActivity(new Intent(Checkout.this, AddProducts.class));
                                finish();
                            }).show();
                } else {
                    App.invoiceResponse = response;
                    App.selectedProducts = new ArrayList<>();
                    binding.checkout.setClickable(true);
                    startActivity(new Intent(this, PrintInvoice.class));
                    finish();
                }
            });
            binding.back.setOnClickListener(view -> {
                startActivity(new Intent(Checkout.this, AddProducts.class));
                finish();
            });
            if (App.currentUser.getMobileGPSMust() == 1) {
                if (checkPermission())
                    getLocation(this);
                else
                    requestPermission();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        checkoutVM.compositeDisposable.clear();
    }

    @Override
    protected void onStop() {
        super.onStop();
        checkoutVM.compositeDisposable.clear();
    }

    @Override
    public void onBackPressed() {
        if (!isLoading) {
            startActivity(new Intent(Checkout.this, AddProducts.class));
            finish();
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

    public void settingsRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        result.addOnCompleteListener(task -> {
            try {
                LocationSettingsResponse response = task.getResult(ApiException.class);
            } catch (ApiException exception) {
                switch (exception.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) exception;
                            resolvable.startResolutionForResult(
                                    this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                        } catch (ClassCastException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        getLocation(Checkout.this);
                        Log.e("checkLocation", "granteeeed");
                    }
                    break;
                    case Activity.RESULT_CANCELED:
                        settingsRequest();//keep asking if imp or do whatever
                        break;
                }
                break;
        }
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
    public void userRestricts() {
        if (App.currentUser.getMobileTax() == 0) {
            binding.percentTax.setEnabled(false);
            binding.percentTaxVal.setEnabled(false);
            binding.percentTaxTxt.setTextColor(Color.GRAY);
            binding.percentValTaxTxt.setTextColor(Color.GRAY);
        }
        if (App.currentUser.getIsCasheir() == 0) {
            binding.cashCheck.setEnabled(false);
            binding.cashCheck.setChecked(false);
            binding.cashTxt.setTextColor(Color.GRAY);
        }
        if (App.currentUser.getIsCheck() == 0) {
            binding.cheqCheck.setEnabled(false);
            binding.cheqCheck.setChecked(false);
            binding.checkTxt.setTextColor(Color.GRAY);
        }
        if (App.currentUser.getIsCredit() == 0) {
            binding.creditCheck.setEnabled(false);
            binding.creditCheck.setChecked(false);
            binding.creditTxt.setTextColor(Color.GRAY);
        }
        if (App.customer.getDealerName() == null) {
            binding.cheqCheck.setEnabled(false);
            binding.cheqCheck.setChecked(false);
            binding.checkTxt.setTextColor(Color.GRAY);

            binding.laterCheck.setEnabled(false);
            binding.laterCheck.setChecked(false);
            binding.agelTxt.setTextColor(Color.GRAY);
            binding.cashCheck.setEnabled(true);
            paymentMethod = 1;
            binding.remainingContainer.setVisibility(View.GONE);
            binding.remainedCash.setVisibility(View.GONE);//todo: update based on Paid Amount
            binding.textView110.setVisibility(View.GONE);
            binding.cheqContainer.setVisibility(View.GONE);
            binding.creditBankCon.setVisibility(View.GONE);
            binding.remaining.setVisibility(View.GONE);
            binding.cashCheck.setChecked(true);
        }
        if (App.currentUser.getMobileDiscount() == 0 && App.customer.getDealerInvoiceDefaultDisc() == null) {
            binding.discountContainer.setVisibility(View.GONE);
        }
    }

    private void customerDiscount() {
        if (App.currentUser.getMobileDiscount() == 0 && Double.parseDouble(App.customer.getDealerInvoiceDefaultDisc()) <= 0) {
            binding.discountContainer.setVisibility(View.GONE);
        } else if (App.currentUser.getMobileDiscount() == 0 && Double.parseDouble(App.customer.getDealerInvoiceDefaultDisc()) > 0) {
            double defaultDiscount = Double.parseDouble(App.customer.getDealerInvoiceDefaultDisc());
            binding.percentDiscount.setText(String.format(Locale.US, "%.2f", defaultDiscount));
            binding.percentDiscount.setEnabled(false);
            binding.percentDiscountVal.setEnabled(false);
            calculateDiscount(true);
            calculateTax(true);
            updateTotals();
            customerDiscount = defaultDiscount;
        } else if (App.currentUser.getMobileDiscount() == 1 && Double.parseDouble(App.customer.getDealerInvoiceDefaultDisc()) > 0) {
            double defaultDiscount = Double.parseDouble(App.customer.getDealerInvoiceDefaultDisc());
            binding.percentDiscount.setText(String.format(Locale.US, "%.2f", defaultDiscount));
            calculateDiscount(true);
            calculateTax(true);
            updateTotals();
            customerDiscount = defaultDiscount;
        }
    }

    public void safeDepositSpinner() {
        ArrayList<String> safeDeposit = new ArrayList<>();
        for (int i = 0; i < App.safeDeposit.getData().size(); i++) {
            safeDeposit.add(App.safeDeposit.getData().get(i).getSafeDepositName());
        }
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, safeDeposit);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.safeDepositSpinner.setAdapter(aa);

        binding.safeDepositSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                safeDepositData = App.safeDeposit.getData().get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                safeDepositData = App.safeDeposit.getData().get(0);
            }
        });
        if (App.currentUser.getPermission() == 1)
            for (int i = 0; i < App.safeDeposit.getData().size(); i++) {
                if (App.currentUser.getSafeDepositISN() == App.safeDeposit.getData().get(i).getSafeDeposit_ISN()
                        && App.currentUser.getSafeDepositBranchISN() == App.safeDeposit.getData().get(i).getBranchISN()) {
                    binding.safeDepositSpinner.setSelection(i);
                }
            }
    }


    public void bankSpinner() {
        ArrayList<String> banks = new ArrayList<>();
        for (int i = 0; i < App.banks.getData().size(); i++) {
            banks.add(App.banks.getData().get(i).getBankName());
        }
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, banks);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.bankSpinner.setAdapter(aa);
        binding.bankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                banksCheckData = App.banks.getData().get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                banksCheckData = App.banks.getData().get(0);
            }
        });
    }

    public void creditBankSpinner() {
        ArrayList<String> banks = new ArrayList<>();
        for (int i = 0; i < App.banks.getData().size(); i++) {
            banks.add(App.banks.getData().get(i).getBankName());
        }
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, banks);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.creditBankSpinner.setAdapter(aa);
        binding.creditBankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                banksCreditData = App.banks.getData().get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                banksCreditData = App.banks.getData().get(0);
            }
        });
    }

    public void datePicker() {
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        binding.selectDateCon.setOnClickListener(view -> {
            new DatePickerDialog(this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateValidation = true;
        App.product.setSelectedExpireDate(sdf.format(myCalendar.getTime()));
        binding.selectDate.setText(sdf.format(myCalendar.getTime()));
        selectedDate = sdf.format(myCalendar.getTime());
    }

    public void laterCheck(View view) {
        binding.cheqCheck.setChecked(false);
        binding.creditCheck.setChecked(false);
        binding.cashCheck.setChecked(false);
        paymentMethod = 0;
        paymentMethodText = "Later";
        binding.agelTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        binding.cashTxt.setTextColor(getResources().getColor(R.color.black));
        binding.creditTxt.setTextColor(getResources().getColor(R.color.black));
        binding.checkTxt.setTextColor(getResources().getColor(R.color.black));
        binding.laterContainer.setVisibility(View.VISIBLE);
        binding.remainingContainer.setVisibility(View.VISIBLE);
        binding.remainedCash.setVisibility(View.VISIBLE);
        binding.remaining.setVisibility(View.VISIBLE);
        binding.textView110.setVisibility(View.VISIBLE);
        binding.cheqContainer.setVisibility(View.GONE);
        binding.creditBankCon.setVisibility(View.GONE);
        safeDepositSpinner();
    }

    public void cashCheck(View view) {
        binding.cheqCheck.setChecked(false);
        binding.creditCheck.setChecked(false);
        binding.laterCheck.setChecked(false);
        paymentMethod = 1;
        paymentMethodText = "Cash";
        binding.remaining.setText("");
        safeDepositSpinner();
        binding.agelTxt.setTextColor(getResources().getColor(R.color.black));
        binding.cashTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        binding.creditTxt.setTextColor(getResources().getColor(R.color.black));
        binding.checkTxt.setTextColor(getResources().getColor(R.color.black));
        binding.laterContainer.setVisibility(View.VISIBLE);
        binding.remainingContainer.setVisibility(View.GONE);
        binding.remainedCash.setVisibility(View.GONE);
        binding.textView110.setVisibility(View.GONE);
        binding.cheqContainer.setVisibility(View.GONE);
        binding.creditBankCon.setVisibility(View.GONE);
        deliveryValue = 0;
        binding.remaining.setVisibility(View.GONE);
    }

    public void cheqCheck(View view) {
        binding.cashCheck.setChecked(false);
        binding.creditCheck.setChecked(false);
        binding.laterCheck.setChecked(false);
        paymentMethodText = "Cheque";
        binding.remaining.setText("");
        binding.agelTxt.setTextColor(getResources().getColor(R.color.black));
        binding.cashTxt.setTextColor(getResources().getColor(R.color.black));
        binding.creditTxt.setTextColor(getResources().getColor(R.color.black));
        binding.checkTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        paymentMethod = 2;
        binding.cheqContainer.setVisibility(View.VISIBLE);
        binding.remainingContainer.setVisibility(View.GONE);
        binding.remainedCash.setVisibility(View.GONE);
        binding.textView110.setVisibility(View.GONE);
        binding.laterContainer.setVisibility(View.GONE);
        binding.laterContainer.setVisibility(View.GONE);
        binding.creditBankCon.setVisibility(View.GONE);
        binding.remaining.setVisibility(View.GONE);
        datePicker();
        bankSpinner();
    }

    public void creditCheck(View view) {
        binding.cheqCheck.setChecked(false);
        binding.cashCheck.setChecked(false);
        binding.laterCheck.setChecked(false);
        binding.remaining.setText("");
        paymentMethodText = "Credit";
        paymentMethod = 3;
        binding.agelTxt.setTextColor(getResources().getColor(R.color.black));
        binding.cashTxt.setTextColor(getResources().getColor(R.color.black));
        binding.creditTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        binding.checkTxt.setTextColor(getResources().getColor(R.color.black));
        binding.creditBankCon.setVisibility(View.VISIBLE);
        binding.remaining.setVisibility(View.GONE);
        binding.cheqContainer.setVisibility(View.GONE);
        binding.remainingContainer.setVisibility(View.GONE);
        binding.remainedCash.setVisibility(View.GONE);//todo: update based on Paid Amount
        binding.textView110.setVisibility(View.GONE);
        binding.laterContainer.setVisibility(View.GONE);
        binding.laterContainer.setVisibility(View.GONE);
        creditBankSpinner();
    }

    public void deliveryCheck(View view) {
        binding.externalCheck.setChecked(false);
        binding.sofraCheck.setChecked(false);
        binding.deliveryTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        binding.externalCheck.setTextColor(getResources().getColor(R.color.black));
        binding.sofraTxt.setTextColor(getResources().getColor(R.color.black));
        saleType = 2;
        binding.deliveryContainer.setVisibility(View.VISIBLE);
        binding.sofraValue.setVisibility(View.GONE);
        binding.deliveryValue.setVisibility(View.VISIBLE);
        binding.textView30.setText("قيمة التوصيل");
        binding.deliveryValue.setHint("أدخل قيمة التوصيل");
        binding.serviceContainer.setVisibility(View.GONE);
        totalAfterService = productsTotal;
        resetServiceValue();
        calculateTotals();
    }

    public void externalCheck(View view) {
        binding.deliverCheck.setChecked(false);
        binding.sofraCheck.setChecked(false);
        binding.deliveryTxt.setTextColor(getResources().getColor(R.color.black));
        binding.externalCheck.setTextColor(getResources().getColor(R.color.colorPrimary));
        totalAfterService = productsTotal;
        binding.deliveryValue.setText("0");
        binding.sofraTxt.setTextColor(getResources().getColor(R.color.black));
        saleType = 1;
        binding.deliveryContainer.setVisibility(View.GONE);
        binding.serviceContainer.setVisibility(View.VISIBLE);
        deliveryValue = 0;
        binding.deliveryValue.setText("0");
        calculateTotals();
    }

    public void tableCheck(View view) {
        binding.deliverCheck.setChecked(false);
        binding.externalCheck.setChecked(false);
        binding.deliveryTxt.setTextColor(getResources().getColor(R.color.black));
        binding.externalCheck.setTextColor(getResources().getColor(R.color.black));
        binding.sofraTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        saleType = 3;
        binding.deliveryValue.setText("0");
        binding.deliveryContainer.setVisibility(View.VISIBLE);
        binding.sofraValue.setVisibility(View.VISIBLE);
        binding.deliveryValue.setVisibility(View.GONE);
        binding.serviceContainer.setVisibility(View.VISIBLE);
        binding.textView30.setText("رقم السفرة");
        binding.sofraValue.setHint("أدخل رقم السفرة");
        deliveryValue = 0;
        calculateTotals();
    }

    @SuppressLint("SetTextI18n")
    public void getProductsTotal() {
        for (int i = 0; i < App.selectedProducts.size(); i++) {
            productsTotal += App.selectedProducts.get(i).getNetPrice();
            numberOfItems += App.selectedProducts.get(i).getQuantity();
            totalLineTaxes += (App.selectedProducts.get(i).getNetPrice() / 100) * Double.parseDouble(App.selectedProducts.get(i).getItemTax());
        }
        binding.totalTax2.setText(String.format(Locale.ENGLISH, "%.3f", totalLineTaxes));
        Log.e("checkCalc ", "Total: " + productsTotal + " Line Tax: " + totalLineTaxes);
        binding.finalTotal.setText(String.format(Locale.US, "%.3f", productsTotal + totalLineTaxes) + " جنيه");
        binding.remainedCash.setText(String.format(Locale.US, "%.3f", productsTotal + totalLineTaxes) + " جنيه");
    }

    private void deliveryCheckListener() {
        binding.deliverCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                deliveryValue = 0;
                binding.deliveryValue.setText("");
                resetServiceValue();
                calculateTotals();
            }

        });
    }

    public void customer_agent() {
        if (App.customer.getDealerName() == null) {
            binding.client.setVisibility(View.GONE);
            binding.clientName.setVisibility(View.GONE);
        } else {
            binding.clientName.setText(App.customer.getDealerName());
            if (App.invoiceType == Purchase || App.invoiceType == ReturnPurchased)
                binding.client.setText("المورد");

        }
        if (App.agent.getDealerName() == null) {
            binding.agentName.setVisibility(View.GONE);
            binding.agent.setVisibility(View.GONE);
        } else {
            binding.agentName.setText(App.agent.getDealerName());
        }
    }

    @SuppressLint("SetTextI18n")
    public void calculations() {
        binding.totalItems.setText(String.format(Locale.US, "%.3f", productsTotal) + " جنيه");
        totalAfterService = productsTotal;
        totalAfterDiscount = productsTotal;
        totalAfterTax = (float) productsTotal;
        setTotal(productsTotal);
        deliveryCheckListener();
        startTextWatcher();
    }

    private void startTextWatcher() {
        binding.deliveryValue.addTextChangedListener(textWatcher);
        binding.percentService.addTextChangedListener(textWatcher);
        binding.percentServiceVal.addTextChangedListener(textWatcher);
        binding.percentDiscount.addTextChangedListener(textWatcher);
        binding.percentDiscountVal.addTextChangedListener(textWatcher);
        if (App.customer.getDealerInvoiceDefaultDisc() != null)
            customerDiscount();
        binding.percentTax.addTextChangedListener(textWatcher);
        binding.percentTaxVal.addTextChangedListener(textWatcher);
        binding.remaining.addTextChangedListener(textWatcher);

    }


    @SuppressLint("SetTextI18n")
    public void setTotal(double total) {
        binding.totalService.setText(String.format(Locale.US, "%.3f", total) + " جنيه");
        binding.totalDiscount.setText(String.format(Locale.US, "%.3f", total) + " جنيه");
        binding.totalTax.setText(String.format(Locale.US, "%.3f", total) + " جنيه");
    }

    public void invoicePost() {
        int dealerBranchISN = 0;
        long dealerISN = 0;
        int dealerType = 0;
        int salesManBranchISN = 0;
        int salesManISN = 0;
        double deliveryValue = 0;
        double paidValue = 0;
        double remainValue = 0;
        long safeDepositBranchISN = 0;
        long safeDepositISN = 0;
        long bankBranchISN = 0;
        long bankISN = 0;
        String tableNum = "";
        String deliveryPhone = "";
        String deliveryAddress = "";
        double lineTax = 0;

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
        if (!binding.deliveryValue.getText().toString().isEmpty() && saleType == 2) {
            deliveryValue = Double.parseDouble(binding.deliveryValue.getText().toString());
        }
        if (!binding.sofraValue.getText().toString().isEmpty() && saleType == 3) {
            tableNum = binding.sofraValue.getText().toString();
        }
        if (paymentMethod == 0) {
            if (!binding.remaining.getText().toString().isEmpty())
                paidValue = Double.parseDouble(binding.remaining.getText().toString());
            remainValue = (totalAfterTax + totalLineTaxes) - paidValue;
        } else {
            paidValue = totalAfterTax;
            remainValue = 0;
        }
        if (safeDepositData.getSafeDepositName() != null) {
            safeDepositBranchISN = safeDepositData.getBranchISN();
            safeDepositISN = safeDepositData.getSafeDeposit_ISN();
        }
        if (banksCreditData.getBankName() != null) {
            bankBranchISN = banksCreditData.getBranchISN();
            bankISN = banksCreditData.getBank_ISN();
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
        ArrayList<Integer> allowStoreMinus = new ArrayList<>();
        ArrayList<String> productStoreName = new ArrayList<>();
        ArrayList<Double> illustrativeQuantity = new ArrayList<>();

        for (int i = 0; i < App.selectedProducts.size(); i++) {
            itemTax.add(Double.parseDouble(App.selectedProducts.get(i).getItemTax()));
            itemTaxValue.add(((App.selectedProducts.get(i).getSelectedUnit().getPrice() / 100) * Double.parseDouble(App.selectedProducts.get(i).getItemTax())) * App.selectedProducts.get(i).getActualQuantity());
            itemName.add(App.selectedProducts.get(i).getItemName());
            illustrativeQuantity.add(App.selectedProducts.get(i).getIllustrativeQuantity());
            discount1.add(App.selectedProducts.get(i).getDiscount1());
            allowStoreMinus.add(App.selectedProducts.get(i).getAllowStoreMinus());
            productStoreName.add(App.selectedProducts.get(i).getSelectedStore().getStoreName());
            lineTax = +Double.parseDouble(App.selectedProducts.get(i).getItemTax());
            ItemBranchISN.add((long) App.selectedProducts.get(i).getBranchISN());
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
            BonusQuantity.add(App.selectedProducts.get(i).getBonusQuantity());
            Price.add(App.selectedProducts.get(i).getPriceItem());
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
        }
        numberOFItems = App.selectedProducts.size();
        try {
            Log.e("checkInvoice", "invoice triggered");
            if (App.isNetworkAvailable(this)) {
                binding.checkout.setClickable(false);
                totalAfterTax += totalLineTaxes;
                isLoading = true;
                checkoutVM.placeInvoice(
                        App.currentUser.getBranchISN(), uuid, paymentMethod,
                        saleType, dealerType, dealerBranchISN, dealerISN, salesManBranchISN, salesManISN,
                        binding.notes.getText().toString(), Double.parseDouble(String.format(Locale.ENGLISH, "%.3f", productsTotal)),
                        Double.parseDouble(String.format(Locale.ENGLISH, "%.3f", serviceValue)),
                        Double.parseDouble(String.format(Locale.ENGLISH, "%.3f", servicePercent)),
                        Double.parseDouble(String.format(Locale.ENGLISH, "%.3f", deliveryValue)),
                        Double.parseDouble(String.format(Locale.ENGLISH, "%.3f", totalAfterService)),
                        Double.parseDouble(String.format(Locale.ENGLISH, "%.3f", discountValue)),
                        Double.parseDouble(String.format(Locale.ENGLISH, "%.3f", discountPercent)),
                        Double.parseDouble(String.format(Locale.ENGLISH, "%.3f", totalAfterDiscount)),
                        Double.parseDouble(String.format(Locale.ENGLISH, "%.3f", taxValue)),
                        Double.parseDouble(String.format(Locale.ENGLISH, "%.3f", taxPercent)),
                        Double.parseDouble(String.format(Locale.ENGLISH, "%.3f", totalAfterTax)),
                        Double.parseDouble(String.format(Locale.ENGLISH, "%.3f", totalAfterTax)),
                        Double.parseDouble(String.format(Locale.ENGLISH, "%.3f", paidValue)),
                        Double.parseDouble(String.format(Locale.ENGLISH, "%.3f", remainValue)),
                        safeDepositBranchISN, safeDepositISN, bankBranchISN, bankISN, tableNum,
                        deliveryPhone, deliveryAddress, App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN(), binding.cheqNumber.getText().toString(),
                        selectedDate, banksCheckData.getBranchISN(), banksCheckData.getBank_ISN(), ItemBranchISN, ItemISN, PriceTypeBranchISN, PriceTypeISN, StoreBranchISN, StoreISN,
                        BasicQuantity, BonusQuantity, TotalQuantity, Price, MeasureUnitBranchISN, MeasureUnitISN, BasicMeasureUnitBranchISN, BasicMeasureUnitISN, ItemSerial,
                        ExpireDate, ColorBranchISN, ColorISN, SizeBranchISN, SizeISN, SeasonBranchISN, SeasonISN, Group1BranchISN, Group1ISN, Group2BranchISN, Group2ISN, LineNotes, numberOFItems,
                        netPrices, basicMeasureUnitQuantity, expireDateBool, colorsBool, seasonsBool, sizesBool, serialBool, group1Bool, group2Bool, serviceItem, itemTax, itemTaxValue, totalLineTaxes,
                        App.currentUser.getAllowStoreMinus(), itemName, discount1, AllowStoreMinusConfirm, lat, _long, null, getMoveType(), null, null, null, allowStoreMinus, productStoreName,
                        illustrativeQuantity, customerDiscount );
            } else {
                isLoading = false;
                App.noConnectionDialog(this);
            }
        } catch (Exception e) {
            Log.e("ERRORRR!", String.valueOf(e));
        }
    }

    public boolean requiredData() {
        if (paymentMethod == 2) {
            if (binding.cheqNumber.getText().toString().isEmpty()) {
                requiredDataDialog("Cheque Number Required", getString(R.string.cheque_missing));
            } else if (selectedDate.isEmpty()) {
                requiredDataDialog("Cheque Date Required", getString(R.string.chequeDate_missing));
            } else if (banksCheckData.getBankISNBranch() == null) {
                requiredDataDialog("Cheque Number Required", getString(R.string.chequeBank_missing));
            } else {
                return true;
            }
        }
        if (!checkPermission()) {
            binding.progress.setVisibility(View.GONE);
            Log.e("permission", "permission needed");
            requestPermission();
            return false;
        }
        return true;
    }

    public void requiredDataDialog(String title, String message) {
        new AlertDialog.Builder(this).
                setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.ic_baseline_error_outline_24)
                .setNegativeButton("Ok", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }).show();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lat = (float) location.getLatitude();
        _long = (float) location.getLongitude();
    }

    //    @SuppressLint("SetTextI18n")
    private void calculateTotals() {
        if (isUpdatingField) return;
        isUpdatingField = true;
        updatePercentageFields();
        isUpdatingField = false;
    }
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not used
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals(".")) {
                calculateTotals();
            }
        }
    };
    @SuppressLint("SetTextI18n")
    private void updatePercentageFields() {
        if (binding.deliverCheck.isChecked() && binding.deliveryValue.hasFocus()) {
            if (!binding.deliveryValue.getText().toString().isEmpty()) {
                deliveryValue = Double.parseDouble(binding.deliveryValue.getText().toString());
            } else {
                deliveryValue = 0;
            }
            calculateService(false);
            calculateDiscount(true);
            calculateTax(true);
        }
        // Service
        if (!binding.percentServiceVal.getText().toString().isEmpty() && binding.percentServiceVal.hasFocus()) {
            if (Double.parseDouble(binding.percentServiceVal.getText().toString()) <= productsTotal) {
                calculateService(false);
                calculateDiscount(true);
                calculateTax(true);
                binding.checkout.setClickable(true);
            } else {
                setEditTextError(binding.percentServiceVal);
            }
        } else if (!binding.percentService.getText().toString().isEmpty() && binding.percentService.hasFocus()) {
            if (Double.parseDouble(binding.percentService.getText().toString()) <= 100) {
                calculateService(true);
                calculateDiscount(true);
                calculateTax(true);
                binding.checkout.setClickable(true);
            } else {
                setEditTextError(binding.percentService);
            }
        } else if (binding.percentServiceVal.hasFocus() || binding.percentService.hasFocus()) {
            resetServiceValue();
        }
        // Discount
        if (!binding.percentDiscountVal.getText().toString().isEmpty() && binding.percentDiscountVal.hasFocus()) {
            if (Double.parseDouble(binding.percentDiscountVal.getText().toString()) <= totalAfterService) {
                calculateDiscount(false);
                calculateTax(true);
            } else {
                setEditTextError(binding.percentDiscountVal);
            }
        } else if (!binding.percentDiscount.getText().toString().isEmpty() && binding.percentDiscount.hasFocus()) {
            if (Double.parseDouble(binding.percentDiscount.getText().toString()) <= 100) {
                calculateDiscount(true);
                calculateTax(true);
            } else {
                setEditTextError(binding.percentDiscount);
            }
        } else if (binding.percentDiscountVal.hasFocus() || binding.percentDiscount.hasFocus()) {
            binding.percentDiscountVal.setText("");
            binding.percentDiscount.setText("");
            discountValue = 0;
            totalAfterDiscount = productsTotal + serviceValue + deliveryValue;
            binding.totalDiscount.setText(String.format(Locale.US, "%.3f", totalAfterDiscount) + " جنيه");
            calculateTax(true);
        }

        // Tax
        if (!binding.percentTaxVal.getText().toString().isEmpty() && binding.percentTaxVal.hasFocus()) {
            if (Double.parseDouble(binding.percentTaxVal.getText().toString()) <= totalAfterDiscount) {
                calculateTax(false);
            } else {
                setEditTextError(binding.percentTaxVal);
            }
        } else if (!binding.percentTax.getText().toString().isEmpty() && binding.percentTax.hasFocus()) {
            if (Double.parseDouble(binding.percentTax.getText().toString()) <= 100) {
                calculateTax(true);
            } else {
                setEditTextError(binding.percentTax);
            }
        } else if (binding.percentTaxVal.hasFocus() || binding.percentTax.hasFocus()) {
            binding.percentTaxVal.setText("");
            binding.percentTax.setText("");
            taxValue = 0;
            totalAfterTax = productsTotal + serviceValue + deliveryValue - discountValue;
            binding.totalTax.setText(String.format(Locale.US, "%.3f", totalAfterTax) + " جنيه");
        }
        updateTotals();
    }

    @SuppressLint("SetTextI18n")
    private void calculateService(boolean isPercentage) {
        if (isPercentage) {
            if (!binding.percentService.getText().toString().isEmpty()) {
                binding.percentServiceVal.removeTextChangedListener(textWatcher);
                double servicePercentage = Double.parseDouble(binding.percentService.getText().toString());
                serviceValue = (productsTotal * servicePercentage) / 100;
                servicePercent = servicePercentage;
                binding.percentServiceVal.setText(String.format(Locale.US, "%.2f", serviceValue));
                binding.percentServiceVal.addTextChangedListener(textWatcher);
            }
        } else {
            if (!binding.percentServiceVal.getText().toString().isEmpty()) {
                binding.percentService.removeTextChangedListener(textWatcher);
                serviceValue = Double.parseDouble(binding.percentServiceVal.getText().toString());
                double servicePercentage = (serviceValue / productsTotal) * 100;
                servicePercent = servicePercentage;
                binding.percentService.setText(String.format(Locale.US, "%.2f", servicePercentage));
                binding.percentService.addTextChangedListener(textWatcher);
            }
        }
        totalAfterService = productsTotal + serviceValue + deliveryValue;
        binding.totalService.setText(String.format(Locale.US, "%.3f", totalAfterService) + " جنيه");
    }

    @SuppressLint("SetTextI18n")
    private void calculateDiscount(boolean isPercentage) {
        double currentTotal = productsTotal + serviceValue + deliveryValue;
        if (isPercentage) {
            if (!binding.percentDiscount.getText().toString().isEmpty()) {
                binding.percentDiscountVal.removeTextChangedListener(textWatcher); //
                double discountPercentage = Double.parseDouble(binding.percentDiscount.getText().toString());
                discountValue = (currentTotal * discountPercentage) / 100;
                discountPercent = discountPercentage;
                binding.percentDiscountVal.setText(String.format(Locale.US, "%.2f", discountValue));
                binding.percentDiscountVal.addTextChangedListener(textWatcher);
            }
        } else {
            if (!binding.percentDiscountVal.getText().toString().isEmpty()) {
                binding.percentDiscount.removeTextChangedListener(textWatcher);
                discountValue = Double.parseDouble(binding.percentDiscountVal.getText().toString());
                double discountPercentage = (discountValue / currentTotal) * 100;
                discountPercent = discountPercentage;
                binding.percentDiscount.setText(String.format(Locale.US, "%.2f", discountPercentage));
                binding.percentDiscount.addTextChangedListener(textWatcher);
            }
        }
        totalAfterDiscount = currentTotal - discountValue;
        binding.totalDiscount.setText(String.format(Locale.US, "%.3f", totalAfterDiscount) + " جنيه");
    }

    @SuppressLint("SetTextI18n")
    private void calculateTax(boolean isPercentage) {
        double currentTotal = productsTotal + serviceValue + deliveryValue - discountValue;
        if (isPercentage) {
            if (!binding.percentTax.getText().toString().isEmpty()) {
                binding.percentTaxVal.removeTextChangedListener(textWatcher);
                double taxPercentage = Double.parseDouble(binding.percentTax.getText().toString());
                taxValue = (currentTotal * taxPercentage) / 100;
                taxPercent = taxPercentage;
                binding.percentTaxVal.setText(String.format(Locale.US, "%.2f", taxValue));
                binding.percentTaxVal.addTextChangedListener(textWatcher); //
            }
        } else {
            if (!binding.percentTaxVal.getText().toString().isEmpty()) {
                binding.percentTax.removeTextChangedListener(textWatcher);
                taxValue = Double.parseDouble(binding.percentTaxVal.getText().toString());
                double taxPercentage = (taxValue / currentTotal) * 100;
                taxPercent = taxPercentage;
                binding.percentTax.setText(String.format(Locale.US, "%.2f", taxPercentage));
                binding.percentTax.addTextChangedListener(textWatcher); //
            }
        }
        totalAfterTax = currentTotal + taxValue;
        binding.totalTax.setText(String.format(Locale.US, "%.3f", totalAfterTax) + " جنيه");
    }

    private void resetServiceValue() {
        binding.percentServiceVal.setText("");
        binding.percentService.setText("");
        serviceValue = 0;
        servicePercent = 0;
        totalAfterService = productsTotal + deliveryValue;
        binding.totalService.setText(String.format(Locale.US, "%.3f", totalAfterService) + " جنيه");
        calculateDiscount(true);
        calculateTax(true);
    }

    private void updateTotals() {
        double grandTotal = productsTotal + deliveryValue + serviceValue - discountValue + taxValue + totalLineTaxes;
        double remainingBalance = (totalAfterTax + totalLineTaxes);
        if (paymentMethod == 0 && !binding.remaining.getText().toString().isEmpty()) {
            if (Double.parseDouble(binding.remaining.getText().toString()) < (totalAfterTax + totalLineTaxes)) {
                remainingBalance = (totalAfterTax + totalLineTaxes) - Double.parseDouble(binding.remaining.getText().toString());
            } else {
                setEditTextError(binding.remaining);
            }
        }
        binding.totalItems.setText(String.format(Locale.US, "%.3f", productsTotal) + " جنيه");
        binding.totalTax2.setText(String.format(Locale.ENGLISH, "%.3f", totalLineTaxes));
        binding.finalTotal.setText(String.format(Locale.US, "%.3f", grandTotal) + " جنيه");
        binding.remainedCash.setText(String.format(Locale.US, "%.3f", remainingBalance) + " جنيه");
    }

    private void setEditTextError(EditText editText) {
        editText.setError("رقم غير مقبول");
        editText.setText("");
        binding.checkout.setClickable(false);
    }

}


