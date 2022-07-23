package com.example.dataflow.ui.cashing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dataflow.App;
import com.example.dataflow.R;
import com.example.dataflow.pojo.product.Product;
import com.example.dataflow.pojo.product.ProductData;
import com.example.dataflow.pojo.users.SalesManData;
import com.example.dataflow.ui.ProductDetails;
import com.example.dataflow.ui.listeners.MyDialogCloseListener;

import java.util.ArrayList;


public class ProductAdapterCashing extends RecyclerView.Adapter<ProductAdapterCashing.ResultsAdapterCashingHolder> {
    private ArrayList<ProductData> list;
    public DialogFragment fragment;
    public Context context;
    public ClickListener clickListener;
    public int moveType;
    public ProductAdapterCashing(ArrayList<ProductData> list, DialogFragment fragment, Context context, ClickListener clickListener, int moveType) {
        this.fragment=fragment;
        this.list = list;
        this.context =context;
        this.clickListener = clickListener;
        this.moveType = moveType;
    }

    @Override
    public ResultsAdapterCashingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_text_item, parent, false);
        return new ResultsAdapterCashingHolder(v);
    }

    @Override
    public void onBindViewHolder(final ResultsAdapterCashingHolder holder, final int position) {
        holder.productName.setText(list.get(position).getItemName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ResultsAdapterCashingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView productName;
        public ResultsAdapterCashingHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            App.product = list.get(getAdapterPosition());
            if(App.product.getSerial()) {
                clickListener.serialClicked(getAdapterPosition());
            } else {
                Intent intent = new Intent(context, ProductScreenCashing.class);
                intent.putExtra("moveType", moveType);
                context.startActivity(intent);
                ((Activity)context).finish();
                fragment.dismiss();}
        }
    }

    public interface ClickListener {
        public void serialClicked(int position);
    }
}