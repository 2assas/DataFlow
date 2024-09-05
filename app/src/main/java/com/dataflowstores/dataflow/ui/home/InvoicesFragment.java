package com.dataflowstores.dataflow.ui.home;

import android.app.AlertDialog;
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
import com.dataflowstores.dataflow.databinding.FragmentInvoicesBinding;
import com.dataflowstores.dataflow.pojo.invoice.InvoiceType;
import com.dataflowstores.dataflow.ui.invoice.FirstInvoice;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InvoicesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvoicesFragment extends Fragment {
    FragmentInvoicesBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InvoicesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InvoicesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InvoicesFragment newInstance(String param1, String param2) {
        InvoicesFragment fragment = new InvoicesFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_invoices, container, false);
        setupViews();
        permission();
        return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_invoices, container, false);
    }

    private void setupViews() {
        binding.back.setOnClickListener(view -> {
            back();
        });
        saleOrders();
        purchaseOrders();
    }

    private boolean checkSafeDepositAndStores() {
        if (App.currentUser.getSafeDepositBranchISN() == 0 || App.currentUser.getSafeDepositISN() == 0) {
            new AlertDialog.Builder(requireActivity()).setTitle("لا تسطيع عمل فاتورة").setMessage("برجاء فحص الخزنه الخاصة بكم.").setCancelable(false).setIcon(R.drawable.ic_baseline_error_outline_24).setNegativeButton("حسنا", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            }).show();
            return false;
        } else if (App.currentUser.getCashierStoreBranchISN() == 0 || App.currentUser.getCashierStoreISN() == 0) {
            new AlertDialog.Builder(requireActivity()).setTitle("لا تسطيع عمل فاتورة").setMessage("برجاء فحص المخزن الخاص بكم").setCancelable(false).setIcon(R.drawable.ic_baseline_error_outline_24).setNegativeButton("حسنا", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            }).show();
            return false;
        } else {
            return true;
        }
    }

    private void purchaseOrders() {
        if (App.currentUser.getMobileSupply() == 0) {
            binding.purchaseOrder.setEnabled(false);
        }
        if (App.currentUser.getMobileReSupply() == 0) {
            binding.returnPurchased.setEnabled(false);
        }
        binding.purchaseOrder.setOnClickListener(view -> {
            if (checkSafeDepositAndStores()) {
                App.invoiceType = InvoiceType.Purchase;
                Intent intent = new Intent(requireActivity(), FirstInvoice.class);
                startActivity(intent);
            }
        });
        binding.returnPurchased.setOnClickListener(view -> {
            if (checkSafeDepositAndStores()) {
                App.invoiceType = InvoiceType.ReturnPurchased;
                Intent intent = new Intent(requireActivity(), FirstInvoice.class);
                startActivity(intent);
            }
        });
    }

    private void saleOrders() {
        binding.invoice.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity())) {
                if (checkSafeDepositAndStores()) {
                    App.invoiceType = InvoiceType.Sales;
                    startActivity(new Intent(requireActivity(), FirstInvoice.class));
                }
            }
        });

        binding.returnSales.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity())) {
                if (checkSafeDepositAndStores()) {
                    App.invoiceType = InvoiceType.ReturnSales;
                    Intent intent = new Intent(requireActivity(), FirstInvoice.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void permission() {
        if (App.currentUser.getMobileSales() == 0) {
            binding.invoice.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.invoice.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }
        if (App.currentUser.getMobileReSales() == 0) {
            binding.returnSales.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.returnSales.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }
    }

    private void back() {
        requireActivity().onBackPressed();
    }
}