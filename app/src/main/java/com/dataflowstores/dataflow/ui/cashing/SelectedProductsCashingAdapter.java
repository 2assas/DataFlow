package com.dataflowstores.dataflow.ui.cashing;

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
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.ProductItemCashingBinding;
import com.dataflowstores.dataflow.pojo.product.ProductData;
import com.dataflowstores.dataflow.ui.adapters.CartItemListener;

import java.util.Locale;

public class SelectedProductsCashingAdapter extends ListAdapter<ProductData, SelectedProductsCashingAdapter.SelectedProductsCashingHolder> {
    private final Context context;
    private final int moveType;
    CartItemListener listener;

    public SelectedProductsCashingAdapter(Context context, int moveType, CartItemListener listener) {
        super(new ProductDiffCallback());
        this.context = context;
        this.moveType = moveType;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SelectedProductsCashingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductItemCashingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.product_item_cashing, parent, false);
        return new SelectedProductsCashingHolder(binding.getRoot());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final SelectedProductsCashingHolder holder, final int position) {
        ProductData productData = getItem(position);
        holder.bind(productData);
    }

    public void callDeleteFunction(int pos) {
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, getItemCount() - pos);
        listener.onDeleteItem(getItem(pos));
    }

    public void callEditFunction(int pos) {
        App.product = getItem(pos);
        App.isEditing = true;
        App.editingPos = pos;
        Intent intent = new Intent(context, ProductScreenCashing.class);
        intent.putExtra("moveType", moveType);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public class SelectedProductsCashingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ProductItemCashingBinding binding;

        public SelectedProductsCashingHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(ProductData productData) {
            binding.productName.setText(productData.getItemName());
            binding.fromStore.setText(productData.getSelectedStore().getStoreName());
            if (moveType == 14) {
                binding.toStore.setText(productData.getSelectedToStore().getStoreName());
            } else {
                binding.toStore.setVisibility(View.GONE);
                binding.toStoreTxt.setVisibility(View.GONE);
                binding.fromStoreTxt.setText("المخزن");
            }
            if (moveType == 7)
                binding.quantityTxt.setText("الكمية الحالية");
            binding.totalQuantity.setText(String.format(Locale.US, "%.3f", productData.getActualQuantity()) + " " + productData.getSelectedUnit().getMeasureUnitArName());
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
