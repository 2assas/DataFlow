package com.dataflowstores.dataflow.ui.payments;

import static com.dataflowstores.dataflow.App.theme;

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
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.PrintPaymentBinding;
import com.dataflowstores.dataflow.databinding.PrintReceiptBinding;
import com.dataflowstores.dataflow.ui.BaseActivity;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.invoice.PrintScreen;

import java.util.Locale;

public class PrintPayment extends BaseActivity {
    PrintPaymentBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.print_payment);
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
        binding.clientName.setText("المورد: "+App.receiptModel.getData().get(0).getDealerName());
        if(App.receiptModel.getData().get(0).getSaleManName()!=null)
            binding.saleManName.setText("المندوب: "+App.receiptModel.getData().get(0).getSaleManName());
        else
            binding.saleManName.setVisibility(View.GONE);
        if(!App.customerBalance.isEmpty()){
            binding.clientBalance.setText(App.customerBalance);
            binding.clientBalance.setVisibility(View.VISIBLE);
            binding.view19.setVisibility(View.VISIBLE);
            App.customerBalance="";
        }else{
            binding.clientBalance.setVisibility(View.GONE);
            binding.view19.setVisibility(View.GONE);
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
        binding.moveId.setText("رقم المدفوع: " + App.receiptModel.getData().get(0).getMoveId());
        App.pdfName = "رقم المدفوع: " + App.receiptModel.getData().get(0).getMoveId();
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
