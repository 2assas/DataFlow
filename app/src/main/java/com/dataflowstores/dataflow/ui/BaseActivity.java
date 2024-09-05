package com.dataflowstores.dataflow.ui;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.dataflowstores.dataflow.R;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("AppShared", MODE_PRIVATE);
        String themeId = prefs.getString("theme", "default"); // Default theme is AppTheme
        setTheme(themeId.equals("default") ? R.style.AppTheme : R.style.SecondTheme); // Apply the retrieved theme
        super.onCreate(savedInstanceState);
    }
}
