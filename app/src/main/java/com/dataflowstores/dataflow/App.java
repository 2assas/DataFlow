package com.dataflowstores.dataflow;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.dataflowstores.dataflow.pojo.financialReport.Data;
import com.dataflowstores.dataflow.pojo.invoice.Invoice;
import com.dataflowstores.dataflow.pojo.invoice.InvoiceResponse;
import com.dataflowstores.dataflow.pojo.invoice.InvoiceType;
import com.dataflowstores.dataflow.pojo.login.UserData;
import com.dataflowstores.dataflow.pojo.product.ProductData;
import com.dataflowstores.dataflow.pojo.receipts.ReceiptModel;
import com.dataflowstores.dataflow.pojo.settings.Banks;
import com.dataflowstores.dataflow.pojo.settings.PriceTypeData;
import com.dataflowstores.dataflow.pojo.settings.SafeDeposit;
import com.dataflowstores.dataflow.pojo.settings.Stores;
import com.dataflowstores.dataflow.pojo.users.CustomerData;
import com.dataflowstores.dataflow.pojo.users.SalesManData;
import com.dataflowstores.dataflow.pojo.workStation.BranchData;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.reports.itemSalesReport.ItemSalesReport;

import java.util.ArrayList;

public class App extends Application {


    public static UserData currentUser=new UserData();
    public static Banks banks = new Banks();
    public static ReceiptModel receiptModel = new ReceiptModel();
    public static PriceTypeData priceType = new PriceTypeData();
    public static ArrayList<PriceTypeData> allPriceType = new ArrayList<>();
    public static SafeDeposit safeDeposit = new SafeDeposit();
    public static Stores stores = new Stores();
    public static Stores storesCashing = new Stores();
    public static CustomerData customer;
    public static SalesManData agent = new SalesManData();
    public static ProductData product = new ProductData();
    public static boolean isEditing = false;
    public static int editingPos = 0;
    public static ArrayList<ProductData> selectedProducts = new ArrayList<>();
    public static InvoiceResponse invoiceResponse = new InvoiceResponse();
    public static Invoice printInvoice = new Invoice();
    public static Bitmap printBitmap;
    public static String lastConnected = "";
    public static String serialNumber = "";
    public static String customerBalance = "";
    public static int specialDiscount = 0;
    public static Data financialReportData = new Data();
    public static InvoiceType invoiceType = null;
    public static String headerNotes = "";
    public static BranchData currentBranch;
    public static ItemSalesReport itemSalesReport;
    public static Integer selectedFoundation = 0;
    public static String pdfName = "";




    public static int getMoveType() {
        switch (App.invoiceType) {
            case ReturnSales:
                return 3;
            case Purchase:
                return 2;
            case ReturnPurchased:
                return 4;
            default:
                return 1;
        }
    }


    public static boolean isNetworkAvailable(Context context) {
        if (context == null) return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true;
                    }
                }
            } else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        Log.i("update_statut", "Network is available : true");
                        return true;
                    }
                } catch (Exception e) {
                    Log.i("update_statut", "" + e.getMessage());
                }
            }
        }
        Log.i("update_statut", "Network is available : FALSE ");
        return false;
    }

    public static <T> int getSleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void noConnectionDialog(Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).
                setTitle(context.getResources().getString(R.string.no_connection_title))
                .setMessage(context.getString(R.string.no_connection))
                .setCancelable(false)
                .setIcon(R.drawable.ic_no_wifi)
                .setNegativeButton("Ok", (dialogInterface, i) -> {
                    if (context instanceof SplashScreen) {
                        ((Activity) context).recreate();
                    } else
                        isNetworkAvailable((context));
                    dialogInterface.dismiss();
                });
        AlertDialog ad = builder.create();
        ad.show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Printooth.INSTANCE.init(this);
    }
//
//    public static Single<Boolean> checkOnlineState() {
//        return
//    }
}

//
//    AtomicBoolean result = new AtomicBoolean(true);
//                    Single.fromCallable(() -> {
//                            NetworkInfo NInfo = connectivityManager.getActiveNetworkInfo();
//                            if (NInfo != null && NInfo.isConnectedOrConnecting()) {
//                            try {
//                            if (InetAddress.getByName("www.google.com").isReachable(600)) {
//                            Log.e("checkConnection", "Connection exists");
//                            // host reachable
//                            result.set(true);
//                            return true;
//                            } else {
//                            Log.e("checkConnection", "No connection");
//                            // host not reachable
//                            result.set(false);
//                            return false;
//                            }
//                            } catch (IOException e) {
//                            Log.e("checkConnection", "No connection Error");
//                            result.set(false);
//                            return false;
//                            }
//                            } else {
//                            return false;
//                            }
//                            }).subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
//                            if (aBoolean) {
//                            Log.e("checkResult", "true");
//                            result.set(true);
//                            } else {
//                            Log.e("checkResult", "False");
//                            result.set(false);
//                            }
//                            });
//                            try {
//                            Thread.sleep(750);
//                            } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                            }
//                            Log.e("checkResult", "result == "+result.get());
//                            return result.get();