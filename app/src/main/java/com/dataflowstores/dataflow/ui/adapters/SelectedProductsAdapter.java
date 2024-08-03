package com.dataflowstores.dataflow.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.pojo.product.ProductData;
import com.dataflowstores.dataflow.ui.products.ProductDetails;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.ProductItemBinding;

import java.util.Locale;

public class SelectedProductsAdapter extends ListAdapter<ProductData, SelectedProductsAdapter.SelectedProductsHolder> {
    private final Context context;
    private final CartItemListener listener;

    public SelectedProductsAdapter(Context context, CartItemListener listener) {
        super(new ProductDiffCallback());
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SelectedProductsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.product_item, parent, false);
        return new SelectedProductsHolder(binding.getRoot());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final SelectedProductsHolder holder, final int position) {
        ProductData productData = getItem(position);
        holder.bind(productData);
    }

    public void callDeleteFunction(int pos) {
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, getItemCount() - pos);
        listener.onDeleteItem(getItem(pos));
    }

    public void callEditFunction(int pos) {
        final ProductData productData = getItem(pos);
        App.product = productData;
        App.isEditing = true;
        App.editingPos = pos;
        context.startActivity(new Intent(context, ProductDetails.class));
        ((Activity) context).finish();
    }

    public class SelectedProductsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ProductItemBinding binding;

        public SelectedProductsHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(ProductData productData) {
            binding.productName.setText(productData.getItemName());
            binding.netPrice.setText(String.format(Locale.US, "%.3f", productData.getNetPrice()));
            binding.quantity.setText(productData.getQuantity() + " " + (productData.getSelectedUnit() != null ? productData.getSelectedUnit().getMeasureUnitArName() : ""));
            binding.price.setText(String.format(Locale.US, "%.3f", productData.getPriceItem()));
            binding.sequence.setText(String.valueOf(getAdapterPosition() + 1));
        }

        @Override
        public void onClick(View v) {
            // Handle click event if needed
        }
    }

    private static class ProductDiffCallback extends DiffUtil.ItemCallback<ProductData> {
        @Override
        public boolean areItemsTheSame(@NonNull ProductData oldItem, @NonNull ProductData newItem) {
            return oldItem.getItemISN() == newItem.getItemISN();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ProductData oldItem, @NonNull ProductData newItem) {
            return oldItem.equals(newItem);
        }
    }
}
