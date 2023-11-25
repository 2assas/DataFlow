package com.dataflowstores.dataflow.ui.products;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dataflowstores.dataflow.ui.cashing.ProductScreenCashing;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.AvailableProductDialogBinding;
import com.dataflowstores.dataflow.pojo.report.ItemAvailableQuantity;
import com.dataflowstores.dataflow.ui.SplashScreen;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class AvailableProductDialog extends BottomSheetDialogFragment {

    AvailableProductDialogBinding binding;
    ArrayList<ItemAvailableQuantity> itemsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding = DataBindingUtil.inflate(inflater, R.layout.available_product_dialog, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            startActivity(new Intent(requireActivity(), SplashScreen.class));
            requireActivity().finishAffinity();
        } else {
            assert getArguments() != null;
            itemsList = (ArrayList<ItemAvailableQuantity>) getArguments().getSerializable("items");
            setupView();
        }

    }

    private void setupView() {
        AvailableProductAdapter adapter;
        if (requireActivity() instanceof ProductDetails)
            adapter = new AvailableProductAdapter(itemsList, this, requireActivity(), (ProductDetails) requireActivity());
        else
            adapter = new AvailableProductAdapter(itemsList, this, requireActivity(), (ProductScreenCashing) requireActivity());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.recyclerView.setAdapter(adapter);
        binding.imageButton.setOnClickListener(view -> dismiss());
    }
}
