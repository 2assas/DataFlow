import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.dataflowstores.dataflow.databinding.FragmentBarcodeScannerBinding
import com.dataflowstores.dataflow.ui.products.addQuantity.ScanBarcodeListener
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector


class ScanBarCodeFragment : Fragment() {

    private lateinit var binding: FragmentBarcodeScannerBinding
    private lateinit var barcodeListener: ScanBarcodeListener
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private lateinit var toneGen1: ToneGenerator
    private lateinit var barcodeData: String
    private val REQUEST_CAMERA_PERMISSION = 201
    private var scanned = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBarcodeScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanned = false
        toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        initialiseDetectorsAndSources()
        binding.back.setOnClickListener {
            barcodeListener.dismissScanner()
            parentFragmentManager.beginTransaction().remove(this).commit()
            cameraSource.release()
        }
        barcodeListener = parentFragment as ScanBarcodeListener
    }

    override fun onDetach() {
        cameraSource.release()
        super.onDetach()
    }

    private fun initialiseDetectorsAndSources() {
        barcodeDetector = BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()

        cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true)
            .build()

        binding.scannerCamera.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        cameraSource.start(binding.scannerCamera.holder)
                    } else {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.CAMERA),
                            REQUEST_CAMERA_PERMISSION
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
                val barcodes: SparseArray<Barcode> = detections?.detectedItems ?: return
                if (barcodes.size() != 0) {
                    binding.detected.post {
                        barcodeData = barcodes.valueAt(0).displayValue
                        binding.detected.removeCallbacks(null)
                        binding.detected.text = barcodeData
                        binding.detected.setTextColor(Color.GREEN)
                        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150)
                        if (barcodeData.isNotEmpty() && !scanned) {
                            scanned = true
                            barcodeListener.onBarcodeScanned(barcodeData)
                            cameraSource.release()
                            parentFragmentManager.beginTransaction()
                                .remove(this@ScanBarCodeFragment).commit()
                            return@post
                        }
                    }
                }
            }
        })
    }

    fun setBarcodeListener(listener: ScanBarcodeListener) {
        barcodeListener = listener
    }


    override fun onPause() {
        super.onPause()
        cameraSource.release()
        barcodeListener.dismissScanner()
    }

    override fun onResume() {
        super.onResume()
        initialiseDetectorsAndSources()
    }
}

