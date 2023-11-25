package com.dataflowstores.dataflow.ui.invoice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.utils.Conts;
import com.dataflowstores.dataflow.utils.DeviceReceiver;
import com.google.android.material.snackbar.Snackbar;

import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.posprinterface.UiExecute;
import net.posprinter.service.PosprinterService;
import net.posprinter.utils.BitmapToByteData;
import net.posprinter.utils.DataForSendToPrinterPos80;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class PrintScreen extends AppCompatActivity implements View.OnClickListener {
    public static String DISCONNECT = "com.posconsend.net.disconnetct";
    /*
    let the printer print bitmap
    */
    List<Bitmap> segments;
    private Bitmap b1;//grey-scale bitmap
    private Bitmap b2;//compress bitmap
    //IMyBinder interface，All methods that can be invoked to connect and send data are encapsulated within this interface
    public static IMyBinder binder;
    //bindService connection
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //Bind successfully
            Log.e("checkBinder", "Begin");
            binder = (IMyBinder) iBinder;
            Log.e("binder", "connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("disbinder", "disconnected");
        }
    };
    public static boolean ISCONNECT;
    Button BTCon,//connection button
            BTDisconnect,//disconnect button
            BTpos,
            BtSb;// start posprint button
    EditText showET;// show edittext
    CoordinatorLayout container;

    private View dialogView;
    BluetoothAdapter bluetoothAdapter;

    private ArrayAdapter<String> adapter1, adapter2, adapter3;//usb adapter
    private ListView lv1, lv2, lv_usb;
    private ArrayList<String> deviceList_bonded = new ArrayList<>();//bonded list
    private ArrayList<String> deviceList_found = new ArrayList<>();//found list
    private Button btn_scan; //scan button
    private LinearLayout LLlayout;
    ArrayList<Bitmap> printBitmaps = new ArrayList<>();
    AlertDialog dialog;
    String mac;
    int pos;
    private DeviceReceiver myDevice;
    ImageView printImg, invoicePic, sharePdf;
    Receiver netReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.printer_screen);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else {
            // bind service，get ImyBinder object
            try {
                Intent intent = new Intent(this, PosprinterService.class);
                bindService(intent, conn, BIND_AUTO_CREATE);
                //init view
                //SetListener
                initView();
                setlistener();
                netReciever = new Receiver();
                registerReceiver(netReciever, new IntentFilter(PrintScreen.DISCONNECT));
//        Tiny.getInstance().init(getApplication());
                SharedPreferences prefs = getSharedPreferences("SavePrinter", MODE_PRIVATE);
                String printerMac = prefs.getString("printerMac", "");
                Log.e("checkPrinter", printerMac + "s ss s");
                showET.setText(printerMac);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if (!printerMac.isEmpty()) {
                        connetBle();
                    }
                }, 1000);
            } catch (Exception ex) {
                Log.e("Exception", ex.toString());
            }
        }
    }

    private void initView() {
        BTCon = (Button) findViewById(R.id.buttonConnect);
        BTDisconnect = (Button) findViewById(R.id.buttonDisconnect);
        BtSb = (Button) findViewById(R.id.buttonSB);
        showET = (EditText) findViewById(R.id.showET);
        printImg = (ImageView) findViewById(R.id.printButton);
        sharePdf = (ImageView) findViewById(R.id.sharePDF);
        container = (CoordinatorLayout) findViewById(R.id.coordinator);
        invoicePic = findViewById(R.id.invoiceImage);
        App.printBitmap = convertGreyImg(App.printBitmap);
//        Log.e("checkBitmapHeight",String.valueOf(App.printBitmap.getHeight()) + " check solution: "+ splitBitmap(App.printBitmap, 400));
//        handleInvoiceHeight();
        invoicePic.setImageBitmap(App.printBitmap);

        final List<Bitmap> imageList = new ArrayList<>(splitBitmap(App.printBitmap, 800));
        Handler handler = new Handler();

        printImg.setOnClickListener(view -> {
            if (ISCONNECT) {
                for (int i = 0; i < imageList.size(); i++) {
                    int finalI = i;
                    handler.postDelayed(() -> {
                        Log.e("checkForLoop", "Final I = " + finalI);
                        printpicCode(imageList.get(finalI));
                    }, i * 4000);
                }
            } else {
                showSnackbar(getString(R.string.connect_first));
            }
        });


        sharePdf.setOnClickListener(view -> {
            createAndSharePDF();
        });


    }

    private void createAndSharePDF() {
        final List<Bitmap> bitmapList = new ArrayList<>(splitBitmap(App.printBitmap, 1650));
        // Create a new PdfDocument
        PdfDocument pdfDocument = new PdfDocument();
        String pdfName = App.pdfName.isEmpty() ? "generated_document" : App.pdfName;
        // Set the file path where the PDF file will be saved
        File pdfFile = new File(getExternalFilesDir(null), pdfName + ".pdf");

        try {
            for (int i = 0; i < bitmapList.size(); i++) {
                Bitmap bitmap = bitmapList.get(i);
                // Create a PageInfo for each bitmap
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), i + 1).create();

                // Start a page
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                // Draw the bitmap on the page
                Canvas canvas = page.getCanvas();
                Paint paint = new Paint();
                canvas.drawBitmap(bitmap, 0, 0, paint);

                // Finish the page
                pdfDocument.finishPage(page);
            }

            // Create an output stream
            OutputStream outputStream = new FileOutputStream(pdfFile);

            // Write the document content to the output stream
            pdfDocument.writeTo(outputStream);

            // Close the output stream
            outputStream.close();

            // Close the document
            pdfDocument.close();

            // Now you can share the generated PDF file via a chooser dialog

            // Add the path of the PDF file to the intent
            Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", pdfFile);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

            // Use createChooser to present the available sharing options
            Intent chooser = Intent.createChooser(shareIntent, "Share PDF via");

            // Verify that the intent will resolve to at least one activity
            if (shareIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooser);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setlistener() {
        BTCon.setOnClickListener(this);
        BTDisconnect.setOnClickListener(this);
        BtSb.setOnClickListener(this);
        BtSb.setVisibility(View.VISIBLE);
        showET.setHint(getString(R.string.hint));
        showET.setEnabled(false);
    }

    private List<Bitmap> splitBitmap(Bitmap printBitmap, int pageHeight) {
        segments = new ArrayList<>();
        if (printBitmap == null)
            return segments;

        int height = printBitmap.getHeight();
        int width = printBitmap.getWidth();

        for (int i = 0; i < height; i += pageHeight) {
            i = Math.min(i, height);
            int limit = i + pageHeight >= height ? height - i : pageHeight;
            Bitmap bitmap = Bitmap.createBitmap(printBitmap, 0, i, width, limit);
            segments.add(bitmap);
        }
        return segments;
    }


    public static int math(float f) {
        int c = (int) ((f) + 0.999999f);
        float n = f + 0.999999f;
        return (n - c) % 2 == 0 ? (int) f : c;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        //connect button
        if (id == R.id.buttonConnect) {
            //bluetooth connection
            connetBle();
        }
        //device button
        if (id == R.id.buttonSB) {
            setBluetooth();
            BTCon.setText(getString(R.string.connect));
        }
        //disconnect
        if (id == R.id.buttonDisconnect) {
            if (ISCONNECT) {
                Log.e("checkDisconnect", " Disconnected");
                binder.disconnectCurrentPort(new UiExecute() {
                    @Override
                    public void onsucess() {
                        showSnackbar(getString(R.string.toast_discon_success));
                        showET.setText("");
                        BTCon.setText(getString(R.string.connect));
                    }

                    @Override
                    public void onfailed() {
                        showSnackbar(getString(R.string.toast_discon_faile));
                    }
                });
            } else {
                showSnackbar(getString(R.string.toast_present_con));
            }
        }
    }

    /**
     * convert grey image
     *
     * @param img bitmap
     * @return data
     */
    public Bitmap convertGreyImg(Bitmap img) {
        int width = img.getWidth();
        int height = img.getHeight();
        Log.e("printError", width + " ss " + height);
        int[] pixels = new int[width * height];
        img.getPixels(pixels, 0, width, 0, 0, width, height);
        //The arithmetic average of a grayscale image; a threshold
        double redSum = 0, greenSum = 0, blueSun = 0;
        double total = width * height;
        Log.e("printError", total + " ss ");

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                redSum += red;
                greenSum += green;
                blueSun += blue;
            }
        }
        int m = (int) (redSum / total);
        //Conversion monochrome diagram
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                int alpha1 = 0xFF << 24;
                int red = ((grey & 0x00FF0000) >> 16);
                int green;
                int blue;
                if (red >= m) {
                    red = green = blue = 255;
                } else {
                    red = green = blue = 0;
                }
                grey = alpha1 | (red << 16) | (green << 8) | blue;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        mBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        Log.e("printingERROR", mBitmap.getHeight() + " && " + mBitmap.getWidth());
        mBitmap = resizeImage(mBitmap, 576, false);
        return mBitmap;
    }

    public static Bitmap resizeImage(Bitmap bitmap, int w, boolean ischecked) {
        Bitmap BitmapOrg = bitmap;
        Bitmap resizedBitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        if (width <= w) {
            return bitmap;
        }
        if (!ischecked) {
            int newWidth = w;
            int newHeight = height * w / width;

            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;

            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // if you want to rotate the Bitmap
            // matrix.postRotate(45);
            resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                    height, matrix, true);
        } else {
            resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, w, height);
        }
        return resizedBitmap;
    }

    private void connetBle() {
        String bleAdrress = showET.getText().toString();
        if (bleAdrress.equals(null) || bleAdrress.equals("")) {
            showSnackbar(getString(R.string.hint));
        } else {
            binder.connectBtPort(bleAdrress, new UiExecute() {
                @Override
                public void onsucess() {
                    ISCONNECT = true;
                    showSnackbar(getString(R.string.con_success));
                    BTCon.setText(getString(R.string.con_success));
                    binder.write(DataForSendToPrinterPos80.openOrCloseAutoReturnPrintState(0x1f), new UiExecute() {
                        @Override
                        public void onsucess() {
                            binder.acceptdatafromprinter(new UiExecute() {
                                @Override
                                public void onsucess() {
//                                    App.lastConnected=bleAdrress;
                                    Log.e("checkSaved", "Saved Last Connection");
                                }

                                @Override
                                public void onfailed() {
                                    ISCONNECT = false;
                                    showSnackbar(getString(R.string.con_has_discon));
                                }
                            });
                        }

                        @Override
                        public void onfailed() {
                            Log.e("checkSaved", "Failed To write Data");
                        }
                    });
                }

                @Override
                public void onfailed() {
                    ISCONNECT = false;
                    showSnackbar(getString(R.string.con_failed));
                }
            });
        }
    }

    public void
    setBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            //open bluetooth
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_CONNECT
                    }, 123);
                    return;
                }
            }
            startActivityForResult(intent, Conts.ENABLE_BLUETOOTH);
        } else {
            showblueboothlist();
        }
    }

    private void showblueboothlist() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                }, 123);
                return;
            }
        }
        if (!bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.startDiscovery();
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        dialogView = inflater.inflate(R.layout.printer_list, null);
        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceList_bonded);
        lv1 = (ListView) dialogView.findViewById(R.id.listView1);
        btn_scan = (Button) dialogView.findViewById(R.id.btn_scan);
        LLlayout = (LinearLayout) dialogView.findViewById(R.id.ll1);
        lv2 = (ListView) dialogView.findViewById(R.id.listView2);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceList_found);
        lv1.setAdapter(adapter1);
        lv2.setAdapter(adapter2);
        dialog = new AlertDialog.Builder(this).setTitle("BLE").setView(dialogView).create();

        dialog.show();

        myDevice = new DeviceReceiver(deviceList_found, adapter2, lv2);
        //register the receiver
        IntentFilter filterStart = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter filterEnd = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(myDevice, filterStart);
        registerReceiver(myDevice, filterEnd);
        setDlistener();
        findAvalibleDevice();
    }

    private void setDlistener() {
        btn_scan.setOnClickListener(v -> LLlayout.setVisibility(View.VISIBLE));
        lv1.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.BLUETOOTH_SCAN,
                                Manifest.permission.BLUETOOTH_CONNECT
                        }, 123);
                        return;
                    }
                }
                if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                String msg = deviceList_bonded.get(arg2);
                mac = msg.substring(msg.length() - 17);
                String name = msg.substring(0, msg.length() - 18);
                //lv1.setSelection(arg2);
                dialog.cancel();
                showET.setText(mac);
                App.lastConnected = mac;
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getSharedPreferences("SavePrinter", MODE_PRIVATE).edit();
                editor.putString("printerMac", mac);
                editor.apply();
                //Log.i("TAG", "mac="+mac);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        //found device and connect device
        lv2.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            // TODO Auto-generated method stub
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.BLUETOOTH_SCAN,
                                Manifest.permission.BLUETOOTH_CONNECT
                        }, 123);
                        return;
                    }
                }
                if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                String msg = deviceList_found.get(arg2);
                mac = msg.substring(msg.length() - 17);
                String name = msg.substring(0, msg.length() - 18);
                //lv2.setSelection(arg2);
                dialog.cancel();
                showET.setText(mac);
                App.lastConnected = mac;
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getSharedPreferences("SavePrinter", MODE_PRIVATE).edit();
                editor.putString("printerMac", mac);
                editor.apply();
                Log.i("TAG", "mac=" + mac);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    /*
    find avaliable device
     */
    private void findAvalibleDevice() {
        // TODO Auto-generated method stub
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT
                }, 123);
                return;
            }
        }
        Set<BluetoothDevice> device = bluetoothAdapter.getBondedDevices();
        deviceList_bonded.clear();
        if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
            adapter1.notifyDataSetChanged();
        }
        if (device.size() > 0) {
            //already
            for (Iterator<BluetoothDevice> it = device.iterator(); it.hasNext(); ) {
                BluetoothDevice btd = it.next();
                deviceList_bonded.add(btd.getName() + '\n' + btd.getAddress());
                adapter1.notifyDataSetChanged();
            }
        } else {
            deviceList_bonded.add("No can be matched to use bluetooth");
            adapter1.notifyDataSetChanged();
        }

    }
    /*
    USB connection
     */

    /**
     * show the massage
     *
     * @param showstring content
     */
    private void showSnackbar(String showstring) {
        Snackbar.make(container, showstring, Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(R.color.semi_transparent)).show();
    }

//    public static PosPrinterDev.PortType portType;//connect type

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    private void printpicCode(final Bitmap printBmp) {
        PrintScreen.binder.writeDataByYouself(new UiExecute() {
            @Override
            public void onsucess() {
                Log.e("Failed", "Success");
            }

            @Override
            public void onfailed() {
                Log.e("Failed", "failed");
            }
        }, () -> {
            List<byte[]> list = new ArrayList<byte[]>();
            list.add(DataForSendToPrinterPos80.initializePrinter());

            list.add(DataForSendToPrinterPos80.printRasterBmp(
                    0, printBmp, BitmapToByteData.BmpType.Threshold, BitmapToByteData.AlignType.Center, 576));
//                list.add(DataForSendToPrinterPos80.printAndFeedForward(3));
            list.add(DataForSendToPrinterPos80.selectCutPagerModerAndCutPager(66, 1));
            list.add(DataForSendToPrinterPos80.initializePrinter());
            return list;
        });
    }

    //    public void printImg(Bitmap printBitmap){
//        Bitmap newBitmap = Bitmap.createBitmap(printBitmap.getWidth(), printBitmap.getHeight(), printBitmap.getConfig());
//        Canvas canvas = new Canvas(newBitmap);
//        canvas.drawColor(Color.WHITE);
//        canvas.drawBitmap(printBitmap, 0, 0, null);
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//        printpicCode(newBitmap);
//
////        Tiny.BitmapCompressOptions options = new Tiny.BitmapCompressOptions();
////        Tiny.getInstance().source(printBitmap).asBitmap().withOptions(options).compress((isSuccess, bitmap) -> {
////        if (isSuccess){
////                printpicCode(bitmap);
////        }else{
////                Log.e("compress", " Failed");
////        }
////        });
//
//
//    }
    /*
    BroadcastReceiver of disconnect
    */
    private class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(PrintScreen.DISCONNECT)) {
                Log.e("Receiver", "Disconnected");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 123:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        showblueboothlist();
                        Log.e("checkLocation", "granteeeed");
                    }
                    break;
                    case Activity.RESULT_CANCELED:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            ActivityCompat.requestPermissions(this, new String[]{
                                    Manifest.permission.BLUETOOTH_SCAN,
                                    Manifest.permission.BLUETOOTH_CONNECT
                            }, 123);
                        }
                        break;
                }
                break;
        }
    }

}