package com.dataflowstores.dataflow.ui.invoice;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import com.dataflowstores.dataflow.databinding.PrintInvoiceBinding;
import com.dataflowstores.dataflow.pojo.users.CustomerData;
import com.dataflowstores.dataflow.ui.DeviceListActivity;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.dataflowstores.dataflow.ui.adapters.PrintingLinesAdapter;

import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.service.PosprinterService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.BitSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class PrintInvoice extends AppCompatActivity implements Runnable {

    private static final String TAG = ".PrintInvoice";
    PrintInvoiceBinding binding;
    PrintInvoiceVM printInvoiceVM;
    //IMyBinder interface，All methods that can be invoked to connect and send data are encapsulated within this interface
    public static IMyBinder binder;
    //bindService connection
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

    Bitmap b2;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    InputStream mmInputStream;
    byte FontStyleVal;
    byte FONT_TYPE;
    PrintingLinesAdapter printingLinesAdapter;
    BluetoothAdapter mBluetoothAdapter;
    BitSet dots;
    private final UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    int printstat;
    BluetoothDevice mBluetoothDevice;
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket socket;
    BluetoothDevice bluetoothDevice;
    OutputStream outputStream;
    InputStream inputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    String value = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            startActivity(new Intent(this, SplashScreen.class));
            finishAffinity();
        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.print_invoice);
            printInvoiceVM = new ViewModelProvider(this).get(PrintInvoiceVM.class);
            printInvoiceVM.toastErrorMutableLiveData.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_LONG).show());
            @SuppressLint("HardwareIds")
            String uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            try {
                printInvoiceVM.getPrintingData(String.valueOf(App.currentUser.getBranchISN()), uuid, String.valueOf(App.invoiceResponse.getData().getMove_ID()),
                        String.valueOf(App.currentUser.getWorkerBranchISN()), String.valueOf(App.currentUser.getWorkerISN()), this, App.resales == 1 ? 3 : 1);
            } catch (Exception e) {
                Toast.makeText(this, "حدث خطأ فى البيانات .. لم يتم تسجيل الفاتورة", Toast.LENGTH_LONG).show();
            }
            ;
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            Intent intent = new Intent(this, PosprinterService.class);
            bindService(intent, conn, BIND_AUTO_CREATE);
            getInvoiceData();
        }
    }

    public void getInvoiceData() {
        printInvoiceVM.invoiceMutableLiveData = new MutableLiveData<>();

        printInvoiceVM.invoiceMutableLiveData.observe(this, invoice -> {
            App.printInvoice = invoice;
            if (App.currentUser.getMobileShowDealerCurrentBalanceInPrint() == 1 && App.customer.getDealerName() != null) {
                @SuppressLint("HardwareIds") String uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                printInvoiceVM.getCustomerBalance(uuid, invoice.getMoveHeader().getDealerISN(), invoice.getMoveHeader().getDealerBranchISN(), invoice.getMoveHeader().getDealerType(),
                        invoice.getMoveHeader().getBranchISN(),
                        invoice.getMoveHeader().getMove_ISN(),
                        invoice.getMoveHeader().getRemainValue(),
                        invoice.getMoveHeader().getNetValue(),
                        App.resales == 1 ? "3" : "1");
            } else {
                binding.progress.setVisibility(View.GONE);
                displayPrintingData();
            }
            App.customer = new CustomerData();

        });

        printInvoiceVM.customerBalanceLiveData.observe(this, customerBalance -> {
            App.customerBalance = customerBalance.getData();
            binding.progress.setVisibility(View.GONE);
            displayPrintingData();
        });
    }

    // this will find a bluetooth printer device
    @SuppressLint("SetTextI18n")
    public void displayPrintingData() {
        binding.foundationName.setText(App.currentUser.getFoundationName());
        binding.branchName.setText(App.currentUser.getBranchName());
        if (App.resales == 1) {
            binding.invoiceNumber.setVisibility(View.GONE);
            binding.resales.setVisibility(View.VISIBLE);
        } else
            binding.invoiceNumber.setText("رقم  الشيك: " + App.printInvoice.getMoveHeader().getBillNumber());
        binding.moveId.setText("رقم الفاتورة: " + App.printInvoice.getMoveHeader().getMove_ID());
        App.pdfName = "رقم الفاتورة: " + App.printInvoice.getMoveHeader().getMove_ID();
        binding.SellingType.setText("نوع الدفع: " + App.printInvoice.getMoveHeader().getCashTypeName());
        binding.invoiceDate.setText("التاريخ: " + App.printInvoice.getMoveHeader().getCreateDate().replace(".000", ""));
        binding.dealerName.setText("المستخدم: " + App.printInvoice.getMoveHeader().getWorkerName());
        binding.tradeRecord.setText("السجل التجاري" + "\n" + App.printInvoice.getMoveHeader().getTradeRecoredNo());
        binding.taxCardNo.setText("رقم التسجيل" + "\n" + App.printInvoice.getMoveHeader().getTaxeCardNo());
        if (App.customerBalance != null && !App.customerBalance.isEmpty()) {
            binding.clientBalance.setText(App.customerBalance);
            binding.clientBalance.setVisibility(View.VISIBLE);
            binding.view511.setVisibility(View.VISIBLE);
        }
        if (App.printInvoice.getMoveHeader().getDealerName() != null) {
            binding.dealerName2.setText("العميل: " + App.printInvoice.getMoveHeader().getDealerName());
        } else {
            binding.dealerName2.setVisibility(View.GONE);
        }
        if (App.printInvoice.getMoveHeader().getSaleManName() != null) {
            binding.saleMan.setText("المندوب: " + App.printInvoice.getMoveHeader().getSaleManName());
        } else {
            binding.saleMan.setVisibility(View.GONE);
        }
        if (App.printInvoice.getMoveHeader().getTableNumber() != null)
            binding.tableNumber.setText("رقم السفرة: " + App.printInvoice.getMoveHeader().getTableNumber());
        else
            binding.tableNumber.setText("نوع البيع: " + App.printInvoice.getMoveHeader().getSaleTypeName());

//        ArrayList<MoveLines> testList= new ArrayList<>();
//        for(int i=0; i<50; i++)
//            testList.addAll(App.printInvoice.getMoveLines());
//
//        App.printInvoice.setMoveLines(testList);

        binding.recyclerView.setAdapter(new PrintingLinesAdapter(App.printInvoice.getMoveLines()));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.textView33.setText("الإجمالى  " + roundTwoDecimals(Double.parseDouble(App.printInvoice.getMoveHeader().getTotalLinesValueAfterDisc())));
        binding.textView37.setText("الخدمة  " + roundTwoDecimals(Double.parseDouble(App.printInvoice.getMoveHeader().getServiceValue()) + Double.parseDouble(App.printInvoice.getMoveHeader().getDeliveryValue())));
        binding.textView38.setText("الخصم  " + roundTwoDecimals(Double.parseDouble(App.printInvoice.getMoveHeader().getBasicDiscountVal())));
        binding.textView39.setText("الضريبة  " + roundTwoDecimals(Double.parseDouble(App.printInvoice.getMoveHeader().getBasicTaxVal()) + Double.parseDouble(App.printInvoice.getMoveHeader().getTotalLinesTaxVal())));
        binding.textView40.setText("المطلوب  " + roundTwoDecimals(Double.parseDouble(App.printInvoice.getMoveHeader().getNetValue())));
        binding.textView41.setText("المتبقى  " + roundTwoDecimals(Double.parseDouble(App.printInvoice.getMoveHeader().getRemainValue())));
        binding.textView42.setText("المدفوع  " + roundTwoDecimals(Double.parseDouble(App.printInvoice.getMoveHeader().getPaidValue())));
        binding.textView43.setText(App.printInvoice.getMoveHeader().getBranchAddress());

        if (!App.printInvoice.getMoveHeader().getTel1().isEmpty())
            binding.textView45.setText(App.printInvoice.getMoveHeader().getTel1());
        else
            binding.textView45.setVisibility(View.GONE);

        if (!App.printInvoice.getMoveHeader().getTel2().isEmpty())
            binding.textView44.setText(App.printInvoice.getMoveHeader().getTel2());
        else
            binding.textView44.setVisibility(View.GONE);
        checkPermission();
        Handler handler = new Handler();
        handler.postDelayed(this::printInvoice, 1000);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("هل تريد الخروج؟")
                .setMessage("تأكد من طباعة الفاتوره قبل الخروج")
                .setPositiveButton("الخروج", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    try {
                        if (mBluetoothSocket != null)
                            mBluetoothSocket.close();
                    } catch (Exception e) {
                        Log.e("Tag", "Exe ", e);
                    }
                    setResult(RESULT_CANCELED);
                    finish();
                })
                .setNegativeButton("البقاء", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", d));
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 1);
        }
    }

    private void printInvoice() {
        binding.scanButton.setVisibility(View.GONE);
        binding.printButton.setVisibility(View.GONE);
        takeScreenShot();
        startActivity(new Intent(PrintInvoice.this, PrintScreen.class));
        finish();
    }

    private void takeScreenShot() {
        View u = binding.scrollView2;
        int totalHeight = binding.scrollView2.getChildAt(0).getHeight();
        int totalWidth = binding.scrollView2.getChildAt(0).getWidth();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /* Terminate bluetooth connection and close all sockets opened */
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }


    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(PrintInvoice.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(PrintInvoice.this, "Not connected to any device", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
            Log.e("checkBluetooth", "gotSocket");
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            binding.scanButton.setText("");
            binding.scanButton.setText("Connected");
            binding.scanButton.setTextColor(Color.rgb(97, 170, 74));
            binding.scanButton.setEnabled(true);
            binding.scanButton.setText("Disconnect");


        }
    };

    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = inputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                inputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                binding.scanButton.setText(data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
