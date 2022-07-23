package com.example.dataflow.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dataflow.App;
import com.example.dataflow.R;
import com.example.dataflow.ViewModels.PrintInvoiceVM;
import com.example.dataflow.databinding.SearchInvoiceBinding;
import com.example.dataflow.ui.adapters.PrintingLinesAdapter;
import com.example.dataflow.ui.invoice.PrintScreen;

import java.text.DecimalFormat;
import java.util.Locale;

public class SearchInvoice extends AppCompatActivity {
    PrintInvoiceVM printInvoiceVM;
    SearchInvoiceBinding binding;
    String uuid;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.search_invoice);
            printInvoiceVM = new ViewModelProvider(this).get(PrintInvoiceVM.class);
            uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            setupViews();
            invoiceObserver();
        }
    }

    public void setupViews() {
        binding.invoiceTemplate.printButton.setVisibility(View.GONE);
        binding.searchInvoices.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (App.isNetworkAvailable(SearchInvoice.this)) {
                    printInvoiceVM.getPrintingData(String.valueOf(App.currentUser.getBranchISN()), uuid, s,
                            String.valueOf(App.currentUser.getWorkerBranchISN()), String.valueOf(App.currentUser.getWorkerISN()), SearchInvoice.this, null);
                    binding.progressBar.setVisibility(View.VISIBLE);
                } else {
                    App.noConnectionDialog(SearchInvoice.this);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                binding.printButton.setOnClickListener(view -> {
                    if (App.isNetworkAvailable(SearchInvoice.this)) {
                        takeScreenShot();
                        startActivity(new Intent(SearchInvoice.this, PrintScreen.class));
                    } else {
                        App.noConnectionDialog(SearchInvoice.this);
                    }
                });
                return false;
            }
        });
    }

    private void takeScreenShot() {
        View u = binding.invoiceTemplate.scrollView2;
        int totalHeight = binding.invoiceTemplate.scrollView2.getChildAt(0).getHeight();
        int totalWidth = binding.invoiceTemplate.scrollView2.getChildAt(0).getWidth();
        App.printBitmap = getBitmapFromView(u, totalHeight, totalWidth);
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

    public void invoiceObserver() {
        printInvoiceVM.errorMutableLiveData.observe(this, error -> {
            if (error) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
        printInvoiceVM.customerBalanceLiveData.observe(this, customerBalance -> {
            App.customerBalance = customerBalance.getMessage();
            binding.progressBar.setVisibility(View.GONE);
            binding.printButton.setVisibility(View.VISIBLE);
            displayPrintingData();
        });
        printInvoiceVM.invoiceMutableLiveData.observe(this, invoice -> {
            App.printInvoice = invoice;
            if (App.currentUser.getMobileShowDealerCurrentBalanceInPrint() == 1 && App.customer.getDealerName() != null) {
                printInvoiceVM.getCustomerBalance(uuid, String.valueOf(App.customer.getDealer_ISN()), String.valueOf(App.customer.getBranchISN()), String.valueOf(App.customer.getDealerType()), String.valueOf(App.customer.getDealerName()));
            } else {
                binding.printButton.setVisibility(View.VISIBLE);
                displayPrintingData();
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void displayPrintingData() {
        binding.invoiceTemplate.foundationName.setText(App.currentUser.getFoundationName());
        binding.invoiceTemplate.branchName.setText(App.currentUser.getBranchName());
        binding.invoiceTemplate.invoiceNumber.setText("رقم  الشيك: " + App.printInvoice.getMoveHeader().getBillNumber());
        binding.invoiceTemplate.moveId.setText("رقم الفاتورة: " + App.printInvoice.getMoveHeader().getMove_ID());
        binding.invoiceTemplate.SellingType.setText("نوع الدفع: " + App.printInvoice.getMoveHeader().getCashTypeName());
        binding.invoiceTemplate.invoiceDate.setText("التاريخ: " + App.printInvoice.getMoveHeader().getCreateDate().replace(".000", ""));
        binding.invoiceTemplate.dealerName.setText("المستخدم: " + App.printInvoice.getMoveHeader().getWorkerName());
        binding.invoiceTemplate.tradeRecord.setText("السجل التجاري" + "\n" + App.printInvoice.getMoveHeader().getTradeRecoredNo());
        binding.invoiceTemplate.taxCardNo.setText("رقم التسجيل" + "\n" + App.printInvoice.getMoveHeader().getTaxeCardNo());
        if (!App.customerBalance.isEmpty()) {
            binding.invoiceTemplate.view511.setVisibility(View.VISIBLE);
            binding.invoiceTemplate.clientBalance.setText(App.customerBalance);
            binding.invoiceTemplate.clientBalance.setVisibility(View.VISIBLE);
        }
        if (App.printInvoice.getMoveHeader().getDealerName() != null) {
            binding.invoiceTemplate.dealerName2.setText("العميل: " + App.printInvoice.getMoveHeader().getDealerName());
        } else {
            binding.invoiceTemplate.dealerName2.setVisibility(View.GONE);
        }
        if (App.printInvoice.getMoveHeader().getSaleManName() != null) {
            binding.invoiceTemplate.saleMan.setText("المندوب: " + App.printInvoice.getMoveHeader().getSaleManName());
        } else {
            binding.invoiceTemplate.saleMan.setVisibility(View.GONE);
        }

        if (App.printInvoice.getMoveHeader().getTableNumber() != null)
            binding.invoiceTemplate.tableNumber.setText("رقم السفرة: " + App.printInvoice.getMoveHeader().getTableNumber());
        else
            binding.invoiceTemplate.tableNumber.setText("نوع البيع: " + App.printInvoice.getMoveHeader().getSaleTypeName());

        binding.invoiceTemplate.recyclerView.setAdapter(new PrintingLinesAdapter(App.printInvoice.getMoveLines()));
        binding.invoiceTemplate.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.invoiceTemplate.textView33.setText("الإجمالى  " + roundTwoDecimals(Double.parseDouble(App.printInvoice.getMoveHeader().getTotalLinesValueAfterDisc())));
        if (App.printInvoice.getMoveHeader().getDeliveryValue() != null)
            binding.invoiceTemplate.textView37.setText("الخدمة  " + roundTwoDecimals(Double.parseDouble(App.printInvoice.getMoveHeader().getDeliveryValue())));
        else
            binding.invoiceTemplate.textView37.setText("الخدمة  " + roundTwoDecimals(Double.parseDouble(App.printInvoice.getMoveHeader().getServiceValue())));

        binding.invoiceTemplate.textView38.setText("الخصم  " + roundTwoDecimals(Double.parseDouble(App.printInvoice.getMoveHeader().getBasicDiscountVal())));
        binding.invoiceTemplate.textView39.setText("الضريبة  " + roundTwoDecimals(Double.parseDouble(App.printInvoice.getMoveHeader().getBasicTaxVal()) + Double.parseDouble(App.printInvoice.getMoveHeader().getTotalLinesTaxVal())));
        binding.invoiceTemplate.textView40.setText("المطلوب  " + roundTwoDecimals(Double.parseDouble(App.printInvoice.getMoveHeader().getNetValue())));
        binding.invoiceTemplate.textView41.setText("المتبقى  " + roundTwoDecimals(Double.parseDouble(App.printInvoice.getMoveHeader().getRemainValue())));
        binding.invoiceTemplate.textView42.setText("المدفوع  " + roundTwoDecimals(Double.parseDouble(App.printInvoice.getMoveHeader().getPaidValue())));
        binding.invoiceTemplate.textView43.setText(App.printInvoice.getMoveHeader().getBranchAddress());
        if (!App.printInvoice.getMoveHeader().getTel1().isEmpty())
            binding.invoiceTemplate.textView45.setText(App.printInvoice.getMoveHeader().getTel1());
        else
            binding.invoiceTemplate.textView45.setVisibility(View.GONE);

        if (!App.printInvoice.getMoveHeader().getTel2().isEmpty())
            binding.invoiceTemplate.textView44.setText(App.printInvoice.getMoveHeader().getTel2());
        else
            binding.invoiceTemplate.textView44.setVisibility(View.GONE);
        checkPermission();
        findViewById(R.id.invoiceTemplate).setVisibility(View.VISIBLE);
    }

    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", d));
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 1);
        }
    }
}
