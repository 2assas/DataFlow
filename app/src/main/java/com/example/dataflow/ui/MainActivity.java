package com.example.dataflow.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.dataflow.App;
import com.example.dataflow.R;
import com.example.dataflow.ViewModels.SettingVM;
import com.example.dataflow.databinding.HomeScreenBinding;
import com.example.dataflow.pojo.users.CustomerData;
import com.example.dataflow.pojo.users.SalesManData;
import com.example.dataflow.ui.cashing.SearchCashing;
import com.example.dataflow.ui.cashing.SearchProductsCashing;
import com.example.dataflow.ui.invoice.FirstInvoice;
import com.example.dataflow.ui.receipts.ReceiptScreen;
import com.example.dataflow.ui.reports.ReportsScreen;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    HomeScreenBinding binding;
    SettingVM settingVM;
    String uuid;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    boolean banksDone = false, priceTypeDone = false, safeDepositDone = false, storesDone = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        }else{
        setupViews();
        getUserSettings(App.currentUser.getBranchISN(), uuid);
        initToolBarAnNavMenu();
    }
}

    @SuppressLint({"HardwareIds", "SetTextI18n"})
    private void setupViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.home_screen);
        uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        settingVM = new ViewModelProvider(this).get(SettingVM.class);
        binding.mainActivity.exitApp.setOnClickListener(view -> {
            SharedPreferences.Editor editor = getSharedPreferences("SaveLogin", MODE_PRIVATE).edit();
            editor.putString("userName", "");
            editor.putString("password", "");
            editor.apply();
            finish();
        });
        checkPermissions();
        binding.mainActivity.textView.setText(getString(R.string.welcome) + " " + App.currentUser.getWorkerName());
        binding.mainActivity.foundationName.setText(App.currentUser.getFoundationName());
        binding.mainActivity.invoice.setOnClickListener(view -> {
            if (banksDone && priceTypeDone && safeDepositDone && storesDone) {
                if (App.currentUser.getSafeDepositBranchISN() == 0 || App.currentUser.getSafeDepositISN() == 0) {
                    new AlertDialog.Builder(this).
                            setTitle("لا تسطيع عمل فاتورة")
                            .setMessage("برجاء فحص الخزنه الخاصة بكم.")
                            .setCancelable(false)
                            .setIcon(R.drawable.ic_baseline_error_outline_24)
                            .setNegativeButton("حسنا", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            }).show();
                } else if (App.currentUser.getCashierStoreBranchISN() == 0 || App.currentUser.getCashierStoreISN() == 0) {
                    new AlertDialog.Builder(this).
                            setTitle("لا تسطيع عمل فاتورة")
                            .setMessage("برجاء فحص المخزن الخاص بكم")
                            .setCancelable(false)
                            .setIcon(R.drawable.ic_baseline_error_outline_24)
                            .setNegativeButton("حسنا", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            }).show();
                } else
                    startActivity(new Intent(this, FirstInvoice.class));
            }
        });
        binding.mainActivity.reports.setOnClickListener(v -> {
            if (banksDone && priceTypeDone && safeDepositDone && storesDone)
                startActivity(new Intent(this, ReportsScreen.class));
        });
        binding.mainActivity.receipts.setOnClickListener(v -> {
            if (banksDone && priceTypeDone && safeDepositDone && storesDone)
                if (App.safeDeposit.getData() != null)
                    startActivity(new Intent(this, ReceiptScreen.class));
                else
                    new AlertDialog.Builder(this).
                            setTitle("لا توجد لديكم خزنة")
                            .setMessage("برجاء إضافة الخزنه الخاصة بكم.")
                            .setCancelable(false)
                            .setIcon(R.drawable.ic_baseline_error_outline_24)
                            .setNegativeButton("حسنا", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            }).show();
        });
        binding.mainActivity.cashing.setOnClickListener(v -> {
            if (banksDone && priceTypeDone && safeDepositDone && storesDone)
                if (App.safeDeposit.getData() != null) {
                    Intent intent = new Intent(this, SearchProductsCashing.class);
                    intent.putExtra("moveType", 16);
                    intent.putExtra("store", false);
                    App.customer = new CustomerData();
                    App.agent = new SalesManData();
                    startActivity(intent);
                } else
                    new AlertDialog.Builder(this).
                            setTitle("لا توجد لديكم خزنة")
                            .setMessage("برجاء إضافة الخزنه الخاصة بكم.")
                            .setCancelable(false)
                            .setIcon(R.drawable.ic_baseline_error_outline_24)
                            .setNegativeButton("حسنا", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            }).show();
        });
        binding.mainActivity.receiving.setOnClickListener(v -> {
            if (banksDone && priceTypeDone && safeDepositDone && storesDone)
                if (App.safeDeposit.getData() != null) {
                    Intent intent = new Intent(this, SearchProductsCashing.class);
                    intent.putExtra("moveType", 17);
                    intent.putExtra("store", false);
                    App.customer = new CustomerData();
                    App.agent = new SalesManData();
                    startActivity(intent);
                } else
                    new AlertDialog.Builder(this).
                            setTitle("لا توجد لديكم خزنة")
                            .setMessage("برجاء إضافة الخزنه الخاصة بكم.")
                            .setCancelable(false)
                            .setIcon(R.drawable.ic_baseline_error_outline_24)
                            .setNegativeButton("حسنا", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            }).show();
        });
        binding.mainActivity.storeTransfer.setOnClickListener(v -> {
            if (banksDone && priceTypeDone && safeDepositDone && storesDone)
                if (App.safeDeposit.getData() != null) {
                    Intent intent = new Intent(this, SearchProductsCashing.class);
                    intent.putExtra("moveType", 14);
                    intent.putExtra("store", true);
                    App.customer = new CustomerData();
                    App.agent = new SalesManData();
                    startActivity(intent);
                } else
                    new AlertDialog.Builder(this).
                            setTitle("لا توجد لديكم خزنة")
                            .setMessage("برجاء إضافة الخزنه الخاصة بكم.")
                            .setCancelable(false)
                            .setIcon(R.drawable.ic_baseline_error_outline_24)
                            .setNegativeButton("حسنا", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            }).show();
        });
        binding.mainActivity.searchCustomer.setOnClickListener(view -> {
            startActivity(new Intent(this, SearchCustomerBalance.class));
        });
    }

    public void checkPermissions() {
        if (App.currentUser.getMobileStockOut() == 0) {
            binding.mainActivity.cashing.setVisibility(View.GONE);
        }
        if (App.currentUser.getMobileStockIn() == 0) {
            binding.mainActivity.receiving.setVisibility(View.GONE);
        }
        if (App.currentUser.getMobileStoreTransfer() == 0) {
            binding.mainActivity.storeTransfer.setVisibility(View.GONE);
        }
        if (App.currentUser.getMobileDealersBalanceEnquiry() == 0) {
            binding.mainActivity.searchCustomer.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.customerBalance="";
    }

    public void getUserSettings(long branchISN, String uuid) {
        if (App.currentUser.getPermission() == -1) {
            new AlertDialog.Builder(this).
                    setTitle("لا يمكن المتابعة")
                    .setMessage("برجاء فحص صلاحياتك")
                    .setCancelable(false)
                    .setIcon(R.drawable.ic_baseline_error_outline_24)
                    .setNegativeButton("حسنا", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        finish();
                    }).show();
        } else {
            if (App.isNetworkAvailable(this)) {
                settingVM.getBanks(branchISN, uuid);
                settingVM.getPriceType(uuid);
                settingVM.getSafeDeposit(branchISN, uuid);
                settingVM.getStores(branchISN, uuid);
            } else {
                App.noConnectionDialog(this);
            }
        }
        settingVM.banksMutableLiveData.observe(this, banks -> {
            App.banks = banks;
            banksDone = true;
        });
        settingVM.priceTypeMutableLiveData.observe(this, priceType -> {
            App.priceType = priceType;
            priceTypeDone = true;
        });
        settingVM.safeDepositMutableLiveData.observe(this, safeDeposit -> {
            App.safeDeposit = safeDeposit;
            safeDepositDone = true;
        });
        settingVM.storesMutableLiveData.observe(this, stores -> {
            App.stores = stores;
            storesDone = true;
        });
    }

    public void initToolBarAnNavMenu() {
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, 0, 0);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.mainActivity.profileDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ViewCompat.getLayoutDirection(binding.drawerLayout) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    binding.drawerLayout.openDrawer(Gravity.RIGHT);
                } else {
                    binding.drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
        navigationView = findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        if (App.currentUser.getMobileStockOut() == 0) {
            nav_Menu.findItem(R.id.searchCashingPermission).setVisible(false);
        }
        if (App.currentUser.getMobileStockIn() == 0) {
            nav_Menu.findItem(R.id.searchReceivingPermission).setVisible(false);
        }
        if (App.currentUser.getMobileStoreTransfer() == 0) {
            nav_Menu.findItem(R.id.searchStoreTransfer).setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.searchSaleInvoice:
                        startActivity(new Intent(MainActivity.this, SearchInvoice.class));
                        break;
                    case R.id.searchReceiptsInvoice:
                        startActivity(new Intent(MainActivity.this, SearchReceipts.class));
                        break;
                    case R.id.searchCashingPermission:
                        Intent intent1 = new Intent(MainActivity.this, SearchCashing.class);
                        intent1.putExtra("moveType", 16);
                        startActivity(intent1);
                        break;
                    case R.id.searchReceivingPermission:
                        Intent intent2 = new Intent(MainActivity.this, SearchCashing.class);
                        intent2.putExtra("moveType", 17);
                        startActivity(intent2);
                        break;
                    case R.id.searchStoreTransfer:
                        Intent intent3 = new Intent(MainActivity.this, SearchCashing.class);
                        intent3.putExtra("moveType", 14);
                        startActivity(intent3);
                        break;
                }
                return false;
            }
        });
    }


}
