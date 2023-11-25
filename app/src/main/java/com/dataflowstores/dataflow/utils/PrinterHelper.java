//package com.dataflowstores.dataflow.utils;
//
//import android.content.Context;
//import android.media.Image;
//import android.util.Log;
//import android.util.Printer;
//
//
//import com.google.common.collect.Lists;
//
//import java.util.List;
//
//public class PrintManager {
//
//    private static final String TAG = "PrintManager";
//
//    private Context context;
//    private List<Printer> printers;
//
//    public PrintManager(Context context) {
//        this.context = context;
//        this.printers = Lists.newArrayList();
//    }
//
//    public void scanForPrinters() {
//        // Scan for printers on the network using the GPP protocol.
//        List<Printer> newPrinters = Printer.s(context, "gpp");
//
//        // Add the new printers to the list of printers.
//        printers.addAll(newPrinters);
//
//        // Log the list of printers.
//        Log.d(TAG, "Printers: " + printers);
//    }
//
//    public void printInvoice(List<Image> images) {
//        // Select the printer to print to.
//        Printer printer = printers.get(0);
//
//        // Send the print job to the printer.
//        printer.printInvoice(images);
//    }
//}