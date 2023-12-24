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
import com.dataflowstores.dataflow.databinding.FragmentFinanceBinding;
import com.dataflowstores.dataflow.ui.expenses.ExpensesScreen;
import com.dataflowstores.dataflow.ui.payments.PaymentsScreen;
import com.dataflowstores.dataflow.ui.receipts.ReceiptScreen;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FinanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FinanceFragment extends Fragment {
    FragmentFinanceBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int moveType;
    boolean isStore = false;

    public FinanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FinanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FinanceFragment newInstance(String param1, String param2) {
        FinanceFragment fragment = new FinanceFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_finance, container, false);
        powers();
        setupViews();
        return binding.getRoot();
    }

    private void setupViews() {
        binding.expenses.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity()))
                startActivity(new Intent(requireActivity(), ExpensesScreen.class));
        });
        binding.back.setOnClickListener(v -> {
            back();
        });
        binding.receipts.setOnClickListener(v -> {
            if (App.isNetworkAvailable(requireActivity()))
                startActivity(new Intent(requireActivity(), ReceiptScreen.class));
        });
        binding.payments.setOnClickListener(v -> {
            if (App.isNetworkAvailable(requireActivity())) {
                Intent intent = new Intent(requireActivity(), PaymentsScreen.class);
                startActivity(intent);
            }
        });


    }

    private void powers() {
        if (App.currentUser.getMobileCashReceipts() == 0) {
            binding.receipts.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.receipts.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }
        if (App.currentUser.getMobileExpenses() == 0) {
            binding.expenses.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.expenses.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }
        if (App.currentUser.getMobilePayment() == 0) {
            binding.payments.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.payments.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }
    }

    private void back() {
        requireActivity().onBackPressed();
    }


}