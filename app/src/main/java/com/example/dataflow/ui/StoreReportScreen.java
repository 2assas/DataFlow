package com.example.dataflow.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dataflow.App;
import com.example.dataflow.R;
import com.example.dataflow.ui.reports.ReportViewModel;
import com.example.dataflow.databinding.StoreReportBinding;
import com.example.dataflow.pojo.settings.StoresData;
import com.example.dataflow.ui.adapters.StoreReportAdapter;
import com.example.dataflow.ui.invoice.PrintScreen;

import java.util.ArrayList;

public class StoreReportScreen extends AppCompatActivity {
    StoreReportBinding binding;
    StoresData storesData = new StoresData();
    ReportViewModel reportViewModel;
    String uuid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.store_report);
        storeSpinner();
        reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        binding.button2.setOnClickListener(v -> {
            reportViewModel.getStoreReport(uuid, storesData.getBranchISN(), storesData.getStore_ISN());
            binding.progress.setVisibility(View.VISIBLE);
            observeReport();
        });

    }



    private void storeSpinner(){
        ArrayList<String> stores= new ArrayList<>();
        for(int i = 0; i< App.stores.getData().size(); i++){
            stores.add(App.stores.getData().get(i).getStoreName());
        }
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,stores);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.storesSpinner.setAdapter(aa);

        binding.storesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                storesData = App.stores.getData().get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                storesData = App.stores.getData().get(0);
            }
        });
    }

    private void observeReport(){
        reportViewModel.storeReportModelMutableLiveData.observe(this, storeReportModel -> {
            binding.progress.setVisibility(View.GONE);
            binding.headerContainer.setVisibility(View.VISIBLE);
            binding.reportRecycler.setVisibility(View.VISIBLE);
            binding.printReportButton.setVisibility(View.VISIBLE);
            Log.e("reportLong", storeReportModel.getData().size()+"");
            StoreReportAdapter storeReportAdapter= new StoreReportAdapter(storeReportModel.getData(), this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            binding.reportRecycler.setLayoutManager(layoutManager);
            binding.reportRecycler.setAdapter(storeReportAdapter);
        });
    }

    @SuppressLint("SetTextI18n")
    public void printReport(View view) {
        binding.printReportButton.setVisibility(View.GONE);
        binding.printReportName.setVisibility(View.VISIBLE);
        binding.printReportName.setText("تقرير "+storesData.getStoreName());
        Handler handler =new Handler();
        handler.postDelayed(() -> {
            takeScreenShot();
            startActivity(new Intent(StoreReportScreen.this, PrintScreen.class));
            finish();
        }, 1000);
      }

    private void takeScreenShot() { View u = binding.reportScroll;

        int totalHeight = binding.reportScroll.getChildAt(0).getHeight();
        int totalWidth = binding.reportScroll.getChildAt(0).getWidth();
        Bitmap b = getBitmapFromView(u,totalHeight,totalWidth);
        App.printBitmap = b;
    }
    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {

        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth,totalHeight , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

}
