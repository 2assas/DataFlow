package com.dataflowstores.dataflow.ui.reports.dealersBalancesReport

import DealerAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dataflowstores.dataflow.App
import com.dataflowstores.dataflow.databinding.FragmentDealerResultsBinding
import com.dataflowstores.dataflow.pojo.report.dealersBalancesReport.DealerViewModel
import com.dataflowstores.dataflow.ui.invoice.PrintScreen

class DealerResultsDialogFragment : Fragment() {

    private lateinit var viewModel: DealerViewModel
    private lateinit var adapter: DealerAdapter
    private lateinit var binding: FragmentDealerResultsBinding

    private val VISIBLE_THRESHOLD = 5
    private var reachedEnd = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDealerResultsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[DealerViewModel::class.java]
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("HardwareIds")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDealerResultsBinding.bind(view)

        binding.closeButton.setOnClickListener {
            dismiss()
        }
        dealersList()
        printButton()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    private fun dealersList() {
        val dealerName = arguments?.getString("dealerName")
        val uuid = arguments?.getString("uuid")
        val branchISN = arguments?.getString("branchISN")
        val branchName = arguments?.getString("branchName")
        val dealerType = arguments?.getInt("dealerType")
        val workerBranchISN = arguments?.getString("workerBranchISN")
        val excludeZeroBalance = arguments?.getBoolean("excludeZeroBalance")
        val dealerCategory = arguments?.getString("DealerCategory") ?: ""

        branchName?.let {
            binding.branchName.visibility = View.VISIBLE
            binding.branchName.text = " فرع $branchName"
        }
        when (dealerType) {
            1 -> binding.title.text = "تقرير حسابات العملاء"
            2 -> binding.title.text = "تقرير حسابات الموردين"
        }

        adapter = DealerAdapter()
        binding.recyclerView.adapter = adapter

        binding.reportScroll.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            val totalHeight = binding.reportScroll.getChildAt(0).height
            val visibleHeight = binding.reportScroll.height
            val scrollThreshold = 50
            val scrollDifference = totalHeight - visibleHeight - scrollY
            if (scrollDifference >= scrollThreshold && !viewModel.isLoading && !reachedEnd) {
                binding.progress.visibility = View.VISIBLE
                viewModel.loadMoreData(
                    dealerName,
                    uuid,
                    branchISN,
                    dealerType,
                    workerBranchISN,
                    excludeZeroBalance,
                    dealerCategory
                )
            }
        }

        viewModel.dealers.observe(viewLifecycleOwner) { dealers ->
            binding.progress.visibility = View.GONE
            if (dealers == null) {
                reachedEnd = true
                if (adapter.itemCount > 0) {
                    binding.printButton.visibility = View.VISIBLE
                }
            }
            dealers?.let {
                var filteredDealers = it.toMutableList()
                if (excludeZeroBalance == true) {
                    filteredDealers =
                        filteredDealers.filter { dealerData -> dealerData.currentBalance?.contains("صفر") != true }
                            .toMutableList()
                }
                if (filteredDealers.size in 1..14) {
                    binding.progress.visibility = View.VISIBLE
                    viewModel.loadMoreData(
                        dealerName,
                        uuid,
                        branchISN,
                        dealerType,
                        workerBranchISN,
                        excludeZeroBalance,
                        dealerCategory
                    )
                }
                adapter.updateData(filteredDealers)
            }
        }

        // Trigger initial data load
        viewModel.getDealersBalancesReport(
            dealerName,
            uuid,
            branchISN,
            dealerType,
            workerBranchISN,
            excludeZeroBalance,
            dealerCategory
        )
        binding.progress.visibility = View.VISIBLE

    }

    private fun takeScreenShot() {
        val u: View = binding.reportScroll
        val totalHeight = binding.reportScroll.getChildAt(0).height
        val totalWidth = binding.reportScroll.getChildAt(0).width
        val b = getBitmapFromView(u, totalHeight, totalWidth)
        App.printBitmap = b
    }

    fun getBitmapFromView(view: View, totalHeight: Int, totalWidth: Int): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return returnedBitmap
    }

    fun printButton() {
        binding.printButton.setOnClickListener {
            binding.printButton.visibility = View.GONE
            binding.closeButton.setVisibility(View.GONE)
            val handler = Handler()
            handler.postDelayed({
                takeScreenShot()
                parentFragmentManager.popBackStack()
                startActivity(Intent(requireActivity(), PrintScreen::class.java))
            }, 1000)
        }
    }

    private fun dismiss() {
        reachedEnd = false
        adapter.submitList(listOf())
        viewModel.dispose()
        parentFragmentManager.popBackStack()

    }

    override fun onDetach() {
        super.onDetach()
        reachedEnd = false
        adapter.submitList(listOf())
        viewModel.dispose()
    }
}
