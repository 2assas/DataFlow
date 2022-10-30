package com.dataflowstores.dataflow.ui.home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.ui.invoice.FirstInvoice;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.FragmentInvoicesBinding;

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
        binding.invoice.setOnClickListener(view -> {
            if (App.currentUser.getSafeDepositBranchISN() == 0 || App.currentUser.getSafeDepositISN() == 0) {
                new AlertDialog.Builder(requireActivity()).
                        setTitle("لا تسطيع عمل فاتورة")
                        .setMessage("برجاء فحص الخزنه الخاصة بكم.")
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_baseline_error_outline_24)
                        .setNegativeButton("حسنا", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        }).show();
            } else if (App.currentUser.getCashierStoreBranchISN() == 0 || App.currentUser.getCashierStoreISN() == 0) {
                new AlertDialog.Builder(requireActivity()).
                        setTitle("لا تسطيع عمل فاتورة")
                        .setMessage("برجاء فحص المخزن الخاص بكم")
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_baseline_error_outline_24)
                        .setNegativeButton("حسنا", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        }).show();
            } else {
                startActivity(new Intent(requireActivity(), FirstInvoice.class));
                App.resales = 0;
            }
        });

        binding.Resales.setOnClickListener(view -> {
            if (App.currentUser.getSafeDepositBranchISN() == 0 || App.currentUser.getSafeDepositISN() == 0) {
                new AlertDialog.Builder(requireActivity()).
                        setTitle("لا تسطيع عمل فاتورة")
                        .setMessage("برجاء فحص الخزنه الخاصة بكم.")
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_baseline_error_outline_24)
                        .setNegativeButton("حسنا", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        }).show();
            } else if (App.currentUser.getCashierStoreBranchISN() == 0 || App.currentUser.getCashierStoreISN() == 0) {
                new AlertDialog.Builder(requireActivity()).
                        setTitle("لا تسطيع عمل فاتورة")
                        .setMessage("برجاء فحص المخزن الخاص بكم")
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_baseline_error_outline_24)
                        .setNegativeButton("حسنا", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        }).show();
            } else {
                Intent intent = new Intent(requireActivity(), FirstInvoice.class);
                startActivity(intent);
                App.resales = 1;
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
            binding.Resales.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.Resales.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }
    }

    private void back() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new HomeFragment());
        fragmentTransaction.commit();
    }
}