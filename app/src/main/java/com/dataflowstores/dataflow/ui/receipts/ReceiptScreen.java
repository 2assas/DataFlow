package com.dataflowstores.dataflow.ui.receipts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.ViewModels.InvoiceViewModel;
import com.dataflowstores.dataflow.ViewModels.ReceiptsVM;
import com.dataflowstores.dataflow.pojo.settings.BanksData;
import com.dataflowstores.dataflow.pojo.settings.SafeDepositData;
import com.dataflowstores.dataflow.pojo.users.CustomerData;
import com.dataflowstores.dataflow.pojo.users.SalesManData;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.fragments.BottomSheetFragment;
import com.dataflowstores.dataflow.utils.SingleShotLocationProvider;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.ReceiptsBinding;
import com.dataflowstores.dataflow.ui.listeners.MyDialogCloseListener;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ReceiptScreen extends AppCompatActivity implements MyDialogCloseListener, LocationListener {
    ReceiptsBinding binding;
    InvoiceViewModel invoiceVM;
    SafeDepositData safeDepositData = new SafeDepositData();
    BanksData banksCheckData = new BanksData();
    BanksData banksCreditData = new BanksData();
    Calendar myCalendar = Calendar.getInstance();
    boolean dateValidation = false;
    int paymentMethod = 0;
    String paymentMethodText = "";
    String selectedDate = "";
    String uuid;
    private static final int REQUEST_CHECK_SETTINGS = 111;
    ReceiptsVM receiptsVM;
    float lat = 0;
    float _long = 0;
    private long moveId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.receipts);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else {
            receiptsVM = new ViewModelProvider(this).get(ReceiptsVM.class);
            uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            userRestricts();
            setupViews();
            checkboxes();
            searchButtons();
        }
    }

    @SuppressLint("HardwareIds")
    private void setupViews() {
        invoiceVM = new ViewModelProvider(this).get(InvoiceViewModel.class);
        uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        App.customer = new CustomerData();
        App.agent = new SalesManData();
        App.selectedProducts = new ArrayList<>();
        binding.salesNameCheck.setChecked(true);
        binding.cashCheck.setChecked(true);
        binding.cashCheck.performClick();
        binding.showProgress.setVisibility(View.GONE);
        if (checkPermission()) getLocation(this);
        else requestPermission();
        invoiceVM.customerBalanceLiveData.observe(this, customerBalance -> {
            App.customerBalance = customerBalance.getMessage();
            new AlertDialog.Builder(this).setTitle("عملية ناجحة")
                    .setMessage("تم تسجيل عملية الدفع بنجاح")
                    .setIcon(getResources().getDrawable(R.drawable.ic_baseline_verified_24))
                    .setPositiveButton("طباعة", (dialog, which) -> {
                        binding.confirmProcess.setClickable(true);
                        receiptsVM.getReceipt(App.currentUser.getBranchISN(), uuid, String.valueOf(moveId),
                                App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN(), App.currentUser.getPermission());
                        binding.showProgress.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }).setNegativeButton("إغلاق", (dialog, which) -> {
                        dialog.dismiss();
                        finish();
                        startActivity(new Intent(this, ReceiptScreen.class));
                    }).show();


        });
    }

    public void userRestricts() {
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
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void confirmProcess(View view) {
        if (validateData()) {
            if (lat != 0 || _long != 0) {
                binding.confirmProcess.setClickable(false);
                createReceipt();
            } else {
                new AlertDialog.Builder(this).
                        setTitle("الموقع مطلوب")
                        .setMessage("لإتمام العملية يرجى السماح لإذن أخذ الموقع الحالى للجهاز")
                        .setPositiveButton("حسنا", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            requestPermission();
                            settingsRequest();
                        }).show();
            }
        }
    }

    private boolean validateData() {
        if (binding.getClient.getText().toString().isEmpty() || App.customer.getDealerName() == null) {
            binding.getClient.setError("إسم العميل مطلوب");
            return false;
        } else if (binding.salesNameCheck.isChecked() && App.agent.getDealerName() == null) {
            new AlertDialog.Builder(this).
                    setTitle("إختر مندوب")
                    .setMessage("من فضلك اختر إسم المندوب او اختار بدون مندوب")
                    .setPositiveButton("حسنا", (dialogInterface, i) -> dialogInterface.dismiss())
                    .setNegativeButton("بدون مندوب", (dialogInterface, i) -> binding.salesNameCheck.setChecked(false)).show();
            return false;
        } else if (binding.receiptTotal.getText().toString().isEmpty()) {
            binding.receiptTotal.setError("لابد من إدخال المبلغ المدفوع");
            return false;
        } else {
            return true;
        }
    }

    private void checkboxes() {
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
        binding.back.setOnClickListener(view -> finish());
    }

    private void searchButtons() {
        binding.searchAgent.setOnClickListener(view -> {
            if (App.isNetworkAvailable(this))
                invoiceVM.getSalesMan(uuid, binding.getAgent.getText().toString(), App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN());
            else {
                App.noConnectionDialog(this);
            }
            BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        });
        binding.searchClient.setOnClickListener(view -> {
            if (App.isNetworkAvailable(this))
                invoiceVM.getCustomer(uuid, binding.getClient.getText().toString(), App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN());
            else {
                App.noConnectionDialog(this);
            }
            BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        });
    }

    public void cashCheck(View view) {
        binding.cheqCheck.setChecked(false);
        binding.creditCheck.setChecked(false);
        paymentMethod = 1;
        paymentMethodText = "Cash";
        Log.e("check", "cashChecked");
        if (App.safeDeposit.getData() != null)
            safeDepositSpinner();
        binding.cashTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        binding.creditTxt.setTextColor(getResources().getColor(R.color.black));
        binding.checkTxt.setTextColor(getResources().getColor(R.color.black));
        binding.laterContainer.setVisibility(View.VISIBLE);
        //todo: update based on Paid Amount
        binding.cheqContainer.setVisibility(View.GONE);
        binding.creditBankCon.setVisibility(View.GONE);
    }

    public void chequeCheck(View view) {
        binding.cashCheck.setChecked(false);
        binding.creditCheck.setChecked(false);
        Log.e("check", "Cheqchecked");
        paymentMethodText = "Cheque";
        binding.cashTxt.setTextColor(getResources().getColor(R.color.black));
        binding.creditTxt.setTextColor(getResources().getColor(R.color.black));
        binding.checkTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        paymentMethod = 2;
        binding.cheqContainer.setVisibility(View.VISIBLE);
        binding.laterContainer.setVisibility(View.GONE);
        binding.laterContainer.setVisibility(View.GONE);
        binding.creditBankCon.setVisibility(View.GONE);
        datePicker();
        bankSpinner();
        //todo: getText from cheq Number EditText
    }

    public void creditCheck(View view) {
        binding.cheqCheck.setChecked(false);
        binding.cashCheck.setChecked(false);
        Log.e("check", "creditchecked");
        paymentMethodText = "Credit";
        paymentMethod = 3;
        binding.cashTxt.setTextColor(getResources().getColor(R.color.black));
        binding.creditTxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        binding.checkTxt.setTextColor(getResources().getColor(R.color.black));
        binding.creditBankCon.setVisibility(View.VISIBLE);
        binding.cheqContainer.setVisibility(View.GONE);
        binding.laterContainer.setVisibility(View.GONE);
        creditBankSpinner();
    }

    private void safeDepositSpinner() {
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
        for (int i = 0; i < App.safeDeposit.getData().size(); i++) {
            if (App.currentUser.getSafeDepositISN() == App.safeDeposit.getData().get(i).getSafeDeposit_ISN()
                    && App.currentUser.getSafeDepositBranchISN() == App.safeDeposit.getData().get(i).getBranchISN())
                binding.safeDepositSpinner.setSelection(i);
        }
    }

    private void bankSpinner() {
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

    private void creditBankSpinner() {
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

    private void datePicker() {
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

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        if (App.customer.getDealerName() != null) {
            binding.getClient.setText(App.customer.getDealerName());
        }
        if (App.agent.getDealerName() != null) {
            binding.getAgent.setText(App.agent.getDealerName());
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
                        getLocation(ReceiptScreen.this);
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
                        new android.app.AlertDialog.Builder(this).setTitle("الموقع مطلوب")
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


    @Override
    public void onLocationChanged(@NonNull Location location) {
        lat = (float) location.getLatitude();
        _long = (float) location.getLongitude();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void createReceipt() {
        int salesManBranchISN = 0;
        int salesManISN = 0;
        double total = 0;
        if (App.agent.getDealerName() != null) {
            salesManBranchISN = App.agent.getBranchISN();
            salesManISN = App.agent.getDealer_ISN();
        }
        total = Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", Float.parseFloat(binding.receiptTotal.getText().toString())));

        receiptsVM.createReceipt(App.currentUser.getBranchISN(), uuid, paymentMethod,
                0, App.customer.getDealerType(), App.customer.getBranchISN(), App.customer.getDealer_ISN(), salesManBranchISN, salesManISN,
                binding.receiptNotes.getText().toString(), Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", total)),
                0, 0, 0, total, 0, 0, total, 0, 0,
                total, total, 0, total,
                safeDepositData.getBranchISN(), safeDepositData.getSafeDeposit_ISN(), banksCreditData.getBranchISN(), banksCreditData.getBank_ISN(), null,
                null, null, App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN(), binding.cheqNumber.getText().toString(),
                selectedDate, banksCheckData.getBranchISN(), banksCheckData.getBank_ISN(), 2, lat, _long);

        receiptsVM.receiptResponseMutableLiveData.observe(this, receiptResponse -> {
            if (App.customer.getDealerName() != null && App.currentUser.getMobileShowDealerCurrentBalanceInPrint() == 1) {
                moveId = receiptResponse.getData().getMoveId();
                invoiceVM.getCustomerBalance(uuid, String.valueOf(App.customer.getDealer_ISN()), String.valueOf(App.customer.getBranchISN()), String.valueOf(App.customer.getDealerType()), String.valueOf(App.customer.getDealerName()));
            } else {
                new AlertDialog.Builder(this).setTitle("عملية ناجحة")
                        .setMessage("تم تسجيل عملية الدفع بنجاح")
                        .setIcon(getResources().getDrawable(R.drawable.ic_baseline_verified_24))
                        .setPositiveButton("طباعة", (dialog, which) -> {
                            binding.confirmProcess.setClickable(true);
                            receiptsVM.getReceipt(App.currentUser.getBranchISN(), uuid, String.valueOf(receiptResponse.getData().getMoveId()),
                                    App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN(), App.currentUser.getPermission());
                            binding.showProgress.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        }).setNegativeButton("إغلاق", (dialog, which) -> {
                            dialog.dismiss();
                            finish();
                            startActivity(new Intent(this, ReceiptScreen.class));
                        }).show();
            }
        });


        receiptsVM.receiptModelMutableLiveData.observe(this, receiptModel -> {
            App.receiptModel = receiptModel;
            binding.confirmProcess.setClickable(true);
            binding.showProgress.setVisibility(View.GONE);
            startActivity(new Intent(ReceiptScreen.this, PrintReceipt.class));
            finish();
        });
    }

}
