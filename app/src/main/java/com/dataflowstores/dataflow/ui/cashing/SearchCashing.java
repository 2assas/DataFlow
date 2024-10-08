package com.dataflowstores.dataflow.ui.cashing;

import static com.dataflowstores.dataflow.App.theme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ViewModels.PrintInvoiceVM;
import com.dataflowstores.dataflow.databinding.SearchCashingBinding;
import com.dataflowstores.dataflow.ui.BaseActivity;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.invoice.PrintScreen;

import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.service.PosprinterService;

public class SearchCashing extends BaseActivity {
    private static final String TAG = "Search Cashing";
    PrintInvoiceVM printInvoiceVM;
    SearchCashingBinding binding;
    String uuid;
    public static IMyBinder binder;
    int moveType = 16;
    Long branchISN;
    Long workerCBranchISN;
    Long workerCISN;

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //Bind successfully
            binder = (IMyBinder) iBinder;
            Log.e("binder", "connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("disbinder", "disconnected");
        }
    };

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.search_cashing);
            printInvoiceVM = new ViewModelProvider(this).get(PrintInvoiceVM.class);
            uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            moveType = getIntent().getIntExtra("moveType", 16);
            setupViews();
        }
    }

    public void setupViews() {
        @SuppressLint("HardwareIds")
        String uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        printInvoiceVM.toastErrorMutableLiveData.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());
        binding.invoiceTemplate.printButton.setVisibility(View.GONE);
        binding.searchInvoices.setOnClickListener(view -> {
            binding.searchInvoices.onActionViewExpanded(); // Expand the SearchView
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.searchInvoices, InputMethodManager.SHOW_IMPLICIT);
        });
        binding.searchInvoices.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (App.isNetworkAvailable(SearchCashing.this)) {
                    printInvoiceVM.getPrintingData(String.valueOf(branchISN), uuid, s,
                            String.valueOf(workerCBranchISN), String.valueOf(workerCISN), SearchCashing.this, moveType);
                    binding.progressBar.setVisibility(View.VISIBLE);
                } else {
                    App.noConnectionDialog(SearchCashing.this);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                binding.printButton.setOnClickListener(view -> {
                    if (App.isNetworkAvailable(SearchCashing.this)) {
                        takeScreenShot();
                        startActivity(new Intent(SearchCashing.this, PrintScreen.class));
                    } else {
                        App.noConnectionDialog(SearchCashing.this);
                    }
                });
                return false;
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Intent intent = new Intent(this, PosprinterService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
        getInvoiceData();
        if(getIntent().getStringExtra("moveId")!=null){
            branchISN = getIntent().getLongExtra("branchISN", 0);
            workerCBranchISN = getIntent().getLongExtra("workerCBranchISN", 0);
            workerCISN = getIntent().getLongExtra("workerCISN", 0);
            String query = getIntent().getStringExtra("moveId");
            moveType = getIntent().getIntExtra("moveType",0);

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


    public void getInvoiceData() {
        printInvoiceVM.invoiceMutableLiveData = new MutableLiveData<>();
        printInvoiceVM.errorMutableLiveData.observe(this, error -> {
            if (error) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
        printInvoiceVM.invoiceMutableLiveData.observe(this, invoice -> {
            App.printInvoice = invoice;
            binding.progressBar.setVisibility(View.GONE);
            binding.printButton.setVisibility(View.VISIBLE);
            displayPrintingData();
        });
    }

    // this will find a bluetooth printer device
    @SuppressLint("SetTextI18n")
    public void displayPrintingData() {
        binding.invoiceTemplate.branchName.setText(App.printInvoice.getMoveHeader().getBranchName());
        binding.invoiceTemplate.invoiceDate.setText("التاريخ: " + App.printInvoice.getMoveHeader().getCreateDate().replace(".000", ""));
        binding.invoiceTemplate.dealerName.setText("الموظف: " + App.printInvoice.getMoveHeader().getWorkerName());
        binding.invoiceTemplate.foundationName.setText(App.currentUser.getFoundationName());

        switch (moveType) {
            case 16: {
                binding.invoiceTemplate.cashingNumber.setText("إذن صرف رقم " + App.printInvoice.getMoveHeader().getMove_ID());
                App.pdfName = "إذن صرف رقم " + App.printInvoice.getMoveHeader().getMove_ID();
                if (App.printInvoice.getMoveHeader().getBranchNameStockMove() != null && !App.printInvoice.getMoveHeader().getBranchNameStockMove().isEmpty()) {
                    binding.invoiceTemplate.branch.setVisibility(View.VISIBLE);
                    binding.invoiceTemplate.branch.setText("فرع الإستلام: " + App.printInvoice.getMoveHeader().getBranchNameStockMove());
                }
                binding.invoiceTemplate.fromStore.setText("المخزن");
                binding.invoiceTemplate.toStore.setVisibility(View.GONE);
            }
            break;
            case 8: {
                binding.invoiceTemplate.cashingNumber.setText("إهلاكات أصناف رقم " + App.printInvoice.getMoveHeader().getMove_ID());
                App.pdfName= "إهلاكات أصناف رقم " + App.printInvoice.getMoveHeader().getMove_ID();

                binding.invoiceTemplate.fromStore.setText("المخزن");
                binding.invoiceTemplate.toStore.setVisibility(View.GONE);
            }
            break;
            case 17: {
                binding.invoiceTemplate.cashingNumber.setText("إذن إستلام رقم " + App.printInvoice.getMoveHeader().getMove_ID());
                App.pdfName = "إذن إستلام رقم " + App.printInvoice.getMoveHeader().getMove_ID();

                binding.invoiceTemplate.fromStore.setText("المخزن");
                if (App.printInvoice.getMoveHeader().getBranchNameStockMove() != null && !App.printInvoice.getMoveHeader().getBranchNameStockMove().isEmpty()) {
                    binding.invoiceTemplate.branch.setVisibility(View.VISIBLE);
                    binding.invoiceTemplate.branch.setText("فرع الصرف: " + App.printInvoice.getMoveHeader().getBranchNameStockMove());
                }
                binding.invoiceTemplate.toStore.setVisibility(View.GONE);
            }
            break;
            case 21: {
                binding.invoiceTemplate.cashingNumber.setText("طلب أصناف رقم " + App.printInvoice.getMoveHeader().getMove_ID());
                App.pdfName = "طلب أصناف رقم " + App.printInvoice.getMoveHeader().getMove_ID();

                binding.invoiceTemplate.fromStore.setText("المخزن");
                binding.invoiceTemplate.toStore.setVisibility(View.GONE);
            }
            break;
            case 15: {
                binding.invoiceTemplate.cashingNumber.setText("تكوين صنف رقم " + App.printInvoice.getMoveHeader().getMove_ID());
                App.pdfName = "تكوين صنف رقم " + App.printInvoice.getMoveHeader().getMove_ID();
                binding.invoiceTemplate.fromStore.setText("المخزن");
                binding.invoiceTemplate.toStore.setVisibility(View.GONE);
            }
            break;
            case 12: {
                binding.invoiceTemplate.cashingNumber.setText("كميات أول مدة رقم " + App.printInvoice.getMoveHeader().getMove_ID());
                App.pdfName= "كميات أول مدة رقم " + App.printInvoice.getMoveHeader().getMove_ID();
                binding.invoiceTemplate.fromStore.setText("المخزن");
                binding.invoiceTemplate.toStore.setVisibility(View.GONE);
            }
            break;
            case 7: {
                binding.invoiceTemplate.cashingNumber.setText("جرد أصناف رقم " + App.printInvoice.getMoveHeader().getMove_ID());
                App.pdfName= "جرد أصناف رقم " + App.printInvoice.getMoveHeader().getMove_ID();
                binding.invoiceTemplate.fromStore.setText("المخزن");
                binding.invoiceTemplate.toStore.setVisibility(View.GONE);
            }
            break;
            case 14: {
                binding.invoiceTemplate.cashingNumber.setText("تحويل مخزنى رقم " + App.printInvoice.getMoveHeader().getMove_ID());
                App.pdfName= "تحويل مخزنى رقم " + App.printInvoice.getMoveHeader().getMove_ID();

                binding.invoiceTemplate.fromStore.setText("من مخزن");
                binding.invoiceTemplate.toStore.setVisibility(View.VISIBLE);
            }
            break;
        }
        binding.invoiceTemplate.recyclerView.setAdapter(new PrintingCashingAdapter(App.printInvoice.getMoveLines(), moveType));
        binding.invoiceTemplate.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        checkPermission();
        findViewById(R.id.invoiceTemplate).setVisibility(View.VISIBLE);
    }


    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 1);
        }
    }


    private void takeScreenShot() {
        View u = binding.invoiceTemplate.scrollView2;
        int totalHeight = binding.invoiceTemplate.scrollView2.getChildAt(0).getHeight();
        int totalWidth = binding.invoiceTemplate.scrollView2.getChildAt(0).getWidth();
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


}
