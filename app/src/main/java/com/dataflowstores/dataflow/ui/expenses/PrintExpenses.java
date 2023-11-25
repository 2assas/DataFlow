package com.dataflowstores.dataflow.ui.expenses;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.pojo.expenses.ExpenseData;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.invoice.PrintScreen;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.PrintExpensesBinding;

import java.util.Locale;
import java.util.Objects;

public class PrintExpenses extends AppCompatActivity {
    PrintExpensesBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.print_expenses);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else {
            fillData();
        }
    }

    private void takeScreenShot() {
        View u = binding.expTemplate.printExpenses;
        int totalHeight = binding.expTemplate.printExpenses.getHeight();
        int totalWidth = binding.expTemplate.printExpenses.getWidth();
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

    public void printReceipt(View view) {
        takeScreenShot();
        startActivity(new Intent(this, PrintScreen.class));
        finish();
    }

    @SuppressLint("SetTextI18n")
    public void fillData() {
        binding.back.setOnClickListener(view -> finish());
        ExpenseData expenseData = (ExpenseData) getIntent().getSerializableExtra("expensesModel");
        binding.expTemplate.mainExp.setText("المصروف الرئيسي: " + expenseData.getExpMenuName());
        App.pdfName= "المصروف الرئيسي: " + expenseData.getExpMenuName();

        //TODO::
        if (expenseData.getSubExpMenuName() != null)
            binding.expTemplate.subExp.setText("المصروف الفرعي: " + expenseData.getSubExpMenuName());
        if (expenseData.getSelectedWorkerName() != null)
            binding.expTemplate.worker.setText("الموظف: " + expenseData.getSelectedWorkerName());
        if (expenseData.getMoveID() != null)
            binding.expTemplate.expNum.setText("رقم المصروف: " + expenseData.getMoveID());
        if (expenseData.getShiftISN() != null && !Objects.equals(expenseData.getShiftISN(), "0"))
            binding.expTemplate.shiftNum.setText("رقم الوردية: " + expenseData.getShiftISN());

        binding.expTemplate.date.setText("التاريخ: " + expenseData.getCreateDate());
//        binding.expTemplate.
        binding.expTemplate.expensesTotal.setText(String.format(Locale.US, "%.2f", Float.parseFloat(expenseData.getNetValue())) + " جنيه");
        binding.expTemplate.receiptNotes.setText("ملاحضات \n" + expenseData.getHeaderNotes());
        binding.expTemplate.paymentMethod.setText(expenseData.getCashTypeName());
//        binding.expTemplate.tradeRecord2.setText("السجل التجاري" + "\n" +
//                expenseData.getTradeRecoredNo());
//        binding.expTemplate.taxCardNo2.setText("رقم التسجيل" + "\n" +
//                expenseData.getTaxeCardNo());
        binding.expTemplate.foundationName.setText(App.currentUser.getFoundationName());
    }

}
