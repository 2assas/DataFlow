package com.example.dataflow.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dataflow.App;
import com.example.dataflow.R;
import com.example.dataflow.databinding.ProductItemBinding;
import com.example.dataflow.databinding.StoreReportItemBinding;
import com.example.dataflow.pojo.product.ProductData;
import com.example.dataflow.pojo.report.StoreReportData;
import com.example.dataflow.ui.ProductDetails;

import java.util.ArrayList;
import java.util.Locale;

public class StoreReportAdapter extends RecyclerView.Adapter<StoreReportAdapter.StoreReportHolder> {
    StoreReportItemBinding binding;
    Context context;
    private ArrayList<StoreReportData> list;

    public StoreReportAdapter(ArrayList<StoreReportData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public StoreReportHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.store_report_item, parent, false);
        return new StoreReportHolder(binding.getRoot());
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final StoreReportHolder holder, final int position) {
        binding.serial.setText(String.valueOf(position+1));
        binding.itemName.setText(list.get(position).getItemName());
        binding.quantity.setText(list.get(position).getTotalQuan());
        binding.measureUnit.setText(list.get(position).getMeasureUnitArName());
        binding.textView49.setText(list.get(position).getQuantity_description());
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public class StoreReportHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public StoreReportHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
        }
    }
}

