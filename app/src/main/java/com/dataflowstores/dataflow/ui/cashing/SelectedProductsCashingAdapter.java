package com.dataflowstores.dataflow.ui.cashing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.ProductItemCashingBinding;
import com.dataflowstores.dataflow.pojo.product.ProductData;

import java.util.ArrayList;
import java.util.Locale;

public class SelectedProductsCashingAdapter extends RecyclerView.Adapter<SelectedProductsCashingAdapter.SelectedProductsCashingHolder> {
    ProductItemCashingBinding binding;
    Context context;
    private ArrayList<ProductData> list;
    int moveType;

    public SelectedProductsCashingAdapter(ArrayList<ProductData> list, Context context, int moveType) {
        this.list = list;
        this.context = context;
        this.moveType = moveType;
    }

    @Override
    public SelectedProductsCashingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.product_item_cashing, parent, false);
        return new SelectedProductsCashingHolder(binding.getRoot());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final SelectedProductsCashingHolder holder, final int position) {
        binding.productName.setText(list.get(position).getItemName());
        binding.fromStore.setText(list.get(position).getSelectedStore().getStoreName());
        if (moveType == 14) {
            binding.toStore.setText(list.get(position).getSelectedToStore().getStoreName());
        } else {
            binding.toStore.setVisibility(View.GONE);
            binding.toStoreTxt.setVisibility(View.GONE);
            binding.fromStoreTxt.setText("المخزن");
        }
        if (moveType == 7)
            binding.quantityTxt.setText("الكمية الحالية");
        binding.totalQuantity.setText(String.format(Locale.US, "%.2f", list.get(position).getActualQuantity()) + " " + list.get(position).getSelectedUnit().getMeasureUnitArName());
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public void callDeleteFunction(int pos) {
        list.remove(pos);
        notifyItemRemoved(pos);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void callEditFunction(int pos) {
        App.product = list.get(pos);
        App.isEditing = true;
        App.editingPos = pos;
        Intent intent = new Intent(context, ProductScreenCashing.class);
        intent.putExtra("moveType", moveType);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public class SelectedProductsCashingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public SelectedProductsCashingHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }
}

