package com.dataflowstores.dataflow.ui.products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.AvailableProductItemBinding;
import com.dataflowstores.dataflow.pojo.report.ItemAvailableQuantity;

import java.util.ArrayList;


public class AvailableProductAdapter extends RecyclerView.Adapter<AvailableProductAdapter.AvailableProductHolder> {
    private ArrayList<ItemAvailableQuantity> list;
    public DialogFragment fragment;
    public Context context;
    AvailableProductItemBinding binding;
    public ItemClickListener clickListener;

    public AvailableProductAdapter(ArrayList<ItemAvailableQuantity> list, DialogFragment fragment, Context context, ItemClickListener clickListener) {
        this.fragment = fragment;
        this.list = list;
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public AvailableProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.available_product_item, parent, false);

        return new AvailableProductHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final AvailableProductHolder holder, final int position) {
        AvailableProductItemBinding binding = holder.binding;
        binding.executePendingBindings();
        binding.productName.setText(list.get(position).getItemName());
        binding.quantity.setText(list.get(position).getTotalQuan()+"");
        binding.quantityDesc.setText(list.get(position).getQuantityDescription());
        binding.unit.setText(list.get(position).getMeasureUnitArName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AvailableProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView productName;
        AvailableProductItemBinding binding;

        public AvailableProductHolder(View itemView, AvailableProductItemBinding binding) {
            super(itemView);
            this.binding = binding;
            productName = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.itemClicked(list.get(getAdapterPosition()));
            fragment.dismiss();
        }
    }

    public interface ItemClickListener {
        public void itemClicked(ItemAvailableQuantity itemAvailableQuantity);
    }

}