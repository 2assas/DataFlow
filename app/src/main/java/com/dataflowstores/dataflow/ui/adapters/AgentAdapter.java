package com.dataflowstores.dataflow.ui.adapters;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dataflowstores.dataflow.App;
import com.dataflowstores.dataflow.pojo.users.SalesManData;
import com.dataflowstores.dataflow.R;

import java.util.ArrayList;


public class AgentAdapter extends RecyclerView.Adapter<AgentAdapter.ResultsAdapterHolder> {
    private ArrayList<SalesManData> list;
    public DialogFragment fragment;
    public AgentAdapter(ArrayList<SalesManData> list, DialogFragment fragment) {
        this.fragment=fragment;
        this.list = list;
    }

    @Override
    public ResultsAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_text_item, parent, false);
        return new ResultsAdapterHolder(v);
    }


    @Override
    public void onBindViewHolder(final ResultsAdapterHolder holder, final int position) {
        holder.customerName.setText(list.get(position).getDealerName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ResultsAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView customerName;
        public ResultsAdapterHolder(View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            App.agent = list.get(getAdapterPosition());
            fragment.dismiss();
        }
    }
}