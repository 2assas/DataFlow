package com.example.dataflow.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.print.PrintHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dataflow.App
import com.example.dataflow.R
import com.example.dataflow.ViewModels.PrintInvoiceVM
import com.example.dataflow.databinding.PrintInvoiceBinding
import com.example.dataflow.pojo.invoice.Invoice
import com.example.dataflow.ui.adapters.PrintingLinesAdapter
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.ImagePrintable
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.ui.ScanningActivity
import com.mazenrashed.printooth.utilities.Printing
import com.mazenrashed.printooth.utilities.PrintingCallback
import kotlinx.android.synthetic.main.print_invoice.*
import java.io.ByteArrayOutputStream
import java.io.Flushable
import java.io.IOException
import java.io.OutputStream
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.experimental.or


class PrintingInvoice : AppCompatActivity() , Runnable {
    private var printing :Printing? = null
    var binding: PrintInvoiceBinding? = null
    var printInvoiceVM: PrintInvoiceVM? = null
    var dots =BitSet()
    val REQUEST_CONNECT_DEVICE = 1
    var DISCONNECT = "com.posconsend.net.disconnetct"
    val REQUEST_ENABLE_BT = 2
    var mBluetoothAdapter: BluetoothAdapter? = null
    private var mBluetoothSocket: BluetoothSocket? = null
    var mBluetoothDevice: BluetoothDevice? = null




    var mmOutputStream: OutputStream? = null
    private var b1 //grey-scale bitmap
            : Bitmap? = null
    private var b2 //compress bitmap
            : Bitmap? = null
    private val mBluetoothConnectProgressDialog: ProgressDialog? = null
    private val applicationUUID = UUID
        .fromString("00001101-0000-1000-8000-00805F9B34FB")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Printooth.init(this);
        if (Printooth.hasPairedPrinter())
            printing = Printooth.printer()
        binding = DataBindingUtil.setContentView(this, R.layout.print_invoice)
        printInvoiceVM = ViewModelProvider(this).get(PrintInvoiceVM::class.java)
        @SuppressLint("HardwareIds") val uuid = Settings.Secure.getString(
            contentResolver, Settings.Secure.ANDROID_ID
        )
        printInvoiceVM!!.getPrintingData(
            App.currentUser.branchISN.toString(),
            uuid,
            App.invoiceResponse.data.move_ID.toString(),
            App.currentUser.workerBranchISN.toString(),
            App.currentUser.workerISN.toString(),
            this
        , null)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        getInvoiceData()
        initViews()
    }
    fun getInvoiceData() {
        printInvoiceVM!!.invoiceMutableLiveData = MutableLiveData()
        printInvoiceVM!!.invoiceMutableLiveData.observe(this, { invoice: Invoice? ->
            binding!!.progress.visibility = View.GONE
            App.printInvoice = invoice
            displayPrintingData()
            binding!!.scanButton.setOnClickListener { view -> scanBluetooth() }
            printInvoice()
        })
    }
    private fun printInvoice() {
        binding!!.printButton.setOnClickListener {
                val bitmap = getBitmapFromView(
                    scrollView2,
                    scrollView2.getChildAt(0).height,
                    scrollView2.getChildAt(0).width
                )
                bitmap?.let { it1 -> print_image(it1) }
            }
   }



    @SuppressLint("SetTextI18n")
    fun displayPrintingData() {
        binding!!.foundationName.text = App.currentUser.foundationName
        binding!!.branchName.text = App.currentUser.branchName
        binding!!.invoiceNumber.text = "رقم  الدفع: " + App.printInvoice.moveHeader.billNumber
        binding!!.moveId.text = "رقم الفاتورة: " + App.invoiceResponse.data.move_ID
        binding!!.SellingType.text = "نوع البيع: " + App.printInvoice.moveHeader.cashTypeName
        binding!!.invoiceDate.text =
            "التاريخ: " + App.printInvoice.moveHeader.createDate.replace(".000", "")
        binding!!.dealerName.text = "المستخدم: " + App.printInvoice.moveHeader.dealerName
        if (App.printInvoice.moveHeader.tableNumber != null) binding!!.tableNumber.text =
            "رقم السفرة: " + App.printInvoice.moveHeader.tableNumber else binding!!.tableNumber.visibility =
            View.GONE
        binding!!.recyclerView.adapter = PrintingLinesAdapter(App.printInvoice.moveLines)
        binding!!.recyclerView.layoutManager = LinearLayoutManager(this)
        binding!!.textView33.text =
            "الإجمالى  " + roundTwoDecimals(App.printInvoice.moveHeader.totalLinesValueAfterDisc.toDouble())
        if (App.printInvoice.moveHeader.deliveryValue != null) binding!!.textView37.text =
            "الخدمة  " + roundTwoDecimals(App.printInvoice.moveHeader.deliveryValue.toDouble()) else binding!!.textView37.text =
            "الخدمة  " + roundTwoDecimals(App.printInvoice.moveHeader.serviceValue.toDouble())
        binding!!.textView38.text =
            "الخصم  " + roundTwoDecimals(App.printInvoice.moveHeader.basicDiscountVal.toDouble())
        binding!!.textView39.text =
            "الضريبة  " + roundTwoDecimals(App.printInvoice.moveHeader.basicTaxVal.toDouble())
        binding!!.textView40.text =
            "المطلوب  " + roundTwoDecimals(App.printInvoice.moveHeader.netValue.toDouble())
        binding!!.textView41.text =
            "الضريبة  " + roundTwoDecimals(App.printInvoice.moveHeader.remainValue.toDouble())
        binding!!.textView42.text =
            "المدفوع  " + roundTwoDecimals(App.printInvoice.moveHeader.paidValue.toDouble())
        binding!!.textView43.text = App.printInvoice.moveHeader.branchAddress
        binding!!.textView45.text = App.printInvoice.moveHeader.tel1
        if (!App.printInvoice.moveHeader.tel2.isEmpty()) binding!!.textView44.text =
            App.printInvoice.moveHeader.tel2 else binding!!.textView44.visibility =
            View.GONE
    }

    fun roundTwoDecimals(d: Double): Double {
        val twoDForm = DecimalFormat("#.##")
        return twoDForm.format(d).toDouble()
    }


    @SuppressLint("MissingPermission")
    private fun scanBluetooth() {
        if (binding!!.scanButton.text == "Connect") {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (mBluetoothAdapter == null) {
                makeText(this, "Message1", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("checkN", " sdsd")
                if (!mBluetoothAdapter!!.isEnabled) {
                    val enableBtIntent = Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE
                    )
                    Log.e("checkN", " ffff")
                    startActivityForResult(
                        enableBtIntent,
                        REQUEST_ENABLE_BT
                    )
                } else {
                    ListPairedDevices()
                    Log.e("checkN", " qqqqq")
                    val connectIntent = Intent(
                        this,
                        DeviceListActivity::class.java
                    )
                    startActivityForResult(
                        connectIntent,
                        REQUEST_CONNECT_DEVICE
                    )
                }
            }
        } else if (binding!!.scanButton.text.equals("Disconnect")) {
            mBluetoothAdapter?.disable()
            binding!!.scanButton.text = ""
            binding!!.scanButton.text = "Disconnected"
            binding!!.scanButton.setTextColor(Color.rgb(199, 59, 59))
            binding!!.printButton.isEnabled = false
            binding!!.scanButton.isEnabled = true
            binding!!.scanButton.text = "Connect"
        }
    }

    @SuppressLint("MissingPermission")
    private fun ListPairedDevices() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH), 1)
        } else {
        val mPairedDevices = mBluetoothAdapter
            ?.getBondedDevices()
            mBluetoothConnectProgressDialog
            if (mPairedDevices != null) {
                if (mPairedDevices.size > 0) {
                    if (mPairedDevices != null) {
                        for (mDevice in mPairedDevices) {
                            mBluetoothConnectProgressDialog?.dismiss()
                            Log.e(
                                "PairedDevices: ", mDevice.name + "  "
                                        + mDevice.address
                            )
                        }
                    }
                }
            }
    }
    }

    private fun closeSocket(nOpenSocket: BluetoothSocket) {
        try {
            nOpenSocket.close()
            Log.d("PrintInvoice", "SocketClosed")
        } catch (ex: IOException) {
            Log.d("PrintInvoice", "CouldNotCloseSocket")
        }
    }

    override fun run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                ?.createRfcommSocketToServiceRecord(applicationUUID)
            mBluetoothAdapter!!.cancelDiscovery()
            mBluetoothSocket?.connect()
            mHandler.sendEmptyMessage(0)
            Log.e("checkBluetooth", "gotSocket")
        } catch (eConnectException: IOException) {
            Log.d("PrintInvoice", "CouldNotConnectToSocket", eConnectException)
            mBluetoothSocket?.let { closeSocket(it) }
            return
        }
    }

    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            mBluetoothConnectProgressDialog?.dismiss()
            binding!!.scanButton.text = ""
            binding!!.scanButton.text = "Connected"
            binding!!.scanButton.setTextColor(Color.rgb(97, 170, 74))
            binding!!.scanButton.isEnabled = true
            binding!!.scanButton.text = "Disconnect"
        }
    }
    private fun initViews(){
    printing?.printingCallback = object : PrintingCallback {
        override fun connectingWithPrinter() {
            Toast.makeText(this@PrintingInvoice, "Connecting with printer", Toast.LENGTH_SHORT).show()
        }

        override fun printingOrderSentSuccessfully() {
            Toast.makeText(this@PrintingInvoice, "Order sent to printer", Toast.LENGTH_SHORT).show()
        }

        override fun connectionFailed(error: String) {
            makeText(this@PrintingInvoice, "Failed to connect printer", Toast.LENGTH_SHORT)
                .show()
        }

        override fun onError(error: String) {
            makeText(this@PrintingInvoice, error, Toast.LENGTH_SHORT).show()
        }

        override fun onMessage(message: String) {
            makeText(this@PrintingInvoice, "Message: $message", Toast.LENGTH_SHORT).show()
        }
    }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CONNECT_DEVICE ) {
            val bitmap = getBitmapFromView(
                scrollView2,
                scrollView2.getChildAt(0).height,
                scrollView2.getChildAt(0).width
            )
            bitmap?.let { it1 -> print_image(it1) }
//            val bitmap = getBitmapFromView(
//                scrollView2,
//                scrollView2.getChildAt(0).height,
//                scrollView2.getChildAt(0).width
//            )
//            bitmap?.let { printSomeImages(it) }
////            mmOutputStream = mBluetoothSocket?.outputStream;
//            try {
//                val imagepath = data!!.data
//                val resolver = contentResolver
//                val b = MediaStore.Images.Media.getBitmap(resolver, imagepath)
//                b1 = convertGreyImg(b)
//                val message = Message()
//                message.what = 1
//

//                b2=resizeImage(b1,576,386,false);
//            } catch (e: java.lang.Exception) {
//                e.printStackTrace()
//            }

        }
        initViews()
    }

    private fun getBitmapFromView(view: View, height: Int, width: Int): Bitmap? {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return bitmap
    }

    private fun convertArgbToGrayscale(
        bmpOriginal: Bitmap, width: Int,
        height: Int
    ) {
        var pixel: Int
        var k = 0
        var B = 0
        var G = 0
        var R = 0
         dots = BitSet()
        try {
            for (x in 0 until height) {
                for (y in 0 until width) {
                    // get one pixel color
                    pixel = bmpOriginal.getPixel(y, x)

                    // retrieve color of all channels
                    R = Color.red(pixel)
                    G = Color.green(pixel)
                    B = Color.blue(pixel)
                    // take conversion up to one single value by calculating
                    // pixel intensity.
                    B = (0.299 * R + 0.587 * G + 0.114 * B).toInt()
                    G = B
                    R = G
                    // set bit into bitset, by calculating the pixel's luma
                    if (R < 55) {
                        dots.set(k) //this is the bitset that i'm printing
                    }
                    k++
                }
            }
        } catch (e: java.lang.Exception) {
            // TODO: handle exception
            Log.e("TAG", e.toString())
        }
    }

    fun convertGreyImg(img: Bitmap): Bitmap? {
    val width = img.width
    val height = img.height
    val pixels = IntArray(width * height)
    img.getPixels(pixels, 0, width, 0, 0, width, height)


    //The arithmetic average of a grayscale image; a threshold
    var redSum = 0.0
    var greenSum = 0.0
    var blueSun = 0.0
    val total = (width * height).toDouble()
    for (i in 0 until height) {
        for (j in 0 until width) {
            val grey = pixels[width * i + j]
            val red = grey and 0x00FF0000 shr 16
            val green = grey and 0x0000FF00 shr 8
            val blue = grey and 0x000000FF
            redSum += red.toDouble()
            greenSum += green.toDouble()
            blueSun += blue.toDouble()
        }
    }
    val m = (redSum / total).toInt()

    //Conversion monochrome diagram
    for (i in 0 until height) {
        for (j in 0 until width) {
            var grey = pixels[width * i + j]
            val alpha1 = 0xFF shl 24
            var red = grey and 0x00FF0000 shr 16
            var green = grey and 0x0000FF00 shr 8
            var blue = grey and 0x000000FF
            if (red >= m) {
                blue = 255
                green = blue
                red = green
            } else {
                blue = 0
                green = blue
                red = green
            }
            grey = alpha1 or (red shl 16) or (green shl 8) or blue
            pixels[width * i + j] = grey
        }
    }
    val mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    mBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    return mBitmap
}

    private fun print_image(bitmap: Bitmap) {
            convertArgbToGrayscale(bitmap, bitmap.width,bitmap.height)
            Log.e("print", "ppp")
            mmOutputStream?.write(PrinterCommands.SET_LINE_SPACING_24)
            var offset = 0
            while (offset < bitmap.height) {
                mmOutputStream?.write(PrinterCommands.SELECT_BIT_IMAGE_MODE)
                for (x in 0 until bitmap.width) {
                    for (k in 0..2) {
                        var slice: Byte = 0
                        for (b in 0..7) {
                            val y = (offset / 8 + k) * 8 + b
                            val i = y * bitmap.width + x
                            var v = false
                            if (i < dots.length()) {
                                v = dots.get(i)
                            }
                            slice = slice or ((if (v) 1 else 0) shl 7 - b).toByte()
                        }
                        mmOutputStream?.write(slice)
                    }
                }
                offset += 24
                mmOutputStream?.write(PrinterCommands.FEED_LINE)
                mmOutputStream?.write(PrinterCommands.FEED_LINE)
                mmOutputStream?.write(PrinterCommands.FEED_LINE)
                mmOutputStream?.write(PrinterCommands.FEED_LINE)
                mmOutputStream?.write(PrinterCommands.FEED_LINE)
                mmOutputStream?.write(PrinterCommands.FEED_LINE)
            }
        mmOutputStream?.write(PrinterCommands.SET_LINE_SPACING_30)
    }
}

private fun Flushable?.write(slice: Byte) {
    TODO("Not yet implemented")
    Log.e("checkkk","kkk")
}

