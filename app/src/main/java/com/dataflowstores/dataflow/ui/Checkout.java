package com.dataflowstores.dataflow.ui;

import static com.dataflowstores.dataflow.App.getMoveType;
import static com.dataflowstores.dataflow.App.theme;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ViewModels.CheckoutVM;
import com.dataflowstores.dataflow.databinding.CheckoutBinding;
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

public class Checkout extends BaseActivity implements View.OnFocusChangeListener, LocationListener {
    private static final int REQUEST_CHECK_SETTINGS = 111;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    CheckoutBinding binding;
    int paymentMethod = 0;
    int saleType = 1;
    Calendar myCalendar = Calendar.getInstance();
    boolean dateValidation = false;
    SafeDepositData safeDepositData = new SafeDepositData();
    BanksData banksCheckData = new BanksData();
    BanksData banksCreditData = new BanksData();
    double totalLineTaxes = 0;
    double linesTaxes = 0;
    double productsTotal = 0;
    int numberOfItems = 0;
    DecimalFormat df = new DecimalFormat("#.##");
    double totalAfterService = 0;
    double totalAfterDiscount = 0;
    float totalAfterTax = 0;
    CheckoutVM checkoutVM;
    String uuid;
    float serviceValue = 0;
    float servicePercent = 0;
    float discountValue = 0;
    float discountPercent = 0;
    float taxValue = 0;
    float taxPercent = 0;
    String paymentMethodText = "";
    String selectedDate = "";
    Integer AllowStoreMinusConfirm = 0;
    DecimalFormat form = new DecimalFormat("0.00");
    TextWatcher servicePer, serviceVal, discountPer, discountVal, taxPer, taxVal, deliveryVal;
    float lat = 0;
    float _long = 0;
    String customerBalance = "";
    boolean isLoading = false;

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
            remaining();
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
                // All location settings are satisfied. The client can initialize location
                // requests here.+
//             ...
            } catch (ApiException exception) {
                switch (exception.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            ResolvableApiException resolvable = (ResolvableApiException) exception;
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                    this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        } catch (ClassCastException e) {
                            // Ignore, should be an impossible error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
//                     ...
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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
        if (App.currentUser.getMobileDiscount() == 0) {
            binding.discountContainer.setVisibility(View.GONE);
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
        binding.remainedCash.setVisibility(View.VISIBLE);//todo: update based on Paid Amount
        binding.textView110.setVisibility(View.VISIBLE);
        binding.cheqContainer.setVisibility(View.GONE);
        binding.creditBankCon.setVisibility(View.GONE);

        remaining();
        safeDepositSpinner();
    }

    public void cashCheck(View view) {
        binding.cheqCheck.setChecked(false);
        binding.creditCheck.setChecked(false);
        binding.laterCheck.setChecked(false);
        paymentMethod = 1;
        paymentMethodText = "Cash";
        Log.e("check", "cashChecked");
        safeDepositSpinner();
        binding.agelTxt.setTextColor(getResources().getColor(R.color.black));
        binding.cashTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        binding.creditTxt.setTextColor(getResources().getColor(R.color.black));
        binding.checkTxt.setTextColor(getResources().getColor(R.color.black));
        binding.laterContainer.setVisibility(View.VISIBLE);
        binding.remainingContainer.setVisibility(View.GONE);
        binding.remainedCash.setVisibility(View.GONE);
        //todo: update based on Paid Amount
        binding.textView110.setVisibility(View.GONE);
        binding.cheqContainer.setVisibility(View.GONE);
        binding.creditBankCon.setVisibility(View.GONE);
        binding.remaining.setVisibility(View.GONE);
    }

    public void cheqCheck(View view) {
        binding.cashCheck.setChecked(false);
        binding.creditCheck.setChecked(false);
        binding.laterCheck.setChecked(false);
        Log.e("check", "Cheqchecked");
        paymentMethodText = "Cheque";
        binding.agelTxt.setTextColor(getResources().getColor(R.color.black));
        binding.cashTxt.setTextColor(getResources().getColor(R.color.black));
        binding.creditTxt.setTextColor(getResources().getColor(R.color.black));
        binding.checkTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        paymentMethod = 2;
        binding.cheqContainer.setVisibility(View.VISIBLE);
        binding.remainingContainer.setVisibility(View.GONE);
        binding.remainedCash.setVisibility(View.GONE);//todo: update based on Paid Amount
        binding.textView110.setVisibility(View.GONE);
        binding.laterContainer.setVisibility(View.GONE);
        binding.laterContainer.setVisibility(View.GONE);
        binding.creditBankCon.setVisibility(View.GONE);
        binding.remaining.setVisibility(View.GONE);
        datePicker();
        bankSpinner();
        //todo: getText from cheq Number EditText
    }

    public void creditCheck(View view) {
        binding.cheqCheck.setChecked(false);
        binding.cashCheck.setChecked(false);
        binding.laterCheck.setChecked(false);
        Log.e("check", "creditchecked");
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
        binding.extraDiscountCon.setVisibility(View.GONE);
        serviceValue = 0;
        servicePercent = 0;
        totalAfterService = productsTotal;
    }

    public void externalCheck(View view) {
        binding.deliverCheck.setChecked(false);
        binding.sofraCheck.setChecked(false);
        binding.deliveryTxt.setTextColor(getResources().getColor(R.color.black));
        binding.externalCheck.setTextColor(getResources().getColor(R.color.colorPrimary));
        totalAfterService = productsTotal;
        servicePercent = 0;
        serviceValue = 0;
        binding.deliveryValue.setText("0");
        binding.sofraTxt.setTextColor(getResources().getColor(R.color.black));
        saleType = 1;
        binding.deliveryContainer.setVisibility(View.GONE);
        binding.extraDiscountCon.setVisibility(View.VISIBLE);
    }

    public void tableCheck(View view) {
        binding.deliverCheck.setChecked(false);
        binding.externalCheck.setChecked(false);
        binding.deliveryTxt.setTextColor(getResources().getColor(R.color.black));
        binding.externalCheck.setTextColor(getResources().getColor(R.color.black));
        binding.sofraTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        saleType = 3;
        servicePercent = 0;
        serviceValue = 0;
        binding.deliveryValue.setText("0");
        binding.deliveryContainer.setVisibility(View.VISIBLE);
        binding.sofraValue.setVisibility(View.VISIBLE);
        binding.deliveryValue.setVisibility(View.GONE);
        binding.textView30.setText("رقم السفرة");
        binding.deliveryValue.setHint("أدخل رقم السفرة");
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
//        productsTotal += totalLineTaxes;
        binding.finalTotal.setText(String.format(Locale.US, "%.3f", productsTotal + totalLineTaxes) + " جنيه");
        binding.remainedCash.setText(String.format(Locale.US, "%.3f", productsTotal + totalLineTaxes) + " جنيه");
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
        binding.percentService.setOnFocusChangeListener(this);
        binding.percentServiceVal.setOnFocusChangeListener(this);
        binding.percentDiscount.setOnFocusChangeListener(this);
        binding.percentDiscountVal.setOnFocusChangeListener(this);
        binding.percentTax.setOnFocusChangeListener(this);
        binding.percentTaxVal.setOnFocusChangeListener(this);
        binding.deliveryValue.setOnFocusChangeListener(this);
        totalAfterService = productsTotal;
        totalAfterDiscount = productsTotal;
        totalAfterTax = (float) productsTotal;

        setTotal(productsTotal);
        delivery();
        service();
        discount();
        tax();
    }

    public void delivery() {
        Log.e("checkWatcher", "here 1");
        deliveryVal = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty() && !charSequence.toString().equals(".")) {
                    double value = (Double.parseDouble(binding.deliveryValue.getText().toString()) / productsTotal);
                    binding.percentService.setText(String.format(Locale.US, "%.3f", value * 100) + "");
                    totalAfterService = productsTotal + Double.parseDouble(binding.deliveryValue.getText().toString());
                    servicePercent = 0;
                    serviceValue = 0;
                } else {
                    binding.percentService.setText(0 + "");
                    totalAfterService = productsTotal;
                    servicePercent = 0;
                    serviceValue = 0;
                }
                totalAfterDiscount = totalAfterService;
                totalAfterTax = (float) totalAfterDiscount;
                binding.totalService.setText(String.format(Locale.US, "%.3f", (totalAfterService)) + " جنيه");
                binding.totalDiscount.setText(String.format(Locale.US, "%.3f", (totalAfterService)) + " جنيه");
                binding.totalTax.setText(String.format(Locale.US, "%.3f", (totalAfterService)) + " جنيه");
                binding.finalTotal.setText(String.format(Locale.US, "%.3f", (totalAfterService + totalLineTaxes)) + " جنيه");
                binding.remainedCash.setText(String.format(Locale.US, "%.3f", (totalAfterService + totalLineTaxes)) + " جنيه");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }

    public void service() {
        servicePer = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty() && !charSequence.toString().equals(".")) {
                    if (Double.parseDouble(charSequence.toString()) <= 100) {
                        double value = (productsTotal / 100) * Double.parseDouble(binding.percentService.getText().toString());
                        binding.percentServiceVal.setText(String.format(Locale.US, "%.3f", value) + "");
                        totalAfterService = productsTotal + value;
                        serviceValue = (float) value;
                        double value1 = serviceValue / productsTotal;
                        servicePercent = (float) (value1 * 100);
                        binding.checkout.setClickable(true);
                    } else {
                        binding.percentService.setError("ERROR!");
                        binding.percentService.setError("");
                        binding.checkout.setClickable(false);
                    }
                } else {
                    binding.percentServiceVal.setText(0 + "");
                    totalAfterService = productsTotal;
                    serviceValue = 0;
                }
                totalAfterDiscount = totalAfterService;
                totalAfterTax = (float) totalAfterDiscount;
                binding.totalService.setText(String.format(Locale.US, "%.3f", totalAfterService) + " جنيه");
                binding.totalDiscount.setText(String.format(Locale.US, "%.3f", totalAfterService) + " جنيه");
                binding.totalTax.setText(String.format(Locale.US, "%.3f", totalAfterService) + " جنيه");
                binding.finalTotal.setText(String.format(Locale.US, "%.3f", totalAfterService + totalLineTaxes) + " جنيه");
                binding.remainedCash.setText(String.format(Locale.US, "%.3f", totalAfterService + totalLineTaxes) + " جنيه");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        serviceVal = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty() && !charSequence.toString().equals(".")) {
                    if (Double.parseDouble(charSequence.toString()) <= productsTotal) {
                        double value = (Double.parseDouble(binding.percentServiceVal.getText().toString()) / productsTotal);
                        double value1 = (productsTotal / 100) * value * 100;
                        binding.percentService.setText(String.format(Locale.US, "%.3f", value * 100) + "");
                        totalAfterService = productsTotal + Double.parseDouble(binding.percentServiceVal.getText().toString());
                        servicePercent = (float) (value * 100);
                        serviceValue = (float) value1;
                        binding.checkout.setClickable(true);
                    } else {
                        binding.percentServiceVal.setError("ERROR!");
                        binding.percentServiceVal.setError("");
                        binding.checkout.setClickable(false);
                    }
                } else {
                    binding.percentService.setText(0 + "");
                    totalAfterService = productsTotal;
                    servicePercent = 0;
                }
                totalAfterDiscount = totalAfterService;
                totalAfterTax = (float) totalAfterDiscount;
                binding.totalService.setText(String.format(Locale.US, "%.3f", totalAfterService) + " جنيه");
                binding.totalDiscount.setText(String.format(Locale.US, "%.3f", totalAfterService) + " جنيه");
                binding.totalTax.setText(String.format(Locale.US, "%.3f", totalAfterService) + " جنيه");
                binding.finalTotal.setText(String.format(Locale.US, "%.3f", totalAfterService + totalLineTaxes) + " جنيه");
                binding.remainedCash.setText(String.format(Locale.US, "%.3f", totalAfterService + totalLineTaxes) + " جنيه");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }

    public void discount() {
        discountPer = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty() && !charSequence.toString().equals(".")) {
                    if (Double.parseDouble(charSequence.toString()) <= 100) {
                        double value = (totalAfterService / 100) * Double.parseDouble(binding.percentDiscount.getText().toString());
                        double value1 = value / totalAfterService;
                        binding.percentDiscountVal.setText(String.format(Locale.US, "%.3f", value) + "");
                        totalAfterDiscount = totalAfterService - value;
                        discountValue = (float) value;
                        discountPercent = (float) value1 * 100;
                        binding.checkout.setClickable(true);
                    } else {
                        binding.percentDiscount.setError("ERROR!");
                        binding.percentDiscount.setError("");
                        binding.checkout.setClickable(false);
                    }
                } else {
                    binding.percentDiscountVal.setText(0 + "");
                    discountValue = 0;
                    discountPercent = 0;
                    totalAfterDiscount = totalAfterService;
                }
                ;
                totalAfterTax = (float) totalAfterDiscount;
                binding.totalDiscount.setText(String.format(Locale.US, "%.3f", totalAfterDiscount) + " جنيه");
                binding.totalTax.setText(String.format(Locale.US, "%.3f", totalAfterDiscount) + " جنيه");
                binding.finalTotal.setText(String.format(Locale.US, "%.3f", totalAfterDiscount + totalLineTaxes) + " جنيه");
                binding.remainedCash.setText(String.format(Locale.US, "%.3f", totalAfterDiscount + totalLineTaxes) + " جنيه");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        discountVal = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty() && !charSequence.toString().equals(".")) {
                    if (Double.parseDouble(charSequence.toString()) <= totalAfterService) {
                        double value = Double.parseDouble(binding.percentDiscountVal.getText().toString()) / totalAfterService;
                        binding.percentDiscount.setText(String.format(Locale.US, "%.3f", value * 100) + "");
                        totalAfterDiscount = totalAfterService - Double.parseDouble(binding.percentDiscountVal.getText().toString());
                        double value1 = (totalAfterService / 100) * value;
                        discountValue = (float) value1;
                        discountPercent = (float) (value * 100);
                        binding.checkout.setClickable(true);
                    } else {
                        binding.percentDiscountVal.setError("ERROR!");
                        binding.percentDiscountVal.setError("");
                        binding.checkout.setClickable(false);
                    }
                } else {
                    binding.percentDiscount.setText(0 + "");
                    totalAfterDiscount = totalAfterService;
                    discountPercent = 0;
                    discountValue = 0;
                }
                totalAfterTax = (float) totalAfterDiscount;
                binding.totalDiscount.setText(String.format(Locale.US, "%.3f", totalAfterDiscount) + " جنيه");
                binding.totalTax.setText(String.format(Locale.US, "%.3f", totalAfterDiscount) + " جنيه");
                binding.finalTotal.setText(String.format(Locale.US, "%.3f", totalAfterDiscount + totalLineTaxes) + " جنيه");
                binding.remainedCash.setText(String.format(Locale.US, "%.3f", totalAfterDiscount + totalLineTaxes) + " جنيه");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }

    public void tax() {
        taxPer = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty() && !charSequence.toString().equals(".")) {
                    if (Double.parseDouble(charSequence.toString()) <= 100) {
                        double value = (totalAfterDiscount / 100) * Double.parseDouble(binding.percentTax.getText().toString());
                        double value1 = value / totalAfterDiscount;
                        binding.percentTaxVal.setText(String.format(Locale.US, "%.3f", value) + "");
                        totalAfterTax = (float) (totalAfterDiscount + value);
                        taxValue = (float) value;
                        taxPercent = (float) (value1 * 100);
                        binding.checkout.setClickable(true);
                    } else {
                        binding.percentTax.setError("ERROR!");
                        binding.percentTax.setError("");
                        binding.checkout.setClickable(false);
                    }
                } else {
                    binding.percentTaxVal.setText(0 + "");
                    totalAfterTax = (float) totalAfterDiscount;
                    taxValue = 0;
                    taxPercent = 0;
                }
                binding.totalTax.setText(String.format(Locale.US, "%.3f", totalAfterTax) + " جنيه");
                binding.finalTotal.setText(String.format(Locale.US, "%.3f", totalAfterTax + totalLineTaxes) + " جنيه");
                binding.remainedCash.setText(String.format(Locale.US, "%.3f", totalAfterTax + totalLineTaxes) + " جنيه");


            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        taxVal = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty() && !charSequence.toString().equals(".")) {
                    if (Double.parseDouble(charSequence.toString()) <= totalAfterDiscount) {
                        double value = Double.parseDouble(binding.percentTaxVal.getText().toString()) / totalAfterDiscount;
                        double value1 = (totalAfterDiscount / 100) * value;
                        binding.percentTax.setText(String.format(Locale.US, "%.3f", value * 100) + "");
                        totalAfterTax = (float) (totalAfterDiscount + Double.parseDouble(binding.percentTaxVal.getText().toString()));
                        taxValue = (float) value1;
                        taxPercent = (float) (value * 100);
                        binding.checkout.setClickable(true);
                    } else {
                        binding.percentTaxVal.setError("ERROR!");
                        binding.percentTaxVal.setText("");
                        binding.checkout.setClickable(false);
                    }
                } else {
                    binding.percentTax.setText(0 + "");
                    totalAfterTax = (float) totalAfterDiscount;
                    taxPercent = 0;
                    taxValue = 0;
                }
                binding.totalTax.setText(String.format(Locale.US, "%.3f", totalAfterTax) + " جنيه");
                binding.finalTotal.setText(String.format(Locale.US, "%.3f", totalAfterTax + totalLineTaxes) + " جنيه");
                binding.remainedCash.setText(String.format(Locale.US, "%.3f", totalAfterTax + totalLineTaxes) + " جنيه");

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }

    @SuppressLint("SetTextI18n")
    public void setTotal(double total) {
        binding.totalService.setText(String.format(Locale.US, "%.3f", total) + " جنيه");
        binding.totalDiscount.setText(String.format(Locale.US, "%.3f", total) + " جنيه");
        binding.totalTax.setText(String.format(Locale.US, "%.3f", total) + " جنيه");
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (view.equals(binding.deliveryValue)) {
            if (hasFocus)
                binding.deliveryValue.addTextChangedListener(deliveryVal);
            else {
                binding.percentService.removeTextChangedListener(deliveryVal);
            }
        }
        if (view.equals(binding.percentService)) {
            if (hasFocus) {
                binding.percentService.addTextChangedListener(servicePer);
            } else {
                binding.percentService.removeTextChangedListener(servicePer);
            }
        }
        if (view.equals(binding.percentServiceVal)) {
            if (hasFocus) {
                binding.percentServiceVal.addTextChangedListener(serviceVal);
            } else {
                binding.percentServiceVal.removeTextChangedListener(serviceVal);
            }
        }
        if (view.equals(binding.percentDiscount)) {
            if (hasFocus) {
                binding.percentDiscount.addTextChangedListener(discountPer);
            } else {
                binding.percentDiscount.removeTextChangedListener(discountPer);
            }
        }
        if (view.equals(binding.percentDiscountVal)) {
            if (hasFocus) {
                binding.percentDiscountVal.addTextChangedListener(discountVal);
            } else {
                binding.percentDiscountVal.removeTextChangedListener(discountVal);
            }
        }
        if (view.equals(binding.percentTax)) {
            if (hasFocus) {
                binding.percentTax.addTextChangedListener(taxPer);
            } else {
                binding.percentTax.removeTextChangedListener(taxPer);
            }
        }
        if (view.equals(binding.percentTaxVal)) {
            if (hasFocus) {
                binding.percentTaxVal.addTextChangedListener(taxVal);
            } else {
                binding.percentTaxVal.removeTextChangedListener(taxVal);
            }
        }

    }

    public void remaining() {
        binding.remaining.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty() && !charSequence.toString().equals("."))
                    //TODO:: Come here.
                    if (Double.parseDouble(binding.remaining.getText().toString()) < (totalAfterTax + totalLineTaxes))
                        binding.remainedCash.setText(String.format(Locale.US, "%.3f", (totalAfterTax + totalLineTaxes) - Double.parseDouble(binding.remaining.getText().toString())) + " جنيه");
                    else {
                        binding.remaining.setError("مبلغ غير مقبول!");
                        binding.remaining.setText("");
                    }
                else
                    binding.remainedCash.setText(String.format(Locale.US, "%.3f", totalAfterTax + totalLineTaxes) + " جنيه");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
        numberOFItems = ((long) App.selectedProducts.size());
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
                        illustrativeQuantity);
                Log.e("checkout", " checkinnnggg");
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
}
