package com.dataflowstores.dataflow.ui.gateway;


import static com.dataflowstores.dataflow.App.theme;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ViewModels.GateWayViewModel;
import com.dataflowstores.dataflow.databinding.GatewayBinding;
import com.dataflowstores.dataflow.pojo.settings.SafeDepositData;
import com.dataflowstores.dataflow.pojo.settings.StoresData;
import com.dataflowstores.dataflow.pojo.workStation.BranchData;
import com.dataflowstores.dataflow.pojo.workStation.WorkstationListData;
import com.dataflowstores.dataflow.ui.BaseActivity;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.home.MainActivity;
import com.dataflowstores.dataflow.webService.Constants;

import java.util.ArrayList;
import java.util.List;

public class GateWay extends BaseActivity implements SelectFoundationDialog.DialogListener {
    GatewayBinding binding;
    GateWayViewModel gateWayViewModel;
    String uuid;
    WorkstationListData workstationData = new WorkstationListData();
    List<StoresData> storesDataList;
    List<SafeDepositData> safeDepositDataList;
    List<BranchData> branchDataList;
    BranchData selectedBranch;
    StoresData selectedStore;
    SafeDepositData selectedSafeDeposit;
    List<SafeDepositData> safeDepositDataListBranch = new ArrayList<>();
    List<StoresData> storesDataListBranch = new ArrayList<>();
    boolean selectBranchStaff = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.gateway);
            uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//        uuid = "bbca3293592c95eb";
            Log.e("getID", uuid);
            gateWayViewModel = new ViewModelProvider(this).get(GateWayViewModel.class);
            binding.appVersion.setText("رقم الإصدار" + Constants.APP_VERSION);
            gateWayViewModel.toastErrorMutableLiveData.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());
            handleLoginCases();
            checkSavePassword();
            observeSelectBranch();
            demoLogin();
        }
    }

    private void checkSavePassword() {
        if (getIntent().getStringExtra("userName") != null) {
            binding.userName.setText(getIntent().getStringExtra("userName"));
            binding.password.setText(getIntent().getStringExtra("password"));
            if (getIntent().getStringExtra("multiFoundation") == null) {
                binding.selectBranch.getRoot().setVisibility(View.VISIBLE);
                gateWayViewModel.SelectBranchStaff(uuid, 0);
                selectBranchStaff = true;
                binding.progress.setVisibility(View.VISIBLE);
                binding.login.setEnabled(false);
            } else {
                binding.login.performClick();
            }
        }
    }




    public void loginButton(View view) {
        binding.progress.setVisibility(View.VISIBLE);
        binding.login.setClickable(false);
        if (binding.foundation.getText().toString().isEmpty() && binding.phone.getText().toString().isEmpty()) {
            if (App.isNetworkAvailable(this))
                if (selectBranchStaff)
                    gateWayViewModel.getLoginStatus(uuid, binding.userName.getText().toString(), binding.password.getText().toString(), null, null, String.valueOf(selectedBranch.getBranchISN()), String.valueOf(selectedSafeDeposit.getBranchISN()), String.valueOf(selectedSafeDeposit.getSafeDeposit_ISN()), String.valueOf(selectedStore.getBranchISN()), String.valueOf(selectedStore.getStore_ISN()),0);
                else
                    gateWayViewModel.getLoginStatus(uuid, binding.userName.getText().toString(), binding.password.getText().toString(), null, null, "-1", null, null, null, null,0);
            else {
                App.noConnectionDialog(this);
            }
        } else {
            if (App.isNetworkAvailable(this))
                gateWayViewModel.getLoginStatus(uuid, binding.userName.getText().toString(), binding.password.getText().toString(), binding.foundation.getText().toString(), binding.phone.getText().toString(), "-1", null, null, null, null,0);
            else {
                App.noConnectionDialog(this);
            }
        }
    }

    private void demoLogin(){
        binding.demoButton.setOnClickListener(v -> {
            gateWayViewModel.getLoginStatus(uuid, null, null, null, null, "-1", null, null, null, null,1);
        });
    }

    public void handleLoginCases() {
        gateWayViewModel.loginLiveData.observe(this, loginStatus -> {
            binding.login.setClickable(true);
            binding.progress.setVisibility(View.GONE);
            switch (loginStatus.getMessage()) {
                case "please add foundation name and phone": {
                    Toast.makeText(this, "Please Insert Foundation Name and Phone Number", Toast.LENGTH_LONG).show();
                    binding.foundationCard.setVisibility(View.VISIBLE);
                    binding.phoneCard.setVisibility(View.VISIBLE);
                }
                break;
                case "Workstation not found": {
                    Toast.makeText(this, "Workstation not found", Toast.LENGTH_LONG).show();
                    binding.phoneCard.setVisibility(View.GONE);
                    binding.foundationCard.setVisibility(View.GONE);
                    binding.login.setVisibility(View.GONE);
                    if (App.isNetworkAvailable(this))
                        gateWayViewModel.createWorkstation(uuid);
                    else {
                        App.noConnectionDialog(this);
                    }
                    Log.e("checkWorkStation", "notFound");
                }
                break;
                case "your account expired": {
                    Toast.makeText(this, "your account expired", Toast.LENGTH_LONG).show();
                }
                break;
                case "your account still pending": {
                    Toast.makeText(this, "your account still pending", Toast.LENGTH_LONG).show();
                }
                break;
                case "user name or password not true": {
                    Toast.makeText(this, "Please enter a correct user name and password", Toast.LENGTH_LONG).show();
                }
                break;
                case "Please Choose Branch": {
                    Toast.makeText(this, "Please Choose Branch", Toast.LENGTH_LONG).show();
                    binding.selectBranch.getRoot().setVisibility(View.VISIBLE);
                    App.currentUser = loginStatus.getData();
                    Log.e("checkToken", "Activity " + App.currentUser.getToken());
                    gateWayViewModel.SelectBranchStaff(uuid, 0);
                    binding.progress.setVisibility(View.VISIBLE);
                    selectBranchStaff = true;
                    binding.login.setEnabled(false);
                }
                break;
                case "Please Select Foundation": {
                    SelectFoundationDialog selectFoundationDialog = new SelectFoundationDialog();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("FCount", loginStatus.getfCount());
                    selectFoundationDialog.setArguments(bundle);
                    selectFoundationDialog.show(getSupportFragmentManager(), selectFoundationDialog.getTag());
                    selectFoundationDialog.setListener(this);
                }
                break;
                case "Login successfully": {
                    Toast.makeText(this, "Account Login Successfully", Toast.LENGTH_LONG).show();
                    App.currentUser = loginStatus.getData();
                    if (binding.rememberMe.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences("AppShared", MODE_PRIVATE).edit();
                        editor.putString("userName", binding.userName.getText().toString());
                        editor.putString("password", binding.password.getText().toString());
                        editor.apply();
                    }
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
                break;
                default:
                    new AlertDialog.Builder(this)
                            .setMessage(loginStatus.getMessage())
                            .setCancelable(false)
                            .setIcon(R.drawable.ic_baseline_error_outline_24)
                            .setNegativeButton("حسنا", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            }).show();
                    break;
            }

        });
        gateWayViewModel.workStationLiveData.observe(this, workstation -> {
            if (workstation.getStatus() == 1) {
                binding.phoneCard.setVisibility(View.GONE);
                binding.foundationCard.setVisibility(View.GONE);
                binding.login.setVisibility(View.GONE);
                binding.branchSpinner.setVisibility(View.VISIBLE);
                binding.workstationName.setVisibility(View.VISIBLE);
                binding.insertWork.setVisibility(View.VISIBLE);
                ArrayList<String> branchesName = new ArrayList<>();
                for (int i = 0; i < workstation.getData().size(); i++) {
                    branchesName.add(workstation.getData().get(i).getBranchName());
                }
                ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, branchesName);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.branchSpinner.setAdapter(aa);
                binding.branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override

                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        workstationData = workstation.getData().get(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        workstationData = workstation.getData().get(0);
                    }
                });
                binding.insertWork.setOnClickListener(view -> {
                    if (!binding.workstation.getText().toString().isEmpty()) {
                        if (App.isNetworkAvailable(this))
                            gateWayViewModel.insertWorkstation(uuid, workstationData.getBranch_ISN(), binding.workstation.getText().toString());
                        else {
                            App.noConnectionDialog(this);
                        }
                        Log.e("insertWork", "clicked");
                    } else
                        binding.workstation.setError("Please Enter Workstation Name");
                });
            } else {
                binding.phoneCard.setVisibility(View.GONE);
                binding.foundationCard.setVisibility(View.GONE);
                binding.login.setVisibility(View.GONE);
                binding.branchNameCard.setVisibility(View.VISIBLE);
                binding.branchNumberCard.setVisibility(View.VISIBLE);
                binding.insertBranch.setVisibility(View.VISIBLE);
                binding.insertBranch.setOnClickListener(view -> {
                    if (!binding.branchName.getText().toString().isEmpty() && !binding.branchNumber.getText().toString().isEmpty())
                        gateWayViewModel.insertBranch(Integer.parseInt(binding.branchNumber.getText().toString()), binding.branchName.getText().toString(), uuid);
                    else {
                        binding.branchNumber.setError("Required");
                        binding.branchName.setError("Required");
                    }
                });
                //TODO: handle no branches case.
            }
        });
        gateWayViewModel.insertBranchLiveData.observe(this, branch -> {
            if (branch.getStatus() == 1) {
                if (App.isNetworkAvailable(this))
                    gateWayViewModel.createWorkstation(uuid);
                else {
                    App.noConnectionDialog(this);
                }
                binding.branchNumberCard.setVisibility(View.GONE);
                binding.branchNameCard.setVisibility(View.GONE);
                binding.insertBranch.setVisibility(View.GONE);
                binding.branchSpinner.setVisibility(View.VISIBLE);
                binding.workstationName.setVisibility(View.VISIBLE);
                binding.insertWork.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, branch.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        gateWayViewModel.insertWorkstationLiveData.observe(this, workstation -> {
            if (workstation.getStatus() == 1) {
                binding.phoneCard.setVisibility(View.GONE);
                binding.foundationCard.setVisibility(View.GONE);
                binding.login.setVisibility(View.VISIBLE);
                binding.branchSpinner.setVisibility(View.GONE);
                binding.workstationName.setVisibility(View.GONE);
                binding.insertWork.setVisibility(View.GONE);
            } else {
                Toast.makeText(this, workstation.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void observeSelectBranch() {
        gateWayViewModel.branchStaffMutableLiveData.observe(this, branchStaffModel -> {
            binding.progress.setVisibility(View.GONE);
            binding.login.setEnabled(true);
            branchDataList = branchStaffModel.getBranchDataList().getBranchData();
            storesDataList = branchStaffModel.getStoreDataList().getData();
            safeDepositDataList = branchStaffModel.getSafeDepositDataList().getData();
            storesDataListBranch = new ArrayList<>();
            safeDepositDataListBranch = new ArrayList<>();
            for (int i = 0; i < branchStaffModel.getBranchDataList().getBranchData().size(); i++) {
                if (branchStaffModel.getBranchDataList().getBranchData().get(i).getBranchISN() == App.currentUser.getBranchISN()) {
                    selectedBranch = branchStaffModel.getBranchDataList().getBranchData().get(i);
                    branchDataList.remove(selectedBranch);
                    branchDataList.add(0, selectedBranch);
                    break;
                }
            }
            branchSpinner(branchDataList);
            for (int k = 0; k < storesDataList.size(); k++) {
                if (storesDataList.get(k).getBranchISN() == selectedBranch.getBranchISN()) {
                    if (storesDataList.get(k).getStore_ISN() == App.currentUser.getCashierStoreISN()
                            && storesDataList.get(k).getBranchISN() == App.currentUser.getCashierStoreBranchISN()) {
                        storesDataListBranch.add(0, storesDataList.get(k));
                        selectedStore = storesDataList.get(k);
                    } else {
                        storesDataListBranch.add(storesDataList.get(k));
                    }
                }
            }
            for (int j = 0; j < safeDepositDataList.size(); j++) {
                if (safeDepositDataList.get(j).getBranchISN() == selectedBranch.getBranchISN()) {
                    if (safeDepositDataList.get(j).getSafeDeposit_ISN() == App.currentUser.getSafeDepositISN()
                            && safeDepositDataList.get(j).getBranchISN() == App.currentUser.getSafeDepositBranchISN()) {
                        safeDepositDataListBranch.add(0, safeDepositDataList.get(j));
                        selectedSafeDeposit = safeDepositDataList.get(j);
                    } else {
                        safeDepositDataListBranch.add(safeDepositDataList.get(j));
                    }
                }
            }
            storeSpinner(storesDataListBranch);
            safeDepositSpinner(safeDepositDataListBranch);
        });

    }

    private void branchSpinner(List<BranchData> branchList) {
        ArrayList<String> branchesName = new ArrayList<>();
        for (int i = 0; i < branchList.size(); i++) {
            branchesName.add(branchList.get(i).getBranchName());
        }
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, branchesName);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.selectBranch.branchSpinner.setAdapter(aa);
        binding.selectBranch.branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBranch = branchList.get(i);
                storesDataListBranch = new ArrayList<>();
                safeDepositDataListBranch = new ArrayList<>();
                for (int k = 0; k < storesDataList.size(); k++) {
                    if (storesDataList.get(k).getBranchISN() == selectedBranch.getBranchISN()) {
                        storesDataListBranch.add(storesDataList.get(k));
                    }
                }
                for (int j = 0; j < safeDepositDataList.size(); j++) {
                    if (safeDepositDataList.get(j).getBranchISN() == selectedBranch.getBranchISN()) {
                        safeDepositDataListBranch.add(safeDepositDataList.get(j));
                    }
                }
                storeSpinner(storesDataListBranch);
                safeDepositSpinner(safeDepositDataListBranch);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedBranch = branchList.get(0);
            }
        });
    }

    private void storeSpinner(List<StoresData> storeList) {
        ArrayList<String> branchesName = new ArrayList<>();
        for (int i = 0; i < storeList.size(); i++) {
            branchesName.add(storeList.get(i).getStoreName());
        }
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, branchesName);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.selectBranch.storeSpinner.setAdapter(aa);
        binding.selectBranch.storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedStore = storeList.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedStore = storeList.get(0);
            }
        });
        for (int i = 0; i < storeList.size(); i++) {
            if (App.currentUser.getCashierStoreISN() == storeList.get(i).getStore_ISN()
                    && App.currentUser.getCashierStoreBranchISN() == storeList.get(i).getBranchISN())
                binding.selectBranch.storeSpinner.setSelection(i);
        }
    }

    private void safeDepositSpinner(List<SafeDepositData> safeDepositList) {
        ArrayList<String> safeDepositsName = new ArrayList<>();
        for (int i = 0; i < safeDepositList.size(); i++) {
            safeDepositsName.add(safeDepositList.get(i).getSafeDepositName());
        }
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, safeDepositsName);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.selectBranch.safeDepositSpinner.setAdapter(aa);
        binding.selectBranch.safeDepositSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSafeDeposit = safeDepositList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedSafeDeposit = safeDepositList.get(0);
            }
        });
        for (int i = 0; i < safeDepositList.size(); i++) {
            if (App.currentUser.getSafeDepositISN() == safeDepositList.get(i).getSafeDeposit_ISN()
                    && App.currentUser.getSafeDepositBranchISN() == safeDepositList.get(i).getBranchISN())
                binding.selectBranch.safeDepositSpinner.setSelection(i);
        }
    }

    @Override
    public void onDialogDismissed() {
        binding.login.performClick();
    }
}
