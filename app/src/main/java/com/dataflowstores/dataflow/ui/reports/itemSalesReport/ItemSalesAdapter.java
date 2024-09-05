package com.dataflowstores.dataflow.ui.reports.itemSalesReport;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.FinancialReportItemBinding;
import com.dataflowstores.dataflow.databinding.ItemSalesReportItemBinding;
import com.dataflowstores.dataflow.pojo.financialReport.ReportItem;
import com.dataflowstores.dataflow.pojo.report.itemSalesReport.ItemSalesData;

import java.util.List;
import java.util.Locale;

public class ItemSalesAdapter extends RecyclerView.Adapter<ItemSalesAdapter.ItemSalesHolder> {

    private List<ItemSalesData> list;

    public ItemSalesAdapter(List<ItemSalesData> list) {
        this.list = list;
    }
    ItemSalesReportItemBinding binding;
    @Override
    public ItemSalesAdapter.ItemSalesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_sales_report_item,parent, false);
        View v = binding.getRoot();
        return new ItemSalesAdapter.ItemSalesHolder(v, binding);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ItemSalesAdapter.ItemSalesHolder holder, final int position) {
        ItemSalesReportItemBinding binding= holder.binding;
        binding.executePendingBindings();
        binding.tCount.setText(list.get(position).getTCount()+"");
        binding.itemName.setText(list.get(position).getItemName());
        binding.moveName.setText( list.get(position).getMoveName());
        binding.quantityDescription.setText( list.get(position).getQuantityDescription());
        binding.netPrice.setText(String.format(Locale.US,"%.3f", Double.parseDouble( list.get(position).getNetPrice())));
        binding.serial.setText(position+1+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ItemSalesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemSalesReportItemBinding binding;
        public ItemSalesHolder(View itemView, ItemSalesReportItemBinding binding) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.binding = binding;
        }

        @Override
        public void onClick(View v) {
        }
    }

}
