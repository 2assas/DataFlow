package com.example.dataflow.ui.receipts;

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

import com.example.dataflow.App;
import com.example.dataflow.R;
import com.example.dataflow.databinding.PrintReceiptBinding;
import com.example.dataflow.ui.SplashScreen;
import com.example.dataflow.ui.invoice.PrintScreen;

import java.util.Locale;

public class PrintReceipt extends AppCompatActivity {
    PrintReceiptBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.print_receipt);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        }else{
        fillData();
    }}

    private void takeScreenShot() {
        View u = binding.printReceipt;
        int totalHeight = binding.printReceipt.getHeight();
        int totalWidth = binding.printReceipt.getWidth();
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

    public void printReceipt(View view) {
        takeScreenShot();
        startActivity(new Intent(this, PrintScreen.class));
        finish();
    }

    @SuppressLint("SetTextI18n")
    public void fillData(){
        binding.clientName.setText("العميل: "+App.receiptModel.getData().get(0).getDealerName());
        if(App.receiptModel.getData().get(0).getSaleManName()!=null)
        binding.saleManName.setText("المندوب: "+App.receiptModel.getData().get(0).getSaleManName());
        else
            binding.saleManName.setVisibility(View.GONE);
        if(!App.customerBalance.isEmpty()){
            binding.clientBalance.setText(App.customerBalance);
            binding.clientBalance.setVisibility(View.VISIBLE);
        }
        binding.date.setText("التاريخ: "+ App.receiptModel.getData().get(0).getCreateDate());
        binding.receiptTotal.setText(String.format(Locale.US,"%.2f",Float.parseFloat(App.receiptModel.getData().get(0).getNetValue()))+" جنيه");
        binding.receiptNotes.setText("ملاحضات \n"+App.receiptModel.getData().get(0).getHeaderNotes());
        binding.paymentMethod.setText(App.receiptModel.getData().get(0).getCashTypeName());
        binding.tradeRecord2.setText("السجل التجاري"+"\n"+
                App.receiptModel.getData().get(0).getTradeRecoredNo());
        binding.taxCardNo2.setText("رقم التسجيل" +"\n"+
                App.receiptModel.getData().get(0).getTaxeCardNo());
        binding.foundationName.setText(App.currentUser.getFoundationName());
        binding.moveId.setText("رقم المقبوض: " + App.receiptModel.getData().get(0).getMoveId());
        binding.clientAddress.setText(App.receiptModel.getData().get(0).getBranchAddress());
        if(App.receiptModel.getData().get(0).getTel1()!=null||App.receiptModel.getData().get(0).getTel1().isEmpty())
         binding.tel1.setText(App.receiptModel.getData().get(0).getTel1());
        else
            binding.tel1.setVisibility(View.GONE);
        if(App.receiptModel.getData().get(0).getTel2()!=null||App.receiptModel.getData().get(0).getTel2().isEmpty())
            binding.tel2.setText(App.receiptModel.getData().get(0).getTel2());
        else
            binding.tel2.setVisibility(View.GONE);
    }

}
