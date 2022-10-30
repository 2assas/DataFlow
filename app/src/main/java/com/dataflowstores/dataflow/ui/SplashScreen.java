package com.dataflowstores.dataflow.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.dataflowstores.dataflow.ui.home.MainActivity;
import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ViewModels.GateWayViewModel;
import com.dataflowstores.dataflow.databinding.SplashScreenBinding;

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
                gateWayViewModel.getLoginStatus(uuid, userName, password, null, null, "-1", null, null, null, null,0);
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
            } else if (loginStatus.getMessage().equals("Please Choose Branch")) {
                App.currentUser = loginStatus.getData();
                Intent intent = new Intent(this, GateWay.class);
                intent.putExtra("userName", userName);
                intent.putExtra("password", password);
                startActivity(intent);
                Toast.makeText(this, loginStatus.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                startActivity(new Intent(this, GateWay.class));
                Toast.makeText(this, loginStatus.getMessage(), Toast.LENGTH_LONG).show();
            }
            finish();
        });

    }


}
