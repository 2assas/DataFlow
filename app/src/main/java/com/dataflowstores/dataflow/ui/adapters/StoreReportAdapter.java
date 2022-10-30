package com.dataflowstores.dataflow.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dataflowstores.dataflow.pojo.report.ItemAvailableQuantity;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.StoreReportItemBinding;

import java.util.ArrayList;

public class StoreReportAdapter extends RecyclerView.Adapter<StoreReportAdapter.StoreReportHolder> {
    StoreReportItemBinding binding;
    Context context;
    private ArrayList<ItemAvailableQuantity> list;

    public StoreReportAdapter(ArrayList<ItemAvailableQuantity> list, Context context) {
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
        binding.quantity.setText(list.get(position).getTotalQuan()+"");
        binding.measureUnit.setText(list.get(position).getMeasureUnitArName());
        binding.textView49.setText(list.get(position).getQuantityDescription());
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

