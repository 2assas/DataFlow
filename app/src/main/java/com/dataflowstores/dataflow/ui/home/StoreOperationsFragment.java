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
import com.dataflowstores.dataflow.databinding.FragmentStoreOperationsBinding;
import com.dataflowstores.dataflow.pojo.users.CustomerData;
import com.dataflowstores.dataflow.pojo.users.SalesManData;
import com.dataflowstores.dataflow.ui.cashing.SearchProductsCashing;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreOperationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreOperationsFragment extends Fragment {
    FragmentStoreOperationsBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StoreOperationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreOperationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreOperationsFragment newInstance(String param1, String param2) {
        StoreOperationsFragment fragment = new StoreOperationsFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_store_operations, container, false);
        permission();
        setupViews();
        return binding.getRoot();
    }

    private void setupViews() {
        App.currentBranch=null;
        App.headerNotes="";
        binding.back.setOnClickListener(view -> back());
        binding.cashing.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity())) {
                Intent intent = new Intent(requireActivity(), SearchProductsCashing.class);
                intent.putExtra("moveType", 16);
                intent.putExtra("store", false);
                App.customer = new CustomerData();
                App.agent = new SalesManData();
                startActivity(intent);
            }
        });
        binding.receiving.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity())) {
                Intent intent = new Intent(requireActivity(), SearchProductsCashing.class);
                intent.putExtra("moveType", 17);
                intent.putExtra("store", true);
                App.customer = new CustomerData();
                App.agent = new SalesManData();
                startActivity(intent);
            }
        });
        binding.storeTransfer.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity())) {
                Intent intent = new Intent(requireActivity(), SearchProductsCashing.class);
                intent.putExtra("moveType", 14);
                intent.putExtra("store", true);
                App.customer = new CustomerData();
                App.agent = new SalesManData();
                startActivity(intent);
            }
        });
        binding.createItem.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity())) {
                Intent intent = new Intent(requireActivity(), SearchProductsCashing.class);
                intent.putExtra("moveType", 15);
                intent.putExtra("store", false);
                App.customer = new CustomerData();
                App.agent = new SalesManData();
                startActivity(intent);
            }
        });
        binding.mobileFirstPeriod.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity())) {
                Intent intent = new Intent(requireActivity(), SearchProductsCashing.class);
                intent.putExtra("moveType", 12);
                intent.putExtra("store", false);
                App.customer = new CustomerData();
                App.agent = new SalesManData();
                startActivity(intent);
            }
        });
        binding.mobileLosses.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity())) {
                Intent intent = new Intent(requireActivity(), SearchProductsCashing.class);
                intent.putExtra("moveType", 8);
                intent.putExtra("store", false);
                App.customer = new CustomerData();
                App.agent = new SalesManData();
                startActivity(intent);
            }
        });
        binding.mobileInventory.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity())) {
                Intent intent = new Intent(requireActivity(), SearchProductsCashing.class);
                intent.putExtra("moveType", 7);
                intent.putExtra("store", false);
                App.customer = new CustomerData();
                App.agent = new SalesManData();
                startActivity(intent);
            }
        });
        binding.mobileItemsList.setOnClickListener(view -> {
            if (App.isNetworkAvailable(requireActivity())) {
                Intent intent = new Intent(requireActivity(), SearchProductsCashing.class);
                intent.putExtra("moveType", 21);
                intent.putExtra("store", true);
                App.customer = new CustomerData();
                App.agent = new SalesManData();
                startActivity(intent);
            }
        });
    }

    private void permission() {
        if (App.currentUser.getMobileStockOut() == 0) {
            binding.cashing.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.cashing.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }

        if (App.currentUser.getMobileStoreTransfer() == 0) {
            binding.storeTransfer.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.storeTransfer.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }

        if (App.currentUser.getMobileStockIn() == 0) {
            binding.receiving.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.receiving.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }

        if (App.currentUser.getMobileFirstPeriod() == 0) {
            binding.mobileFirstPeriod.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.mobileFirstPeriod.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }
        if (App.currentUser.getMobileLosses() == 0) {
            binding.mobileLosses.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.mobileLosses.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }
        if (App.currentUser.getMobileItemConfiguring() == 0) {
            binding.createItem.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.createItem.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }
        if (App.currentUser.getMobileItemQuanModify() == 0) {
            binding.mobileInventory.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.mobileInventory.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }
        if (App.currentUser.getMobileItemsList() == 0) {
            binding.mobileItemsList.setEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                binding.mobileItemsList.setBackground(requireActivity().getDrawable(R.drawable.gray_rounded));
            }
        }

    }

    private void back() {
        requireActivity().onBackPressed();
    }
}