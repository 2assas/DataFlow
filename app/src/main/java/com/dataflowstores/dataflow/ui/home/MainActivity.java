package com.dataflowstores.dataflow.ui.home;

import static com.dataflowstores.dataflow.App.theme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.ActivityMainBinding;
import com.dataflowstores.dataflow.ui.BaseActivity;
import com.dataflowstores.dataflow.ui.SplashScreen;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;
    FragmentManager manager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else {
            setupViews();
        }
    }

    @SuppressLint({"HardwareIds", "SetTextI18n"})
    private void setupViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        HomeFragment fragment1 = new HomeFragment();
        manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.container, fragment1)
                .addToBackStack("home")
                .commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        App.customerBalance = "";
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }
}
