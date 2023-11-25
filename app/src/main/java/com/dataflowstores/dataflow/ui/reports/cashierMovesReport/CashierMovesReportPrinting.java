package com.dataflowstores.dataflow.ui.reports.cashierMovesReport;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.CashierMovesPrintingBinding;
import com.dataflowstores.dataflow.pojo.report.cashierMoves.CashierMoveData;
import com.dataflowstores.dataflow.pojo.settings.SafeDepositData;
import com.dataflowstores.dataflow.ui.SearchInvoice;
import com.dataflowstores.dataflow.ui.SearchReceipts;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.cashing.SearchCashing;
import com.dataflowstores.dataflow.ui.expenses.SearchExpenses;
import com.dataflowstores.dataflow.ui.invoice.PrintScreen;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CashierMovesReportPrinting extends AppCompatActivity implements CashierMovesAdapter.onCashierMoveListener {
    CashierMovesPrintingBinding binding;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.US);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.cashier_moves_printing);
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
        //todo:: replace app.FinancialReport with intents.

        App.pdfName ="تقرير حركة الورديات والخزائن";

        if (getIntent().getStringExtra("shiftISN") == null) {
            binding.shiftNumber.setVisibility(View.GONE);
        } else {
            binding.shiftNumber.setText("وردية رقم: " + getIntent().getStringExtra("shiftISN"));
        }
        if (getIntent().getStringExtra("clientName") != null) {
            binding.clientName.setText("العميل: " + getIntent().getStringExtra("clientName"));
        } else {
            binding.clientName.setVisibility(View.GONE);
        }
        if (getIntent().getStringExtra("moveType") != null) {
            binding.moveType.setText("نوع الحركة: " + getIntent().getStringExtra("moveType"));
        } else {
            binding.moveType.setVisibility(View.GONE);
        }
        binding.branchName.setText("فرع: " + getIntent().getStringExtra("branch"));
        List<CashierMoveData> dataList = (List<CashierMoveData>) getIntent().getSerializableExtra("dataList");
        if (dataList.size() > 0) {
            CashierMovesAdapter financialReportAdapter = new CashierMovesAdapter(dataList, this);
            binding.recyclerView2.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerView2.setAdapter(financialReportAdapter);
        } else {
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
            startActivity(new Intent(CashierMovesReportPrinting.this, PrintScreen.class));
            finish();
        }, 1000);
    }

    @Override
    public void onCashierMoveClick(CashierMoveData cashierMoveData) {

        switch (cashierMoveData.getMoveName()) {
            case "مبيعات":
                Intent intent = new Intent(this, SearchInvoice.class);
                startActivity(putIntentExtra(intent, cashierMoveData));
                App.resales = 0;
                break;
            case "مقبوضات":
                Intent intent11 = new Intent(this, SearchReceipts.class);
                startActivity(putIntentExtra(intent11, cashierMoveData));
                break;
            case "مصروفات":
                Intent intent12 = new Intent(this, SearchExpenses.class);
                startActivity(putIntentExtra(intent12, cashierMoveData));
                break;
            case "اذن صرف":
            case "تعديل مخزني":
            case "تكوين صنف":
            case "اهلاكات":
            case "كميات اول المدة":
            case "تحويلات مخزنية":
            case "اذن استلام":
                Intent intent1 = new Intent(this, SearchCashing.class);
                startActivity(putIntentExtra(intent1, cashierMoveData));
                break;
            case "مرتجع مبيعات":
                Intent intent8 = new Intent(this, SearchInvoice.class);
                startActivity(putIntentExtra(intent8, cashierMoveData));
                App.resales = 1;
                break;
            default:
                Toast.makeText(this, R.string.cannot_print_move, Toast.LENGTH_SHORT).show();
        }
    }

    private Intent putIntentExtra(Intent intent, CashierMoveData cashierMoveData) {
        intent.putExtra("moveType", Integer.parseInt(cashierMoveData.getMoveType()));
        intent.putExtra("moveId", cashierMoveData.getMoveID());
        intent.putExtra("branchISN", Long.parseLong(cashierMoveData.getBranchISN()));
        intent.putExtra("workerCBranchISN", Long.parseLong(cashierMoveData.getWorkerCBranchISN()));
        intent.putExtra("workerCISN", Long.parseLong(cashierMoveData.getWorkerCISN()));
        return intent;
    }
}
