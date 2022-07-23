package com.example.dataflow;

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

import com.example.dataflow.pojo.financialReport.Data;
import com.example.dataflow.pojo.financialReport.FinancialReportResponse;
import com.example.dataflow.pojo.invoice.Invoice;
import com.example.dataflow.pojo.invoice.InvoiceResponse;
import com.example.dataflow.pojo.login.UserData;
import com.example.dataflow.pojo.product.Product;
import com.example.dataflow.pojo.product.ProductData;
import com.example.dataflow.pojo.receipts.ReceiptModel;
import com.example.dataflow.pojo.settings.Banks;
import com.example.dataflow.pojo.settings.PriceType;
import com.example.dataflow.pojo.settings.PriceTypeData;
import com.example.dataflow.pojo.settings.SafeDeposit;
import com.example.dataflow.pojo.settings.SafeDepositData;
import com.example.dataflow.pojo.settings.Stores;
import com.example.dataflow.pojo.users.Customer;
import com.example.dataflow.pojo.users.CustomerData;
import com.example.dataflow.pojo.users.SalesManData;
import com.mazenrashed.printooth.Printooth;

import java.util.ArrayList;

public class App extends Application {
    public static UserData currentUser=new UserData();
    public static Banks banks = new Banks();
    public static ReceiptModel receiptModel = new ReceiptModel();
    public static PriceTypeData priceType = new PriceTypeData();
    public static SafeDeposit safeDeposit = new SafeDeposit();
    public static Stores stores = new Stores();
    public static Stores storesCashing = new Stores();
    public static CustomerData customer;
    public static SalesManData agent = new SalesManData();
    public static ProductData product = new ProductData();
    public static boolean isEditing=false;
    public static int editingPos=0;
    public static ArrayList<ProductData> selectedProducts = new ArrayList<>();
    public static InvoiceResponse invoiceResponse= new InvoiceResponse();
    public static Invoice printInvoice= new Invoice();
    public static Bitmap printBitmap;
    public static String lastConnected="";
    public static String serialNumber="";
    public static String customerBalance="";
    public static Data financialReportData = new Data();


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

    public static void noConnectionDialog(Context context){
        new AlertDialog.Builder(context).
                setTitle(context.getResources().getString( R.string.no_connection_title))
                .setMessage(context.getString( R.string.no_connection))
                .setCancelable(false)
                .setIcon(R.drawable.ic_no_wifi)
                .setNegativeButton("Ok", (dialogInterface, i) -> {
                    ((Activity) context).recreate();
                    dialogInterface.dismiss();
                }).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Printooth.INSTANCE.init(this);
    }
}
