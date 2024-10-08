package com.dataflowstores.dataflow.ui;

import static com.dataflowstores.dataflow.App.theme;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ViewModels.GateWayViewModel;
import com.dataflowstores.dataflow.databinding.SplashScreenBinding;
import com.dataflowstores.dataflow.pojo.login.LoginStatus;
import com.dataflowstores.dataflow.ui.gateway.GateWay;
import com.dataflowstores.dataflow.ui.home.MainActivity;

public class SplashScreen extends BaseActivity {
    SplashScreenBinding binding;
    GateWayViewModel gateWayViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.splash_screen);
        SharedPreferences prefs = getSharedPreferences("AppShared", MODE_PRIVATE);
        String userName = prefs.getString("userName", "");//"No name defined" is the default value.
        String password = prefs.getString("password", ""); //0 is the default


        // value// .
        String uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        gateWayViewModel = new ViewModelProvider(this).get(GateWayViewModel.class);
        gateWayViewModel.toastErrorMutableLiveData.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());
        if (!userName.isEmpty() && !password.isEmpty()) {
            if (App.isNetworkAvailable(this))
                gateWayViewModel.getLoginStatus(uuid, userName, password, null, null, "-1", null, null, null, null, 0);
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
                finish();
            } else if (loginStatus.getMessage().equals("Please Choose Branch") || loginStatus.getMessage().equals("Please Select Foundation")) {
                App.currentUser = loginStatus.getData();
                Intent intent = new Intent(this, GateWay.class);
                intent.putExtra("userName", userName);
                intent.putExtra("password", password);
                if (loginStatus.getMessage().equals("Please Select Foundation"))
                    intent.putExtra("multiFoundation", "true");
                startActivity(intent);
                Toast.makeText(this, loginStatus.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            } else if (loginStatus.getMessage().contains("Go to Play Store To Update")) {
                getAlertDialog(loginStatus).show();
            } else {
                startActivity(new Intent(this, GateWay.class));
                Toast.makeText(this, loginStatus.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }


    private AlertDialog getAlertDialog(LoginStatus loginStatus) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(loginStatus.getMessage());
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_baseline_error_outline_24);
        if (loginStatus.getMessage().contains("Go to Play Store To Update")) {
            builder.setPositiveButton("الذهاب للمتجر", (dialogInterface, i) -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.dataflowstores.dataflow&hl=en&gl=US"));
                startActivity(intent);
                dialogInterface.dismiss();
                finish();
            });
        }

        return builder.create();
    }

}
