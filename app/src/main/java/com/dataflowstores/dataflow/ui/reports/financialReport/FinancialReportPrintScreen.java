package com.dataflowstores.dataflow.ui.reports.financialReport;

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
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.FinancialReportPrintBinding;
import com.dataflowstores.dataflow.pojo.settings.SafeDepositData;
import com.dataflowstores.dataflow.ui.BaseActivity;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.invoice.PrintScreen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FinancialReportPrintScreen extends BaseActivity {
    FinancialReportPrintBinding binding;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.US);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.financial_report_print);
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
        binding.safeDepositName.setText("");
        binding.back.setOnClickListener(v -> finish());
        App.pdfName = "تقرير الورديات والخزائن";
        if (App.financialReportData.getShift() == null) {
            binding.shiftNumber.setVisibility(View.GONE);
            binding.shiftContainer.setVisibility(View.GONE);
            binding.shiftTimes.setVisibility(View.GONE);
            binding.finalTotal.setVisibility(View.GONE);
        } else {
            binding.shiftNumber.setText("وردية رقم: " + App.financialReportData.getShift().getShiftISN());

            binding.shiftStatus.setText(App.financialReportData.getShift().getShiftStatus());
            binding.openShiftAmount.setText(String.format(Locale.US, "%.2f", Double.parseDouble(App.financialReportData.getShift().getOpenCash())));
            binding.closeShiftAmount.setText(String.format(Locale.US, "%.2f", Double.parseDouble(App.financialReportData.getShift().getCloseCash())));
            binding.toBePaidAmount.setText(String.format(Locale.US, "%.2f", App.financialReportData.getShift().getAssumedCash()));
            binding.paidAmount.setText(String.format(Locale.US, "%.2f", Double.parseDouble(App.financialReportData.getShift().getHandedCash())));
            Date startTime = null;
            try {
                startTime = sdf.parse(App.financialReportData.getShift().getShiftOpenDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date endTime = null;
            try {
                endTime = sdf.parse(App.financialReportData.getShift().getShiftCloseDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            binding.openShiftTime.setText(sdf.format(requireNonNull(startTime)));
            binding.closeShiftTime.setText(sdf.format(requireNonNull(endTime)));
            binding.finalTotal.setText(String.format(Locale.US, "%.2f", App.financialReportData.getShift().getShiftTotal()));
        }
//        if(App.financialReportData.get)
        binding.totalRevenue.setText(String.format(Locale.US, "%.2f", App.financialReportData.getTotalRevenue()));
        binding.totalExpenses.setText(String.format(Locale.US, "%.2f", App.financialReportData.getTotalExpenses()));
        binding.finalBalance.setText(String.format(Locale.US, "%.2f", App.financialReportData.getFinalBalance()));
        binding.branchName.setText("فرع: " + getIntent().getStringExtra("branch"));
        if (App.financialReportData.getReport().size() > 0) {
            FinancialReportAdapter financialReportAdapter = new FinancialReportAdapter(App.financialReportData.getReport());
            binding.recyclerView2.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerView2.setAdapter(financialReportAdapter);
        } else {
            binding.headerContainer.setVisibility(View.GONE);
            binding.recyclerView2.setVisibility(View.GONE);
        }
        if (getIntent().getStringExtra("bank") != null) {
            binding.bankName.setVisibility(View.VISIBLE);
            binding.bankName.setText("البنك: " + getIntent().getStringExtra("bank"));
        }
        if (getIntent().getStringExtra("userName") != null) {
            binding.userName.setVisibility(View.VISIBLE);
            binding.userName.setText("الموظف: " + getIntent().getStringExtra("userName"));
        }
        if (getIntent().getStringExtra("cash").equals("1") || getIntent().getStringExtra("check").equals("1") || getIntent().getStringExtra("credit").equals("1")) {
            binding.paymentMethod.setVisibility(View.VISIBLE);
            if (getIntent().getStringExtra("cash").equals("1"))
                binding.paymentMethod.setText(binding.paymentMethod.getText().toString() + "    " + "النقدى");
            if (getIntent().getStringExtra("check").equals("1"))
                binding.paymentMethod.setText(binding.paymentMethod.getText().toString() + "    " + "الشيك");
            if (getIntent().getStringExtra("cash").equals("1"))
                binding.paymentMethod.setText(binding.paymentMethod.getText().toString() + "    " + "الإئتمان");
        }

        if (getIntent().getSerializableExtra("safeDeposit") != null) {
            SafeDepositData safeDeposit = (SafeDepositData) getIntent().getSerializableExtra("safeDeposit");
            binding.safeDepositName.setText(safeDeposit.getSafeDepositName());
        } else
            binding.safeDepositName.setVisibility(View.GONE);
        if (getIntent().getStringExtra("startTime") != null && getIntent().getStringExtra("endTime") != null) {
            binding.timeFromTo.setText("التوقيت من: " + getIntent().getStringExtra("startTime") + " إلى: " + getIntent().getStringExtra("endTime"));
        } else
            binding.timeFromTo.setVisibility(View.GONE);

        Log.e("CheckWorkdays", getIntent().getStringExtra("workdayStart") + " ----  " + getIntent().getStringExtra("workdayEnd"));
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
            startActivity(new Intent(FinancialReportPrintScreen.this, PrintScreen.class));
            finish();
        }, 1000);
    }
}