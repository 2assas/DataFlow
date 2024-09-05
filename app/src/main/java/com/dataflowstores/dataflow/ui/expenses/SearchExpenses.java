package com.dataflowstores.dataflow.ui.expenses;

import static com.dataflowstores.dataflow.App.theme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.SearchExpensesBinding;
import com.dataflowstores.dataflow.pojo.expenses.ExpenseData;
import com.dataflowstores.dataflow.ui.BaseActivity;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.invoice.PrintScreen;

import java.util.Locale;
import java.util.Objects;

public class SearchExpenses extends BaseActivity {
    SearchExpensesBinding binding;
    ExpensesViewModel expensesViewModel;
    String uuid;
    Long branchISN;
    Long workerCBranchISN;
    Long workerCISN;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.search_expenses);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else {
            expensesViewModel = new ViewModelProvider(this).get(ExpensesViewModel.class);
            uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            setupViews();
            observeData();
        }
    }

    public void setupViews() {
        expensesViewModel.toastErrorMutableLiveData.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());
        searchInvoice();
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

    private void searchInvoice() {
        binding.searchInvoices.setOnClickListener(view -> {
            binding.searchInvoices.onActionViewExpanded(); // Expand the SearchView
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.searchInvoices, InputMethodManager.SHOW_IMPLICIT);
        });
        binding.searchInvoices.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (App.isNetworkAvailable(SearchExpenses.this)) {
                    expensesViewModel.getExpenses(branchISN, uuid, s, workerCBranchISN, workerCISN, 1);
                    binding.progressBar.setVisibility(View.VISIBLE);

                } else {
                    App.noConnectionDialog(SearchExpenses.this);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                binding.noResults.setVisibility(View.GONE);
                binding.printButton.setOnClickListener(view -> {
                    if (App.isNetworkAvailable(SearchExpenses.this)) {
                        takeScreenShot();
                        startActivity(new Intent(SearchExpenses.this, PrintScreen.class));
                    } else {
                        App.noConnectionDialog(SearchExpenses.this);
                    }
                });
                return false;
            }
        });

    }

    public void observeData() {
        expensesViewModel.expensesModelMutableLiveData.observe(this, receiptModel -> {
            if (receiptModel.getData() != null) {
                binding.noResults.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                binding.printButton.setVisibility(View.VISIBLE);
                binding.expTemplate.getRoot().setVisibility(View.VISIBLE);
                fillData(receiptModel.getData().get(0));
            } else {
                binding.noResults.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }


    @SuppressLint("SetTextI18n")
    public void fillData(ExpenseData expenseData) {
        binding.expTemplate.mainExp.setText("المصروف الرئيسي: " + expenseData.getExpMenuName());
        App.pdfName = "المصروف الرئيسي: " + expenseData.getExpMenuName() + " رقم المصروف" + expenseData.getMoveID();

        if (expenseData.getSubExpMenuName() != null)
            binding.expTemplate.subExp.setText("المصروف الفرعي: " + expenseData.getSubExpMenuName());
        if (expenseData.getSelectedWorkerName() != null)
            binding.expTemplate.worker.setText("الموظف: " + expenseData.getSelectedWorkerName());
        if (expenseData.getMoveID() != null)
            binding.expTemplate.expNum.setText("رقم المصروف: " + expenseData.getMoveID());
        if (expenseData.getShiftISN() != null && !Objects.equals(expenseData.getShiftISN(), "0"))
            binding.expTemplate.shiftNum.setText("رقم الوردية: " + expenseData.getShiftISN());
        binding.expTemplate.foundationName.setText(App.currentUser.getFoundationName());

        binding.expTemplate.date.setText("التاريخ: " + expenseData.getCreateDate());
//        binding.expTemplate.
        binding.expTemplate.expensesTotal.setText(String.format(Locale.US, "%.3f", Float.parseFloat(expenseData.getNetValue())) + " جنيه");
        binding.expTemplate.receiptNotes.setText("ملاحضات \n" + expenseData.getHeaderNotes());
        binding.expTemplate.paymentMethod.setText(expenseData.getCashTypeName());
//        binding.expTemplate.tradeRecord2.setText("السجل التجاري" + "\n" +
//                expenseData.getTradeRecoredNo());
//        binding.expTemplate.taxCardNo2.setText("رقم التسجيل" + "\n" +
//                expenseData.getTaxeCardNo());
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


}
