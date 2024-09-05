package com.dataflowstores.dataflow.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dataflowstores.dataflow.pojo.invoice.MoveLines;
import com.dataflowstores.dataflow.R;

import java.util.ArrayList;
import java.util.Locale;


public class PrintingLinesAdapter extends RecyclerView.Adapter<PrintingLinesAdapter.PrintingLinesViewHolder> {

    private ArrayList<MoveLines> list = new ArrayList<>();

    public PrintingLinesAdapter(ArrayList<MoveLines> list) {
        this.list = list;
    }

    @Override
    public PrintingLinesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.line_item, parent, false);
        return new PrintingLinesViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final PrintingLinesViewHolder holder, final int position) {
        holder.itemName.setText(list.get(position).getItemName());
        holder.itemPrice.setText(String.format(Locale.US,"%.3f",Double.parseDouble( list.get(position).getPrice() )));
        holder.itemTotal.setText(String.format(Locale.US,"%.3f",Double.parseDouble( list.get(position).getNetPrice())));
        holder.itemQuantity.setText(String.format(Locale.US,"%.3f",Double.parseDouble(list.get(position).getTotalQuantity())));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PrintingLinesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemName,itemQuantity, itemPrice, itemTotal;

        public PrintingLinesViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemName= itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            itemTotal = itemView.findViewById(R.id.itemTotal);
        }

        @Override
        public void onClick(View v) {
        }
    }
}