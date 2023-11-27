package com.dataflowstores.dataflow.ui.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.FragmentReportsBinding;
import com.dataflowstores.dataflow.ui.StoreReportScreen;
import com.dataflowstores.dataflow.ui.reports.SearchCustomerBalance;
import com.dataflowstores.dataflow.ui.reports.cashierMovesReport.CashierMovesReport;
import com.dataflowstores.dataflow.ui.reports.financialReport.FinancialReport;
import com.dataflowstores.dataflow.ui.reports.itemSalesReport.ItemSalesReport;
import com.dataflowstores.dataflow.ui.searchItemPrice.SearchItemPrice;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportsFragment extends Fragment {
    FragmentReportsBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReportsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportsFragment newInstance(String param1, String param2) {
        ReportsFragment fragment = new ReportsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reports, container, false);
        permissions();
        setupViews();
        return binding.getRoot();
    }

    private void setupViews() {
        App.customer = null;
        binding.back.setOnClickListener(view -> back());
        binding.financialReport.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity())) {
                startActivity(new Intent(requireActivity(), FinancialReport.class));
            }
        });
        binding.itemSalesReport.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity()))
                startActivity(new Intent(requireActivity(), ItemSalesReport.class));
        });
        binding.storeReport.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity()))
                startActivity(new Intent(requireActivity(), StoreReportScreen.class));
        });
        binding.searchCustomer.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity()))
                startActivity(new Intent(requireActivity(), SearchCustomerBalance.class));
        });
        binding.priceEnquiry.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity()))
                startActivity(new Intent(requireActivity(), SearchItemPrice.class));
        });
        binding.cashierMovesReport.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity()))
                startActivity(new Intent(requireActivity(), CashierMovesReport.class));
        });

    }

    private void permissions() {
        if (App.currentUser.getMobileDealersBalanceEnquiry() != 1 && App.currentUser.getMobileSuppliersBalanceEnquiry() != 1) {
            binding.searchCustomer.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.searchCustomer.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }
        if (App.currentUser.getMobileMoneyReport() == 0) {
            binding.financialReport.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.financialReport.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }
        if (App.currentUser.getMobileItemsSalesReport() == 0) {
            binding.itemSalesReport.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.itemSalesReport.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }

        if (App.currentUser.getMobileItemPricesEnquiry() != 1 && App.currentUser.getMobileItemPricesEnquiryBuy() != 1) {
            binding.priceEnquiry.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.priceEnquiry.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }
        if (App.currentUser.getMobileInventoryReport() == 0) {
            binding.storeReport.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.storeReport.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }
        if (App.currentUser.getMobileCashierMovesReport() == 0) {
            binding.cashierMovesReport.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.cashierMovesReport.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }
    }

    private void back() {
        requireActivity().onBackPressed();

    }

}