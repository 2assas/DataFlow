package com.dataflowstores.dataflow.ui.home;

import static android.content.Context.MODE_PRIVATE;
import static com.dataflowstores.dataflow.App.theme;
import static com.dataflowstores.dataflow.pojo.invoice.InvoiceType.Purchase;
import static com.dataflowstores.dataflow.pojo.invoice.InvoiceType.ReturnPurchased;
import static com.dataflowstores.dataflow.pojo.invoice.InvoiceType.ReturnSales;
import static com.dataflowstores.dataflow.pojo.invoice.InvoiceType.Sales;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ViewModels.SettingVM;
import com.dataflowstores.dataflow.databinding.HomeScreenBinding;
import com.dataflowstores.dataflow.ui.SearchInvoice;
import com.dataflowstores.dataflow.ui.SearchReceipts;
import com.dataflowstores.dataflow.ui.cashing.SearchCashing;
import com.dataflowstores.dataflow.ui.expenses.SearchExpenses;
import com.dataflowstores.dataflow.ui.payments.SearchPayments;
import com.dataflowstores.dataflow.ui.reports.ReportsFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    HomeScreenBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SettingVM settingVM;
    String uuid;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    boolean banksDone = false, priceTypeDone = false, safeDepositDone = false, storesDone = false;
    FragmentManager manager;
    ProgressDialog progressDialog;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_screen, container, false);
        uuid = Settings.Secure.getString(requireActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        App.uuid=uuid;
        settingVM = new ViewModelProvider(this).get(SettingVM.class);
        manager = requireActivity().getSupportFragmentManager();
        App.selectedProducts=new ArrayList<>();
        setupViews();
        getUserSettings(App.currentUser.getBranchISN(), uuid);
        handleFragments();
        initToolBarAnNavMenu();
        onBackPressed();
        return binding.getRoot();
    }

    private void setupViews() {
        App.invoiceType = null;
        binding.home.exitApp.setOnClickListener(view -> {
            new AlertDialog.Builder(requireActivity()).setTitle("تأكيد الخروج").setMessage("هل تريد الخروج من التطبيق؟").setPositiveButton("خروج", (dialogInterface, i) -> {
                SharedPreferences.Editor editor = requireActivity().getSharedPreferences("AppShared", MODE_PRIVATE).edit();
                editor.putString("userName", "");
                editor.putString("password", "");
                editor.apply();
                App.selectedFoundation = 0;
                requireActivity().finish();
            }).setNegativeButton("البقاء", ((dialogInterface, i) -> dialogInterface.dismiss())).show();
        });
        binding.home.textView.setText(getString(R.string.welcome) + " " + App.currentUser.getWorkerName());
        binding.home.foundationName.setText(App.currentUser.getFoundationName());
        binding.home.branchName.setText(App.currentUser.getBranchName());
        if (!Objects.equals(App.currentUser.getDeviceID(), "0")) {
            binding.home.deviceId.setText(getString(R.string.device_id)+ App.currentUser.getDeviceID());
        } else {
            binding.home.deviceId.setVisibility(View.GONE);
        }
        settingVM.toastErrorMutableLiveData.observe(requireActivity(), s -> Toast.makeText(requireActivity(), s, Toast.LENGTH_LONG).show());
    }

    public void handleFragments() {
        finance();
        invoice();
        storeOperations();
        reports();
    }

    private void finance() {
        binding.home.finance.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity())) {
                if (banksDone && priceTypeDone && safeDepositDone && storesDone)
                    if (App.safeDeposit.getData() != null) {
                        FinanceFragment financeFragment = new FinanceFragment();
                        manager.beginTransaction().replace(R.id.container, financeFragment).addToBackStack("home").commit();
                    } else
                        new AlertDialog.Builder(requireActivity()).setTitle("لا توجد لديكم خزنة").setMessage("برجاء إضافة الخزنه الخاصة بكم.").setCancelable(false).setIcon(R.drawable.ic_baseline_error_outline_24).setNegativeButton("حسنا", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        }).show();
            }
        });
    }

    private void invoice() {
        binding.home.invoice.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity())) {
                if (banksDone && priceTypeDone && safeDepositDone && storesDone) {
                    if (App.currentUser.getSafeDepositBranchISN() == 0 || App.currentUser.getSafeDepositISN() == 0) {
                        new AlertDialog.Builder(requireActivity()).setTitle("لا تسطيع عمل فاتورة").setMessage("برجاء فحص الخزنه الخاصة بكم.").setCancelable(false).setIcon(R.drawable.ic_baseline_error_outline_24).setNegativeButton("حسنا", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        }).show();
                    } else if (App.currentUser.getCashierStoreBranchISN() == 0 || App.currentUser.getCashierStoreISN() == 0) {
                        new AlertDialog.Builder(requireActivity()).setTitle("لا تسطيع عمل فاتورة").setMessage("برجاء فحص المخزن الخاص بكم").setCancelable(false).setIcon(R.drawable.ic_baseline_error_outline_24).setNegativeButton("حسنا", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        }).show();
                    } else {
                        InvoicesFragment invoicesFragment = new InvoicesFragment();
                        manager.beginTransaction().replace(R.id.container, invoicesFragment).addToBackStack("home").commit();
//                    startActivity(new Intent(requireActivity(), FirstInvoice.class));
                    }
                }
            }
        });
    }

    private void storeOperations() {
        binding.home.storeOperations.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity())) {
                if (banksDone && priceTypeDone && safeDepositDone && storesDone)
                    if (App.safeDeposit.getData() != null) {
                        manager.beginTransaction().replace(R.id.container, new StoreOperationsFragment()).addToBackStack("home").commit();
                    } else
                        new AlertDialog.Builder(requireActivity()).setTitle("لا توجد لديكم خزنة").setMessage("برجاء إضافة الخزنه الخاصة بكم.").setCancelable(false).setIcon(R.drawable.ic_baseline_error_outline_24).setNegativeButton("حسنا", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        }).show();
            }
        });
    }

    private void reports() {
        binding.home.reports.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity())) {
                if (banksDone && priceTypeDone && safeDepositDone && storesDone)
                    if (App.safeDeposit.getData() != null) {
                        manager.beginTransaction().replace(R.id.container, new ReportsFragment()).addToBackStack("home").commit();
                    } else
                        new AlertDialog.Builder(requireActivity()).setTitle("لا توجد لديكم خزنة").setMessage("برجاء إضافة الخزنه الخاصة بكم.").setCancelable(false).setIcon(R.drawable.ic_baseline_error_outline_24).setNegativeButton("حسنا", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        }).show();
            }
        });
    }

    public void getUserSettings(long branchISN, String uuid) {
        if (App.currentUser.getPermission() == -1) {
            new AlertDialog.Builder(requireActivity()).
                    setTitle("لا يمكن المتابعة")
                    .setMessage("برجاء فحص صلاحياتك")
                    .setCancelable(false)
                    .setIcon(R.drawable.ic_baseline_error_outline_24)
                    .setNegativeButton("حسنا", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        requireActivity().finish();
                    }).show();
        } else {
            if (App.isNetworkAvailable(requireActivity())) {
                progressDialog = new ProgressDialog(requireActivity());
                progressDialog.setMessage(getString(R.string.loading_please_wait));
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();
                settingVM.getInitialInvoiceApis(branchISN, uuid, 0);
            } else {
                App.noConnectionDialog(requireActivity());
            }
        }
        settingVM.allDone.observe(getViewLifecycleOwner(), aBoolean -> {
            progressDialog.dismiss();
        });

        settingVM.initialAPIsMutableLiveData.observe(getViewLifecycleOwner(), initialAPIs -> {
            App.banks = initialAPIs.getBanks();
            banksDone = true;
            for (int i = 0; i < initialAPIs.getPriceType().getData().size(); i++) {
                if (initialAPIs.getPriceType().getData().get(i).getBranchISN() == App.currentUser.getCashierSellPriceTypeBranchISN() && initialAPIs.getPriceType().getData().get(i).getPricesType_ISN() == App.currentUser.getCashierSellPriceTypeISN()) {
                    App.priceType = initialAPIs.getPriceType().getData().get(i);
                }
            }
            priceTypeDone = true;
            App.safeDeposit = initialAPIs.getSafeDeposit();
            safeDepositDone = true;
            App.stores = initialAPIs.getStores();
            storesDone = true;
        });

    }

    private void onBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.e("checkBack", "home pressed");
                new AlertDialog.Builder(requireActivity()).setTitle("تأكيد الخروج").setMessage("هل تريد الخروج من التطبيق؟").setPositiveButton("خروج", ((dialogInterface, i) -> {
                    App.selectedFoundation = 0;
                    requireActivity().finish();
                })).setNegativeButton("البقاء", ((dialogInterface,                i) -> dialogInterface.dismiss())).show();
            }
        });
    }

    public void initToolBarAnNavMenu() {
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(requireActivity(), binding.drawerLayout, 0, 0);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.home.profileDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ViewCompat.getLayoutDirection(binding.drawerLayout) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    binding.drawerLayout.openDrawer(Gravity.RIGHT);
                } else {
                    binding.drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
        navigationView = binding.navView;
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
        if (!Objects.equals(App.currentUser.getDeviceID(), "0")) {
            nav_Menu.findItem(R.id.nav_device_id).setTitle(App.currentUser.getDeviceID());
        } else {
            nav_Menu.findItem(R.id.nav_device_id_title).setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.sales:
                        if (App.isNetworkAvailable(requireActivity())) {
                            App.invoiceType = Sales;
                            Intent intent = new Intent(requireActivity(), SearchInvoice.class);
                            startActivity(intent);
                            break;
                        }
                    case R.id.returnSales:
                        if (App.isNetworkAvailable(requireActivity())) {
                            App.invoiceType = ReturnSales;
                            Intent intent8 = new Intent(requireActivity(), SearchInvoice.class);
                            startActivity(intent8);
                            break;
                        }
                    case R.id.purchaseOrder:
                        if (App.isNetworkAvailable(requireActivity())) {
                            App.invoiceType = Purchase;
                            Intent intent = new Intent(requireActivity(), SearchInvoice.class);
                            startActivity(intent);
                            break;
                        }
                    case R.id.returnPurchased:
                        if (App.isNetworkAvailable(requireActivity())) {
                            App.invoiceType = ReturnPurchased;
                            Intent intent8 = new Intent(requireActivity(), SearchInvoice.class);
                            startActivity(intent8);
                            break;
                        }
                    case R.id.searchReceiptsInvoice:
                        if (App.isNetworkAvailable(requireActivity())) {
                            startActivity(new Intent(requireActivity(), SearchReceipts.class));
                            break;
                        }
                    case R.id.searchPaymentInvoice:
                        if (App.isNetworkAvailable(requireActivity())) {
                            startActivity(new Intent(requireActivity(), SearchPayments.class));
                            break;
                        }
                    case R.id.searchExpensesInvoice:
                        if (App.isNetworkAvailable(requireActivity())) {
                            startActivity(new Intent(requireActivity(), SearchExpenses.class));
                            break;
                        }
                    case R.id.searchCashingPermission:
                        if (App.isNetworkAvailable(requireActivity())) {
                            Intent intent1 = new Intent(requireActivity(), SearchCashing.class);
                            intent1.putExtra("moveType", 16);
                            startActivity(intent1);
                            break;
                        }
                    case R.id.searchReceivingPermission:
                        if (App.isNetworkAvailable(requireActivity())) {
                            Intent intent2 = new Intent(requireActivity(), SearchCashing.class);
                            intent2.putExtra("moveType", 17);
                            startActivity(intent2);
                            break;
                        }
                    case R.id.searchStoreTransfer: {
                        Intent intent3 = new Intent(requireActivity(), SearchCashing.class);
                        intent3.putExtra("moveType", 14);
                        startActivity(intent3);
                        break;
                    }
                    case R.id.mobileFirstPeriod: {
                        Intent intent4 = new Intent(requireActivity(), SearchCashing.class);
                        intent4.putExtra("moveType", 12);
                        startActivity(intent4);
                        break;
                    }
                    case R.id.mobileLoses:
                        if (App.isNetworkAvailable(requireActivity())) {
                            Intent intent5 = new Intent(requireActivity(), SearchCashing.class);
                            intent5.putExtra("moveType", 8);
                            startActivity(intent5);
                            break;
                        }
                    case R.id.itemCreate:
                        if (App.isNetworkAvailable(requireActivity())) {
                            Intent intent6 = new Intent(requireActivity(), SearchCashing.class);
                            intent6.putExtra("moveType", 15);
                            startActivity(intent6);
                            break;
                        }
                    case R.id.mobileInventory:
                        if (App.isNetworkAvailable(requireActivity())) {
                            Intent intent7 = new Intent(requireActivity(), SearchCashing.class);
                            intent7.putExtra("moveType", 7);
                            startActivity(intent7);
                            break;
                        }
                    case R.id.changeTheme:
                        showThemeSelectionDialog();
                        break;

                }
                return false;
            }
        });
    }

    private void showThemeSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Select Theme").setItems(new CharSequence[]{"النمط الأزرق", "النمط الأصفر"}, (dialogInterface, which) -> {
            switch (which) {
                case 0:
                    if (theme != R.style.AppTheme) {
                        setAppTheme(R.style.AppTheme);
                        requireActivity().recreate();
                    } else dialogInterface.dismiss();
                    break;
                case 1:
                    if (theme != R.style.SecondTheme) {
                        setAppTheme(R.style.SecondTheme);
                        requireActivity().recreate();
                    } else dialogInterface.dismiss();
                    break;
            }
        });
        builder.create().show();
    }

    private void setAppTheme(int themeId) {
        requireActivity().setTheme(themeId);
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppShared", MODE_PRIVATE);
        prefs.edit().putString("theme", themeId == R.style.AppTheme ? "default" : "second").apply(); // Save the selected theme
    }
}

