package com.dataflowstores.dataflow.ui.searchItemPrice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.ProductPriceItemBinding;
import com.dataflowstores.dataflow.pojo.searchItemPrice.ItemPriceItem;

import java.util.ArrayList;
import java.util.Locale;


public class ItemPriceAdapter extends RecyclerView.Adapter<ItemPriceAdapter.ItemPriceHolder> {
    private ArrayList<ItemPriceItem> list = new ArrayList<>();
    public Context context;
    ProductPriceItemBinding binding;
    public ItemClickListener clickListener;

    public ItemPriceAdapter(ArrayList<ItemPriceItem> list, Context context, ItemClickListener clickListener) {
        this.list = list;
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public ItemPriceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.product_price_item, parent, false);

        return new ItemPriceHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ItemPriceHolder holder, final int position) {
        ProductPriceItemBinding binding = holder.binding;
        binding.executePendingBindings();
        binding.priceTypeName.setText(list.get(position).getPricesTypeName());
        binding.basicUnitQuantity.setText(list.get(position).getBasicUnitQuantity() + "");
        binding.price.setText(String.format(Locale.US, "%.3f", Double.parseDouble(list.get(position).getPrice())));
        binding.measureUnit.setText(list.get(position).getMeasureUnitArName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemPriceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView productName;
        ProductPriceItemBinding binding;

        public ItemPriceHolder(View itemView, ProductPriceItemBinding binding) {
            super(itemView);
            this.binding = binding;
            productName = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.itemClicked(list.get(getAdapterPosition()));
        }
    }

    public interface ItemClickListener {
        public void itemClicked(ItemPriceItem itemAvailableQuantity);
    }

}