package com.dataflowstores.dataflow.ui.expenses;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.pojo.expenses.SubExpItem;
import com.dataflowstores.dataflow.pojo.expenses.WorkerItem;
import com.dataflowstores.dataflow.pojo.settings.BanksData;
import com.dataflowstores.dataflow.pojo.settings.SafeDepositData;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.utils.SingleShotLocationProvider;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.ExpensesScreenBinding;
import com.dataflowstores.dataflow.pojo.expenses.MainExpItem;
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
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ExpensesScreen extends AppCompatActivity implements LocationListener {

    ExpensesScreenBinding binding;
    ExpensesViewModel expensesViewModel;
    String uuid;
    MainExpItem selectedMainExp;
    SubExpItem selectedSubExp;
    WorkerItem selectedWorker;
    Integer shiftId;
    SafeDepositData safeDepositData = new SafeDepositData();
    BanksData banksCheckData = new BanksData();
    BanksData banksCreditData = new BanksData();
    Calendar myCalendar = Calendar.getInstance();
    boolean dateValidation = false;
    int paymentMethod = 0;
    String paymentMethodText = "";
    String selectedDate = "";
    private static final int REQUEST_CHECK_SETTINGS = 111;
    float lat = 0;
    float _long = 0;
    private long moveId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.expenses_screen);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else {
            expensesViewModel = new ViewModelProvider(this).get(ExpensesViewModel.class);
            uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            setupView();
            observers();
            userRestricts();
        }
    }

    private void setupView() {
        expensesViewModel.SelectBranchStaff(uuid);
        binding.showProgress.setVisibility(View.GONE);
        binding.back.setOnClickListener(view -> finish());
        App.selectedProducts = new ArrayList<>();
        binding.cashCheck.setChecked(true);
        binding.cashCheck.performClick();
        if (checkPermission()) getLocation(this);
        else requestPermission();

    }

    private void observers() {
        expensesViewModel.allExpResponseMutableLiveData.observe(this, allExpensesResponse -> {
            if (allExpensesResponse.getMainExpResponse() != null) {
                mainExpSpinner(allExpensesResponse.getMainExpResponse().getData(), allExpensesResponse.getSubExpResponse().getData());
            }
            if (allExpensesResponse.getSubExpResponse() != null) {
                workersSpinner(allExpensesResponse.getWorkerResponse().getData());
            }
        });
    }

    private void mainExpSpinner(List<MainExpItem> mainExpItemList, List<SubExpItem> subExpItemList) {
        ArrayList<String> mainExpNames = new ArrayList<>();
        selectedMainExp = mainExpItemList.get(0);
        for (int i = 0; i < mainExpItemList.size(); i++) {
            mainExpNames.add(mainExpItemList.get(i).getMainExpMenuName());
        }
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mainExpNames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.mainExpSpinner.setAdapter(aa);

        binding.mainExpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMainExp = mainExpItemList.get(i);
                if (subExpItemList != null) {
                    subExpSpinner(subExpItemList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedMainExp = mainExpItemList.get(0);
            }
        });
    }

    private void subExpSpinner(List<SubExpItem> subExpItemList) {
        ArrayList<String> subExpNames = new ArrayList<>();
        List<SubExpItem> filterList = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < subExpItemList.size(); i++) {
            if (Objects.equals(subExpItemList.get(i).getMainExpMenuISN(), selectedMainExp.getMainExpMenuISN()) && Objects.equals(subExpItemList.get(i).getMainExpMenuBranchISN(), selectedMainExp.getMainExpMenuBranchISN())) {
                if (counter == 0) {
                    subExpNames.add("");
                    filterList.add(0, new SubExpItem(true));
                }
                subExpNames.add(subExpItemList.get(i).getSubExpMenuName());
                filterList.add(subExpItemList.get(i));
                counter++;
            }
        }
        if (filterList.size() > 0)
            selectedSubExp = filterList.get(0);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, subExpNames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.subExpSpinner.setAdapter(aa);

        binding.subExpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) selectedSubExp = null;
                else
                    selectedSubExp = filterList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedSubExp = null;
            }
        });
    }

    private void workersSpinner(List<WorkerItem> workerItems) {
        ArrayList<String> workerNames = new ArrayList<>();
        if (Objects.equals(selectedMainExp.getMustChooseWorker(), "0"))
            workerItems.add(0, new WorkerItem(true));

        for (int i = 0; i < workerItems.size(); i++) {
            if (i == 0 && selectedMainExp.getMustChooseWorker().equals("0")) workerNames.add("");
            else
                workerNames.add(workerItems.get(i).getWorkerName());
        }
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, workerNames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.workersSpinner.setAdapter(aa);

        binding.workersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0 && selectedMainExp.getMustChooseWorker().equals("0"))
                    selectedWorker = null;
                else
                    selectedWorker = workerItems.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (selectedMainExp.getMustChooseWorker().equals("0"))
                    selectedWorker = null;
                else
                    selectedWorker = workerItems.get(0);
            }

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
        if (lat != 0 || _long != 0) {
            binding.confirmProcess.setClickable(false);
            if (Objects.equals(selectedMainExp.getMustChooseWorker(), "1") && selectedWorker == null) {
                binding.workersSpinner.performClick();
                binding.confirmProcess.setClickable(true);
                Toast.makeText(this, "لابد من إختيار الموظف مع المصروف الرئيسي المحدد", Toast.LENGTH_LONG).show();
            } else if (binding.receiptTotal.getText().toString().isEmpty()) {
                binding.receiptTotal.setError("مطلوب");
                binding.confirmProcess.setClickable(true);
            } else {
                createExpenses();
                binding.showProgress.setVisibility(View.VISIBLE);
            }
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
            Log.e("checkSafe", "at " + i + " = " + App.safeDeposit.getData().get(i).getSafeDepositName() +
                    " -id- " + App.safeDeposit.getData().get(i).getSafeDeposit_ISN() +
                    " -branch id- " + App.safeDeposit.getData().get(i).getBranchISN()
            );

            if (App.currentUser.getSafeDepositISN() == App.safeDeposit.getData().get(i).getSafeDeposit_ISN()
                    && App.currentUser.getSafeDepositBranchISN() == App.safeDeposit.getData().get(i).getBranchISN()) {
                binding.safeDepositSpinner.setSelection(i);
                Log.e("checkSafe", "selected = " + App.safeDeposit.getData().get(i).getSafeDepositName() +
                        " -id- " + App.safeDeposit.getData().get(i).getSafeDeposit_ISN() +
                        " -branch id- " + App.safeDeposit.getData().get(i).getBranchISN() +
                        " -user id- " + App.currentUser.getSafeDepositISN() +
                        " -user branch id- " + App.currentUser.getSafeDepositBranchISN()
                );
            }
//          SafeDepositBranchISN":7,"SafeDepositISN":21,
        }

    }

    private void bankSpinner() {
        ArrayList<String> banks = new ArrayList<>();
        if (App.banks.getData() != null) {
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
        } else {
            Toast.makeText(this, "لا يوجد لديك بنوك مضافة", Toast.LENGTH_LONG).show();
        }
    }

    private void creditBankSpinner() {
        ArrayList<String> banks = new ArrayList<>();
        if (App.banks.getData() != null) {
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
        } else {
            Toast.makeText(this, "لا يوجد لديك بنوك مضافة", Toast.LENGTH_LONG).show();
        }
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

    public void createExpenses() {
        double total = Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", Float.parseFloat(binding.receiptTotal.getText().toString())));

        expensesViewModel.createExpenses(App.currentUser.getBranchISN(), uuid, paymentMethod,
                0, binding.receiptNotes.getText().toString(), Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", total)),
                0, 0, 0, total, 0, 0, total, 0, 0,
                total, total, 0, total,
                safeDepositData.getBranchISN(), safeDepositData.getSafeDeposit_ISN(), banksCreditData.getBranchISN(), banksCreditData.getBank_ISN(), null,
                null, null, App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN(), binding.cheqNumber.getText().toString(),
                selectedDate, banksCheckData.getBranchISN(), banksCheckData.getBank_ISN(), 2, lat, _long, !binding.shiftNumber.getText().toString().isEmpty() ? Long.parseLong(binding.shiftNumber.getText().toString()) : null, Long.parseLong(selectedMainExp.getMainExpMenuISN()),
                Long.parseLong(selectedMainExp.getMainExpMenuBranchISN()), selectedMainExp.getMainExpMenuName(), selectedSubExp != null ? Long.valueOf(selectedSubExp.getSubExpMenuISN()) : null, selectedSubExp != null ? Long.valueOf(selectedSubExp.getSubExpMenuBranchISN()) : null, selectedSubExp != null ? selectedSubExp.getSubExpMenuName() : null,
                selectedWorker != null ? Long.valueOf(selectedWorker.getWorkerCBranchISN()) : null, selectedWorker != null ? Long.valueOf(selectedWorker.getWorkerISN()) : null
        );

        expensesViewModel.expensesResponseMutableLiveData.observe(this, expensesResponse -> {
            new AlertDialog.Builder(this).setTitle("عملية ناجحة")
                    .setMessage("تم تسجيل عملية الدفع بنجاح")
                    .setCancelable(false)
                    .setIcon(getResources().getDrawable(R.drawable.ic_baseline_verified_24))
                    .setPositiveButton("طباعة", (dialog, which) -> {
                        expensesViewModel.getExpenses(App.currentUser.getBranchISN(), uuid, String.valueOf(expensesResponse.getData().getMoveId()),
                                App.currentUser.getWorkerBranchISN(), App.currentUser.getWorkerISN(), App.currentUser.getPermission());
                        binding.showProgress.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }).setNegativeButton("إغلاق", (dialog, which) -> {
                        dialog.dismiss();
                        finish();
                        startActivity(new Intent(this, ExpensesScreen.class));
                    }).show();
        });


        expensesViewModel.expensesModelMutableLiveData.observe(this, expensesModel -> {
            binding.showProgress.setVisibility(View.GONE);
            Intent intent = new Intent(this, PrintExpenses.class);
            intent.putExtra("expensesModel", expensesModel.getData().get(0));
            startActivity(intent);
            finish();
        });
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
                        getLocation(ExpensesScreen.this);
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


}
