package com.example.dataflow.ui.cashing;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dataflow.R;
import com.example.dataflow.pojo.invoice.MoveLines;

import java.util.ArrayList;
import java.util.Locale;


public class PrintingCashingAdapter extends RecyclerView.Adapter<PrintingCashingAdapter.PrintingCashingViewHolder> {

    private ArrayList<MoveLines> list = new ArrayList<>();
    private final int moveType;

    public PrintingCashingAdapter(ArrayList<MoveLines> list, int moveType) {
        this.list = list;
        this.moveType = moveType;
    }

    @Override
    public PrintingCashingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cashing_line_item, parent, false);
        return new PrintingCashingViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final PrintingCashingViewHolder holder, final int position) {
        holder.itemName.setText(list.get(position).getItemName());
        holder.fromStore.setText(list.get(position).getStoreNameFrom());
        Log.e("checkStore From", list.get(position).getStoreNameFrom() + "     --       "+ list.get(position).getStoreNameTo());
        if (list.get(position).getStoreNameTo() != null)
            holder.toStore.setText(list.get(position).getStoreNameTo());
        holder.itemQuantity.setText(String.format(Locale.US, "%.2f", Double.parseDouble(list.get(position).getTotalQuantity())));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PrintingCashingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemName, itemQuantity, fromStore, toStore;

        public PrintingCashingViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemName = itemView.findViewById(R.id.itemName);
            fromStore = itemView.findViewById(R.id.fromStore1);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            toStore = itemView.findViewById(R.id.toStore2);
            if (moveType == 14) {
                toStore.setVisibility(View.VISIBLE);
                toStore.setTextSize(12);
                fromStore.setTextSize(12);
            } else {
                toStore.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
        }
    }
}