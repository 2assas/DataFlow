package com.dataflowstores.dataflow.ui;

import static com.dataflowstores.dataflow.App.getMoveType;
import static com.dataflowstores.dataflow.pojo.invoice.InvoiceType.ReturnPurchased;
import static com.dataflowstores.dataflow.pojo.invoice.InvoiceType.ReturnSales;
import static com.dataflowstores.dataflow.pojo.invoice.InvoiceType.Sales;

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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ViewModels.PrintInvoiceVM;
import com.dataflowstores.dataflow.databinding.SearchInvoiceBinding;
import com.dataflowstores.dataflow.ui.adapters.PrintingLinesAdapter;
import com.dataflowstores.dataflow.ui.invoice.PrintScreen;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Objects;

public class SearchInvoice extends AppCompatActivity {
    PrintInvoiceVM printInvoiceVM;
    SearchInvoiceBinding binding;
    String uuid;
    int moveType = 1;

    Long branchISN;
    Long workerCBranchISN;
    Long workerCISN;


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
        moveType = getMoveType();
        printInvoiceVM.toastErrorMutableLiveData.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());
        binding.searchInvoices.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (App.isNetworkAvailable(SearchInvoice.this)) {
                    printInvoiceVM.getPrintingData(String.valueOf(branchISN), uuid, s,
                            String.valueOf(workerCBranchISN), String.valueOf(workerCISN), SearchInvoice.this, moveType);
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
        checkPrintMoves();
    }

    private void checkPrintMoves() {
        if (getIntent().getStringExtra("moveId") != null) {
            branchISN = getIntent().getLongExtra("branchISN", 0);
            workerCBranchISN = getIntent().getLongExtra("workerCBranchISN", 0);
            workerCISN = getIntent().getLongExtra("workerCISN", 0);
            String query = getIntent().getStringExtra("moveId");
            binding.searchInvoices.setQuery(query, true);
            binding.searchInvoices.setVisibility(View.GONE);
            binding.back.setVisibility(View.VISIBLE);
            binding.back.setOnClickListener(v -> {
                onBackPressed();
            });
        } else {
            branchISN = App.currentUser.getBranchISN();
            workerCBranchISN = App.currentUser.getWorkerBranchISN();
            workerCISN = App.currentUser.getWorkerISN();
        }
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
            if (App.currentUser.getMobileShowDealerCurrentBalanceInPrint() == 1 && !Objects.equals(invoice.getMoveHeader().getDealerISN(), "0")
                    && !Objects.equals(invoice.getMoveHeader().getDealerBranchISN(), "0") && !Objects.equals(invoice.getMoveHeader().getDealerType(), "0")
            ) {
                printInvoiceVM.getCustomerBalance(uuid, invoice.getMoveHeader().getDealerISN(), invoice.getMoveHeader().getDealerBranchISN(), invoice.getMoveHeader().getDealerType(),
                        invoice.getMoveHeader().getBranchISN(),
                        invoice.getMoveHeader().getMove_ISN(),
                        invoice.getMoveHeader().getRemainValue(),
                        invoice.getMoveHeader().getNetValue(),
                        String.valueOf(moveType)
                );
            } else {
                binding.printButton.setVisibility(View.VISIBLE);
                displayPrintingData();
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void displayPrintingData() {
        if (App.invoiceType == ReturnSales || App.invoiceType == ReturnPurchased) {
            binding.invoiceTemplate.invoiceNumber.setVisibility(View.GONE);
            binding.invoiceTemplate.resales.setVisibility(View.VISIBLE);
            if (App.invoiceType == ReturnSales) {
                binding.invoiceTemplate.resales.setText("مرتجع مبيعات");
            } else {
                binding.invoiceTemplate.resales.setText("مرتجع مشتريات");
            }
        } else
            binding.invoiceTemplate.invoiceNumber.setText("رقم  الشيك: " + App.printInvoice.getMoveHeader().getBillNumber());
        binding.invoiceTemplate.foundationName.setText(App.currentUser.getFoundationName());
        binding.invoiceTemplate.branchName.setText(App.printInvoice.getMoveHeader().getBranchName());
        binding.invoiceTemplate.moveId.setText("رقم الفاتورة: " + App.printInvoice.getMoveHeader().getMove_ID());
        String invoiceName = "";
        switch (App.invoiceType) {
            case Sales:
                invoiceName = "فاتورة مبيعات";
                break;
            case ReturnSales:
                invoiceName = "فاتورة مرتجع مبيعات";
                break;
            case Purchase:
                invoiceName = "فاتورة مشتريات";
                binding.invoiceTemplate.notes.setVisibility(View.GONE);
                binding.invoiceTemplate.invoiceNumber.setVisibility(View.GONE);
                binding.invoiceTemplate.resales.setVisibility(View.VISIBLE);
                binding.invoiceTemplate.resales.setText("مشتريات");
                break;
            case ReturnPurchased:
                invoiceName = "فاتورة مرتجع مشتريات";
                binding.invoiceTemplate.notes.setVisibility(View.GONE);
                binding.invoiceTemplate.invoiceNumber.setVisibility(View.GONE);
                break;
        }
        App.pdfName = invoiceName + " رقم: " + App.printInvoice.getMoveHeader().getMove_ID();
        binding.invoiceTemplate.SellingType.setText("نوع الدفع: " + App.printInvoice.getMoveHeader().getCashTypeName());
        binding.invoiceTemplate.invoiceDate.setText("التاريخ: " + App.printInvoice.getMoveHeader().getCreateDate().replace(".000", ""));
        binding.invoiceTemplate.dealerName.setText("المستخدم: " + App.printInvoice.getMoveHeader().getWorkerName());
        binding.invoiceTemplate.tradeRecord.setText("السجل التجاري" + "\n" + App.printInvoice.getMoveHeader().getTradeRecoredNo());
        binding.invoiceTemplate.taxCardNo.setText("رقم التسجيل" + "\n" + App.printInvoice.getMoveHeader().getTaxeCardNo());

        if (!App.customerBalance.isEmpty()) {
            binding.invoiceTemplate.view511.setVisibility(View.VISIBLE);
            binding.invoiceTemplate.clientBalance.setText(App.customerBalance);
            binding.invoiceTemplate.clientBalance.setVisibility(View.VISIBLE);
            App.customerBalance = "";
        } else {
            binding.invoiceTemplate.clientBalance.setVisibility(View.GONE);
            binding.invoiceTemplate.view511.setVisibility(View.GONE);
        }
        if (App.printInvoice.getMoveHeader().getDealerName() != null) {
            binding.invoiceTemplate.dealerName2.setText("العميل: " + App.printInvoice.getMoveHeader().getDealerName());
            binding.invoiceTemplate.dealerName2.setVisibility(View.VISIBLE);
        } else {
            binding.invoiceTemplate.dealerName2.setVisibility(View.GONE);
        }
        if (App.printInvoice.getMoveHeader().getSaleManName() != null) {
            binding.invoiceTemplate.saleMan.setText("المندوب: " + App.printInvoice.getMoveHeader().getSaleManName());
            binding.invoiceTemplate.saleMan.setVisibility(View.VISIBLE);
        } else {
            binding.invoiceTemplate.saleMan.setVisibility(View.GONE);
        }

        if (App.printInvoice.getMoveHeader().getTableNumber() != null)
            binding.invoiceTemplate.tableNumber.setText("رقم السفرة: " + App.printInvoice.getMoveHeader().getTableNumber());
        else {
            if (App.invoiceType == Sales)
                binding.invoiceTemplate.tableNumber.setText("نوع البيع: " + App.printInvoice.getMoveHeader().getSaleTypeName());
            else
                binding.invoiceTemplate.tableNumber.setVisibility(View.GONE);
        }
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
        binding.invoiceTemplate.notes.setText(App.printInvoice.getMoveHeader().getBranchAddress());
        if (!App.printInvoice.getMoveHeader().getTel1().isEmpty()) {
            binding.invoiceTemplate.textView45.setText(App.printInvoice.getMoveHeader().getTel1());
            binding.invoiceTemplate.textView45.setVisibility(View.VISIBLE);
        } else {
            binding.invoiceTemplate.textView45.setVisibility(View.GONE);
        }
        if (!App.printInvoice.getMoveHeader().getTel2().isEmpty()) {
            binding.invoiceTemplate.textView44.setText(App.printInvoice.getMoveHeader().getTel2());
            binding.invoiceTemplate.textView44.setVisibility(View.VISIBLE);
        } else
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
