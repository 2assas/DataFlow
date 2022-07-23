package com.example.dataflow.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.dataflow.App;
import com.example.dataflow.R;
import com.example.dataflow.ViewModels.GateWayViewModel;
import com.example.dataflow.databinding.SplashScreenBinding;

import java.net.InetAddress;

public class SplashScreen extends AppCompatActivity {
    SplashScreenBinding binding;
    GateWayViewModel gateWayViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.splash_screen);
        SharedPreferences prefs = getSharedPreferences("SaveLogin", MODE_PRIVATE);
        String userName = prefs.getString("userName", "");//"No name defined" is the default value.
        String password = prefs.getString("password", ""); //0 is the default
        // value// .
        String uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        gateWayViewModel = new ViewModelProvider(this).get(GateWayViewModel.class);
        if (!userName.isEmpty() && !password.isEmpty()) {
            if (App.isNetworkAvailable(this))
                gateWayViewModel.getLoginStatus(uuid, userName, password, null, null);
            else {
                App.noConnectionDialog(this);
            }             //assas
        } else {
            startActivity(new Intent(this, GateWay.class));
            finish();
        }
        gateWayViewModel.loginLiveData.observe(this, loginStatus -> {
            if (loginStatus.getMessage().equals("Login successfully")) {
                App.currentUser = loginStatus.getData();
                startActivity(new Intent(this, MainActivity.class));
            } else {
                startActivity(new Intent(this, GateWay.class));
                Toast.makeText(this, loginStatus.getMessage(), Toast.LENGTH_LONG).show();
            }
            finish();
        });
    }

}
