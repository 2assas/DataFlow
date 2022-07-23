package com.example.dataflow.ui.cashing;

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

import com.example.dataflow.App;
import com.example.dataflow.R;
import com.example.dataflow.ViewModels.PrintInvoiceVM;
import com.example.dataflow.databinding.CachingPrintingBinding;
import com.example.dataflow.ui.DeviceListActivity;
import com.example.dataflow.ui.invoice.PrintScreen;

import net.posprinter.posprinterface.IMyBinder;
import net.posprinter.service.PosprinterService;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class CashingPrinting extends AppCompatActivity implements Runnable {

    private static final String TAG = "Cashing";
    CachingPrintingBinding binding;
    String uuid;
    PrintInvoiceVM printInvoiceVM;
    public static IMyBinder binder;
    int moveType = 16;
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
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothAdapter mBluetoothAdapter;
    private final UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.caching_printing);
        uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        moveType = getIntent().getIntExtra("moveType", 16);
        printInvoiceVM = new ViewModelProvider(this).get(PrintInvoiceVM.class);
        setupViews();
    }

    public void setupViews() {
        @SuppressLint("HardwareIds")
        String uuid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            printInvoiceVM.getPrintingData(String.valueOf(App.currentUser.getBranchISN()), uuid, String.valueOf(App.invoiceResponse.getData().getMove_ID()),
                    String.valueOf(App.currentUser.getWorkerBranchISN()), String.valueOf(App.currentUser.getWorkerISN()), this, moveType);
        } catch (Exception e) {
            Toast.makeText(this, "حدث خطأ فى البيانات .. لم يتم تسجيل الفاتورة", Toast.LENGTH_LONG).show();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Intent intent = new Intent(this, PosprinterService.class);
        bindService(intent, conn, BIND_AUTO_CREATE);
        getInvoiceData();
    }


    public void getInvoiceData() {
        printInvoiceVM.invoiceMutableLiveData = new MutableLiveData<>();
        printInvoiceVM.invoiceMutableLiveData.observe(this, invoice -> {
            binding.progress.setVisibility(View.GONE);
            App.printInvoice = invoice;
            displayPrintingData();
        });
    }

    // this will find a bluetooth printer device
    @SuppressLint("SetTextI18n")
    public void displayPrintingData() {
        binding.foundationName.setText(App.currentUser.getFoundationName());
        binding.branchName.setText(App.currentUser.getBranchName());
        binding.invoiceDate.setText("التاريخ: " + App.printInvoice.getMoveHeader().getCreateDate().replace(".000", ""));
        binding.dealerName.setText("الموظف: " + App.printInvoice.getMoveHeader().getWorkerName());
        switch (moveType){
            case 16: {
                binding.cashingNumber.setText("إذن صرف رقم " + App.printInvoice.getMoveHeader().getMove_ID());
                binding.fromStore.setText("المخزن");
                binding.toStore.setVisibility(View.GONE);
            }
            break;
            case 17: {
                binding.cashingNumber.setText("إذن إستلام رقم " + App.printInvoice.getMoveHeader().getMove_ID());
                binding.fromStore.setText("المخزن");
                binding.toStore.setVisibility(View.GONE);
            }
            break;
            case 14: {
                binding.cashingNumber.setText("تحويل مخزنى رقم " + App.printInvoice.getMoveHeader().getMove_ID());
                binding.fromStore.setText("من مخزن");
                binding.toStore.setVisibility(View.VISIBLE);
            }
            break;
        }
        binding.recyclerView.setAdapter(new PrintingCashingAdapter(App.printInvoice.getMoveLines(), moveType));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, 1);
        }
    }

    private void printInvoice() {
        binding.scanButton.setVisibility(View.GONE);
        binding.printButton.setVisibility(View.GONE);
        takeScreenShot();
        startActivity(new Intent(CashingPrinting.this, PrintScreen.class));
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
                    Intent connectIntent = new Intent(CashingPrinting.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(CashingPrinting.this, "Not connected to any device", Toast.LENGTH_SHORT).show();
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

}

