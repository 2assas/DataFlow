package com.dataflowstores.dataflow.ui.reports.itemSalesReport;

import static com.dataflowstores.dataflow.App.theme;
import static java.util.Objects.requireNonNull;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.ItemSalesReportPrintBinding;
import com.dataflowstores.dataflow.pojo.report.itemSalesReport.ItemSalesData;
import com.dataflowstores.dataflow.pojo.settings.SafeDepositData;
import com.dataflowstores.dataflow.ui.BaseActivity;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.invoice.PrintScreen;
import com.dataflowstores.dataflow.ui.reports.financialReport.FinancialReportAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ItemSalesPrintScreen extends BaseActivity {
    ItemSalesReportPrintBinding binding;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.US);
    String branchName, shiftNum, clientName, timeFrom, timeTo, workDayStart, workDayEnd;
    List<ItemSalesData> itemSalesDataList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.item_sales_report_print);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else
            initViews();
    }

    void initViews() {
        fillInvoice();
    }

    @SuppressLint("SetTextI18n")
    void fillInvoice() {
        binding.foundationName.setText(App.currentUser.getFoundationName());
        binding.back.setOnClickListener(v -> finish());
        shiftNum = getIntent().getStringExtra("shiftNum");
        App.pdfName = "تقرير مبيعات الاصناف";
        itemSalesDataList = (List<ItemSalesData>) getIntent().getExtras().getSerializable("dataList");

        if (shiftNum == null) {
            binding.shiftNumber.setVisibility(View.GONE);
        } else {
            binding.shiftNumber.setText("وردية رقم: " + shiftNum);
        }

        if (getIntent().getStringExtra("branch") != null) {
            binding.branchName.setText("فرع: " + getIntent().getStringExtra("branch"));
        } else {
            binding.branchName.setVisibility(View.GONE);
        }
        if (itemSalesDataList.size() > 0) {
            ItemSalesAdapter itemSalesAdapter = new ItemSalesAdapter(itemSalesDataList);
            binding.recyclerView2.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerView2.setAdapter(itemSalesAdapter);
        } else {
            binding.recyclerView2.setVisibility(View.GONE);
        }
        if (getIntent().getStringExtra("userName") != null) {
            binding.userName.setVisibility(View.VISIBLE);
            binding.userName.setText("الموظف: " + getIntent().getStringExtra("userName"));
        } else {
            binding.userName.setVisibility(View.GONE);
        }
        if (getIntent().getStringExtra("clientName") != null) {
            binding.clientName.setVisibility(View.VISIBLE);
            binding.clientName.setText("العميل: " + getIntent().getStringExtra("clientName"));
        } else {
            binding.clientName.setVisibility(View.GONE);
        }


        if (getIntent().getStringExtra("startTime") != null && getIntent().getStringExtra("endTime") != null) {
            binding.timeFromTo.setText("التوقيت من: " + getIntent().getStringExtra("startTime") + " إلى: " + getIntent().getStringExtra("endTime"));
        } else
            binding.timeFromTo.setVisibility(View.GONE);

        if (getIntent().getStringExtra("workdayStart") != null && getIntent().getStringExtra("workdayEnd") != null)
            binding.workdayTime.setText("اليومية من: " + getIntent().getStringExtra("workdayStart") + " إلى: " + getIntent().getStringExtra("workdayEnd"));
        else
            binding.workdayTime.setVisibility(View.GONE);
    }

    private void takeScreenShot() {
        View u = binding.reportScroll;
        int totalHeight = binding.reportScroll.getChildAt(0).getHeight();
        int totalWidth = binding.reportScroll.getChildAt(0).getWidth();
        Bitmap b = getBitmapFromView(u, totalHeight, totalWidth);
        App.printBitmap = b;
    }

    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {
        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    public void printButton(View view) {
        binding.printReport.setVisibility(View.GONE);
        binding.back.setVisibility(View.GONE);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            takeScreenShot();
            startActivity(new Intent(ItemSalesPrintScreen.this, PrintScreen.class));
            finish();
        }, 1000);
    }
}
