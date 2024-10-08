package com.dataflowstores.dataflow.ui.reports.financialReport;

import android.annotation.SuppressLint;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dataflowstores.dataflow.pojo.financialReport.ReportItem;
import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.FinancialReportItemBinding;

import java.util.List;
import java.util.Locale;


public class FinancialReportAdapter extends RecyclerView.Adapter<FinancialReportAdapter.FinancialReportHolder> {

    private List<ReportItem> list;

    public FinancialReportAdapter(List<ReportItem> list) {
        this.list = list;
    }
    FinancialReportItemBinding binding;
    @Override
    public FinancialReportHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.financial_report_item,parent, false);
        View v = binding.getRoot();
        return new FinancialReportHolder(v, binding);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final FinancialReportHolder holder, final int position) {
        FinancialReportItemBinding binding= holder.binding;
        binding.executePendingBindings();
        binding.account.setText(list.get(position).getAccountName());
        binding.quantity.setText(list.get(position).getAccountCount());
        binding.total.setText(String.format(Locale.US,"%.3f", list.get(position).getAccountValue()));
        binding.actualTotal.setText(String.format(Locale.US,"%.3f", list.get(position).getNetValue()));
        binding.serial.setText(position+1+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class FinancialReportHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        FinancialReportItemBinding binding;
        public FinancialReportHolder(View itemView, FinancialReportItemBinding binding) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.binding = binding;
        }

        @Override
        public void onClick(View v) {
        }
    }
}