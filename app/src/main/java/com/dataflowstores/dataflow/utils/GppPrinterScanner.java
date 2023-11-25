package com.dataflowstores.dataflow.utils;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiManager;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintJobInfo;
import android.print.PrintManager;
import android.printservice.PrintService;
import android.printservice.PrinterDiscoverySession;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.List;

public class GppPrinterScanner {
    private static final String TAG = "GppPrinterScanner";


    public static void scanForPrinters(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);

        // Get the list of available printers.
        List<PrintJob> printerList = printManager.getPrintJobs();

        // Iterate through the list of printers and look for printers that support the GPP protocol.
        for (PrintJob printJob : printerList) {
            if (printJob.getInfo().getState() == PrintJobInfo.STATE_STARTED) {
                Log.d(TAG, "Found a GPP printer: " + printJob.getInfo().getLabel());
            }
        }
    }
    private NsdManager mNsdManager;
    private NsdManager.DiscoveryListener mDiscoveryListener = new NsdManager.DiscoveryListener() {
        @Override
        public void onStartDiscoveryFailed(String s, int i) {

        }

        @Override
        public void onStopDiscoveryFailed(String s, int i) {

        }

        @Override
        public void onDiscoveryStarted(String s) {

        }

        @Override
        public void onDiscoveryStopped(String s) {

        }

        @Override
        public void onServiceFound(NsdServiceInfo serviceInfo) {
            Log.d("PrinterDiscovery", "onServiceFound: " + serviceInfo.getServiceName());

//            // Check if the discovered service is a printer service (you might need to adjust the service type)
//            if (serviceInfo.getServiceType().equals("_ipp._tcp")) {
//                // Create a PrintJobInfo object to represent the print job
//                PrintJobInfo printJobInfo = new PrintJobInfo.Builder(serviceInfo.getServiceName())
//                        .setPrinterId(serviceInfo.getServiceName()) // Use the service name as the printer ID
//                        .build();
//
//                // Connect to the printer and submit a print job
//                PrintJob printJob = printManager.print(printJobInfo, new PrintDocumentAdapter() {
//                    // Implement necessary methods for printing, such as onLayout, onWrite, etc.
//                    // This depends on the content you want to print.
//                });
//
//                // You can track the status of the print job if needed
//                if (printJob != null) {
//                    if (printJob.isCompleted()) {
//                        Log.d("PrinterDiscovery", "Print job completed");
//                    } else if (printJob.isFailed()) {
//                        Log.e("PrinterDiscovery", "Print job failed");
//                    }
//                }
//            }
        }

        @Override
        public void onServiceLost(NsdServiceInfo nsdServiceInfo) {

        }
    };
    private void connectToPrinter(NsdServiceInfo serviceInfo) {
        // Create a print service
        PrintService printService = new PrintService() {
            @Nullable
            @Override
            protected PrinterDiscoverySession onCreatePrinterDiscoverySession() {
                return null;

            }

            @Override
            protected void onRequestCancelPrintJob(android.printservice.PrintJob printJob) {
            }

            @Override
            protected void onPrintJobQueued(android.printservice.PrintJob printJob) {

            }
        };


    }
}
