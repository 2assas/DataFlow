package com.dataflowstores.dataflow.ui.payments;

import static com.dataflowstores.dataflow.App.theme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.SearchPaymentBinding;
import com.dataflowstores.dataflow.pojo.receipts.ReceiptData;
import com.dataflowstores.dataflow.pojo.receipts.ReceiptModel;
import com.dataflowstores.dataflow.ui.BaseActivity;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.invoice.PrintScreen;

import java.util.Locale;
import java.util.Objects;

public class SearchPayments extends BaseActivity {
    SearchPaymentBinding binding;
    PaymentsViewModel viewModel;
    String uuid;
    Long branchISN;
    Long workerCBranchISN;
    Long workerCISN;
    ReceiptModel paymentModel;


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.search_payment);
            viewModel = new ViewModelProvider(this).get(PaymentsViewModel.class);
            uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            setupViews();
            observeData();
            checkPermission();
        }
    }


    public void setupViews() {
        viewModel.toastErrorMutableLiveData.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());
        binding.searchInvoices.setOnClickListener(view -> {
            binding.searchInvoices.onActionViewExpanded(); // Expand the SearchView
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.searchInvoices, InputMethodManager.SHOW_IMPLICIT);
        });
        binding.searchInvoices.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (App.isNetworkAvailable(SearchPayments.this)) {
                    viewModel.getPayment(branchISN, uuid, s, workerCBranchISN, workerCISN, 1);
                    binding.progressBar.setVisibility(View.VISIBLE);

                } else {
                    App.noConnectionDialog(SearchPayments.this);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                binding.printButton.setOnClickListener(view -> {
                    if (App.isNetworkAvailable(SearchPayments.this)) {
                        takeScreenShot();
                        startActivity(new Intent(SearchPayments.this, PrintScreen.class));
                    } else {
                        App.noConnectionDialog(SearchPayments.this);
                    }
                });
                return false;
            }
        });

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
        View u = binding.invoiceTemplate.printReceipt;
        int totalHeight = binding.invoiceTemplate.printReceipt.getChildAt(0).getHeight();
        int totalWidth = binding.invoiceTemplate.printReceipt.getChildAt(0).getWidth();
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

    public void observeData() {
        viewModel.paymentModelMutableLiveData.observe(this, payment -> {
            if (payment.getData().size() != 0) {
                binding.noResults.setVisibility(View.GONE);
                paymentModel = payment;
                if (App.currentUser.getMobileShowDealerCurrentBalanceInPrint() == 1 && !Objects.equals(paymentModel.getData().get(0).getDealerISN(), "0")
                        && !Objects.equals(paymentModel.getData().get(0).getDealerISN(), "0") && !Objects.equals(paymentModel.getData().get(0).getDealerISN(), "0")
                ) {
                    ReceiptData receiptData = paymentModel.getData().get(0);
                    viewModel.getCustomerBalance(uuid, receiptData.getDealerISN(),
                            receiptData.getDealerBranchISN(), receiptData.getDealerType(),
                            receiptData.getBranchISN(), receiptData.getMove_ISN(), receiptData.getRemainValue(), receiptData.getNetValue(), receiptData.getMoveType());
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.printButton.setVisibility(View.VISIBLE);
                    fillData();
                }
            } else {
                binding.noResults.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        viewModel.customerBalanceLiveData.observe(this, customerBalance -> {
            App.customerBalance = customerBalance.getMessage();
            binding.progressBar.setVisibility(View.GONE);
            binding.printButton.setVisibility(View.VISIBLE);
            fillData();
        });
    }

    @SuppressLint("SetTextI18n")
    public void fillData() {
        binding.invoiceTemplate.getRoot().setVisibility(View.VISIBLE);
        binding.invoiceTemplate.clientName.setText("المورد: " + paymentModel.getData().get(0).getDealerName());
        if (paymentModel.getData().get(0).getSaleManName() != null)
            binding.invoiceTemplate.saleManName.setText("المندوب: " + paymentModel.getData().get(0).getSaleManName());
        else
            binding.invoiceTemplate.saleManName.setVisibility(View.GONE);
        binding.invoiceTemplate.foundationName.setText(App.currentUser.getFoundationName());

        binding.invoiceTemplate.date.setText("التاريخ: " + paymentModel.getData().get(0).getCreateDate());
        binding.invoiceTemplate.receiptTotal.setText(String.format(Locale.US, "%.2f", Float.parseFloat(paymentModel.getData().get(0).getNetValue())) + " جنيه");
        binding.invoiceTemplate.receiptNotes.setText("ملاحضات \n" + paymentModel.getData().get(0).getHeaderNotes());
        binding.invoiceTemplate.paymentMethod.setText(paymentModel.getData().get(0).getCashTypeName());
        binding.invoiceTemplate.tradeRecord2.setText("السجل التجاري" + "\n" +
                paymentModel.getData().get(0).getTradeRecoredNo());
        binding.invoiceTemplate.taxCardNo2.setText("رقم التسجيل" + "\n" +
                paymentModel.getData().get(0).getTaxeCardNo());
        binding.invoiceTemplate.moveId.setText("رقم المدفوع: " + paymentModel.getData().get(0).getMoveId());
        App.pdfName = "رقم المدفوع: " + paymentModel.getData().get(0).getMoveId();
        binding.invoiceTemplate.clientAddress.setText(paymentModel.getData().get(0).getBranchAddress());
        if (paymentModel.getData().get(0).getTel1() != null || paymentModel.getData().get(0).getTel1().isEmpty())
            binding.invoiceTemplate.tel1.setText(paymentModel.getData().get(0).getTel1());
        else
            binding.invoiceTemplate.tel1.setVisibility(View.GONE);
        if (paymentModel.getData().get(0).getTel2() != null || paymentModel.getData().get(0).getTel2().isEmpty())
            binding.invoiceTemplate.tel2.setText(paymentModel.getData().get(0).getTel2());
        else
            binding.invoiceTemplate.tel2.setVisibility(View.GONE);
        if (!App.customerBalance.isEmpty()) {
            binding.invoiceTemplate.clientBalance.setText(App.customerBalance);
            binding.invoiceTemplate.clientBalance.setVisibility(View.VISIBLE);
            binding.invoiceTemplate.view19.setVisibility(View.VISIBLE);
            App.customerBalance = "";
        } else {
            binding.invoiceTemplate.clientBalance.setVisibility(View.GONE);
            binding.invoiceTemplate.view19.setVisibility(View.GONE);
        }
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 1);
        }
    }

}
