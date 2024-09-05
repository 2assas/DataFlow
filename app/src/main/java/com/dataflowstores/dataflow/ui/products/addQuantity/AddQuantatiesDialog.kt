package com.dataflowstores.dataflow.ui.products.addQuantity

import ScanBarCodeFragment
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.dataflowstores.dataflow.App
import com.dataflowstores.dataflow.R
import com.dataflowstores.dataflow.databinding.AddQuantityDialogBinding
import com.dataflowstores.dataflow.ui.SplashScreen
import com.dataflowstores.dataflow.ui.products.QuantityItemListener
import com.dataflowstores.dataflow.ui.products.QuantityItemsAdapter
import com.dataflowstores.dataflow.utils.SwipeHelper
import java.util.Locale


open class AddQuantityFragment : Fragment(), ScanBarcodeListener, QuantityItemListener {

    private lateinit var binding: AddQuantityDialogBinding
    private lateinit var scanBarCodeFragment: ScanBarCodeFragment

    private var selectedNumber = 1
    private var mListener: DialogListener? = null
    private var counter = 0
    private var totalQuantity = 0.0
    private lateinit var adapter: QuantityItemsAdapter
    private var startIndex = 0
    private var length = 0
    private var divideOn = 0.0
    private var addedQuantityList = listOf<Double>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.add_quantity_dialog, container, false)
        return binding.root
    }


    fun setListener(listener: DialogListener) {
        mListener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            startActivity(Intent(requireActivity(), SplashScreen::class.java))
            requireActivity().finishAffinity()
        } else {
            assert(arguments != null)
            arguments?.let {
                startIndex = it.getString("startIndex")?.toInt() ?: 0
                length = it.getString("length")?.toInt() ?: 0
                divideOn = it.getString("divideOn")?.toDouble() ?: 0.0
            }
            binding.saveButton.setOnClickListener {
                totalQuantity = String.format(Locale.ENGLISH, "%.3f", totalQuantity).toDouble()
                mListener?.onDialogDismissed(totalQuantity, counter)
                parentFragmentManager.beginTransaction()
                    .remove(this@AddQuantityFragment).commit()
            }
            setupViews()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = try {
            context as DialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement DialogListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener?.onDialogDismissed(0.0, 0)
    }

    override fun onStart() {
        super.onStart()
    }

    private fun setupViews() {
        adapter = QuantityItemsAdapter(requireActivity(), this)
        binding.productsRecycler.adapter = adapter
        barCodeScan()
        quantityInput()
        recyclerSwipe();
    }

    private fun barCodeScan() {
        binding.scan.setOnClickListener { view ->
            binding.scanBarcodeContainer.visibility = View.VISIBLE
            scanBarCodeFragment = ScanBarCodeFragment()
            scanBarCodeFragment.setBarcodeListener(this)
            childFragmentManager.beginTransaction()
                .replace(R.id.scanBarcodeContainer, scanBarCodeFragment)
                .commit()
        }
    }

    private fun quantityInput() {
        binding.addQuantityButton.setOnClickListener {
            val quantity = binding.quantityEditText.text.toString()
            if (quantity.isNotEmpty()) {
                insertNewQuantity(quantity = quantity.toDouble())
                binding.quantityEditText.setText("")
            }
        }
    }

    private fun insertNewQuantity(quantity: Double) {
        counter++
        totalQuantity += quantity
        val newItem = QuantityItemsAdapter.QuantityItem(counter, quantity)
        adapter.addItems(newItem)
        binding.counterValue.text = "$counter"
        binding.totalQuantity.text = String.format(Locale.ENGLISH, "%.3f", totalQuantity)

    }

    open fun recyclerSwipe(): SwipeHelper? {
        return object : SwipeHelper(requireActivity(), binding.productsRecycler, 250) {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                buffer.add(MyButton(
                    requireActivity(), R.drawable.delete, Color.RED
                ) { pos: Int ->
                    if (adapter.itemCount == 1) {
                        App.selectedProducts = ArrayList()
                    }
                    adapter.callDeleteFunction(pos)
                })
            }
        }
    }


    interface DialogListener {
        fun onDialogDismissed(totalQuantity: Double, count: Int)
    }

    override fun onBarcodeScanned(barcode: String) {
        val substring = barcode.substring(startIndex, startIndex + length)
        if (substring.isNotEmpty()) {
            insertNewQuantity(substring.toDouble() / divideOn)
        }
    }

    override fun dismissScanner() {
        binding.scanBarcodeContainer.visibility = View.GONE
    }

    override fun onDeleteItem(itemQuantity: Double) {
        counter--
        totalQuantity -= itemQuantity
        binding.counterValue.text = counter.toString()
        binding.totalQuantity.text = String.format(Locale.ENGLISH, "%.3f", totalQuantity)
    }

}