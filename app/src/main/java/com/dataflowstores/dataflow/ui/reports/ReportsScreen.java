package com.dataflowstores.dataflow.ui.reports;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.ReportScreenBinding;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.StoreReportScreen;
import com.dataflowstores.dataflow.ui.reports.financialReport.FinancialReport;

public class ReportsScreen extends AppCompatActivity {
    ReportScreenBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        }else{
        binding = DataBindingUtil.setContentView(this,  R.layout.report_screen);
        binding.back.setOnClickListener(v -> finish());
        binding.storeReport.setOnClickListener(v -> {
            startActivity(new Intent(this, StoreReportScreen.class));
        });
        binding.financialReport.setOnClickListener(v -> {
            startActivity(new Intent(this, FinancialReport.class));
        });
    }}
}
