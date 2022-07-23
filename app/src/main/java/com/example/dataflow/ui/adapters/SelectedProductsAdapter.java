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
import com.example.dataflow.pojo.product.ProductData;
import com.example.dataflow.ui.ProductDetails;

import java.util.ArrayList;
import java.util.Locale;

public class SelectedProductsAdapter extends RecyclerView.Adapter<SelectedProductsAdapter.SelectedProductsHolder> {
    ProductItemBinding binding;
    Context context;
    private ArrayList<ProductData> list;

    public SelectedProductsAdapter(ArrayList<ProductData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public SelectedProductsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.product_item, parent, false);
        return new SelectedProductsHolder(binding.getRoot());
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final SelectedProductsHolder holder, final int position) {
        binding.productName.setText(list.get(position).getItemName());
        binding.price.setText(String.format(Locale.US,"%.2f", list.get(position).getNetPrice()));
        binding.totalPrice.setText( list.get(position).getQuantity()+" "+list.get(position).getSelectedUnit().getMeasureUnitArName());
        binding.totalQuantity.setText(String.format(Locale.US,"%.2f", list.get(position).getSelectedUnit().getPrice()));
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
    public int getItemViewType(int position)
    {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public void callEditFunction(int pos) {
        App.product = list.get(pos);
        App.isEditing = true;
        App.editingPos =pos;
        context.startActivity(new Intent(context, ProductDetails.class));
        ((Activity)context).finish();
    }

    public class SelectedProductsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public SelectedProductsHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
        }
    }
}

