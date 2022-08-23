package com.example.dataflow.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.dataflow.App;
import com.example.dataflow.R;
import com.example.dataflow.ViewModels.GateWayViewModel;
import com.example.dataflow.databinding.GatewayBinding;
import com.example.dataflow.pojo.workStation.WorkstationListData;
import com.example.dataflow.utils.Conts;
import com.google.android.gms.common.internal.Constants;

import java.util.ArrayList;

public class GateWay extends AppCompatActivity {
    GatewayBinding binding;
    GateWayViewModel gateWayViewModel;
    String uuid;
    WorkstationListData workstationData = new WorkstationListData();

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
            binding.appVersion.setText("رقم الإصدار" + Conts.APP_VERSION);
            handleLoginCases();
        }
    }

    public void loginButton(View view) {
        if (binding.foundation.getText().toString().isEmpty() && binding.phone.getText().toString().isEmpty()) {
            if (App.isNetworkAvailable(this))
                gateWayViewModel.getLoginStatus(uuid, binding.userName.getText().toString(), binding.password.getText().toString(), null, null);
            else {
                App.noConnectionDialog(this);
            }
        } else {
            if (App.isNetworkAvailable(this))
                gateWayViewModel.getLoginStatus(uuid, binding.userName.getText().toString(), binding.password.getText().toString(), binding.foundation.getText().toString(), binding.phone.getText().toString());
            else {
                App.noConnectionDialog(this);
            }
        }
    }

    public void handleLoginCases() {
        gateWayViewModel.loginLiveData.observe(this, loginStatus -> {
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
                case "Login successfully": {
                    Toast.makeText(this, "Account Login Successfully", Toast.LENGTH_LONG).show();
                    App.currentUser = loginStatus.getData();
                    if (binding.rememberMe.isChecked()) {
                        SharedPreferences.Editor editor = getSharedPreferences("SaveLogin", MODE_PRIVATE).edit();
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

}
