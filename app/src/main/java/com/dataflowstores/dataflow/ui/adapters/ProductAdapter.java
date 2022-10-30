package com.dataflowstores.dataflow.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.pojo.product.ProductData;
import com.dataflowstores.dataflow.ui.StoreReportScreen;
import com.dataflowstores.dataflow.ui.products.ProductDetails;
import com.dataflowstores.dataflow.ui.searchItemPrice.SearchItemPrice;
import com.dataflowstores.dataflow.R;

import java.util.ArrayList;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ResultsAdapterHolder> {
    private ArrayList<ProductData> list;
    public DialogFragment fragment;
    public Context context;
    public ClickListener clickListener;

    public ProductAdapter(ArrayList<ProductData> list, DialogFragment fragment, Context context, ClickListener clickListener) {
        this.fragment = fragment;
        this.list = list;
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public ResultsAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_text_item, parent, false);
        return new ResultsAdapterHolder(v);
    }

    @Override
    public void onBindViewHolder(final ResultsAdapterHolder holder, final int position) {
        holder.productName.setText(list.get(position).getItemName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ResultsAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView productName;

        public ResultsAdapterHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            App.product = list.get(getAdapterPosition());
            if (App.product.getSerial()) {
                clickListener.serialClicked(getAdapterPosition());
            } else if (context instanceof StoreReportScreen || context instanceof SearchItemPrice) {
                fragment.dismiss();
            } else {
                context.startActivity(new Intent(context, ProductDetails.class));
                ((Activity) context).finish();
                fragment.dismiss();
            }
        }
    }

    public interface ClickListener {
        public void serialClicked(int position);
    }
}