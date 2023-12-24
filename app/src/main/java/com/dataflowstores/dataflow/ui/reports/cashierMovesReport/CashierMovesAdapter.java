package com.dataflowstores.dataflow.ui.reports.cashierMovesReport;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflowstores.dataflow.R;
import com.dataflowstores.dataflow.databinding.CashierMovesItemBinding;
import com.dataflowstores.dataflow.pojo.report.cashierMoves.CashierMoveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CashierMovesAdapter extends RecyclerView.Adapter<CashierMovesAdapter.CashierMovesHolder> {

    public List<CashierMoveData> list;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
    public onCashierMoveListener listener;
    public CashierMovesAdapter(List<CashierMoveData> list, onCashierMoveListener listener) {
        this.list = list;
        this.listener = listener;
    }

    CashierMovesItemBinding binding;

    @Override
    public CashierMovesAdapter.CashierMovesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.cashier_moves_item, parent, false);
        View v = binding.getRoot();
        return new CashierMovesAdapter.CashierMovesHolder(v, binding);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final CashierMovesAdapter.CashierMovesHolder holder, final int position) {
        CashierMovesItemBinding binding = holder.binding;
        binding.executePendingBindings();
        String dealerName = "<b>" + "المتعامل:" + "</b> " + list.get(position).getDealerName();
        binding.dealerName.setText(Html.fromHtml(dealerName));
        String moveId = "<b>" + "رقم الإذن:" + "</b> " + list.get(position).getMoveID();
        binding.moveId.setText(Html.fromHtml(moveId));
        String moveName = "<b>" + "الحركة:" + "</b> " + list.get(position).getMoveName();
        binding.moveName.setText(Html.fromHtml(moveName));
        Date date;
        try {
          date = sdf.parse(list.get(position).getCreateDate());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assert date != null;
        String createdDate = "<b>" + "التوقيت:" + "</b> " + sdf.format(date);
        binding.createdDate.setText(Html.fromHtml(createdDate));
        String workingDayDate = "<b>" + "اليومية:" + "</b> " + list.get(position).getWorkingDayDate();
        binding.workingDayDate.setText(Html.fromHtml(workingDayDate));
        String netValue = "<b>" + "الصافي:" + "</b> " + String.format(Locale.US, "%.2f", Double.parseDouble(list.get(position).getNetValue()));
        binding.netValue.setText(Html.fromHtml(netValue));
        int serialNum = position + 1;
        binding.serialNum.setText(serialNum + "");

        if (!list.get(position).getHeaderNotes().isEmpty()) {
            binding.headerNotes.setText(list.get(position).getHeaderNotes());
            binding.headerNotes.setVisibility(View.VISIBLE);
        } else {
            binding.headerNotes.setVisibility(View.GONE);
        }
        if (!list.get(position).getPrev_balance().isEmpty()) {
            binding.prevBalance.setText(list.get(position).getPrev_balance());
            binding.prevBalance.setVisibility(View.VISIBLE);
        } else {
            binding.prevBalance.setVisibility(View.GONE);
        }
        if (!list.get(position).getCurrent_balance().isEmpty()) {
            binding.currentBalance.setText(list.get(position).getCurrent_balance());
            binding.currentBalance.setVisibility(View.VISIBLE);
        } else {
            binding.currentBalance.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CashierMovesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CashierMovesItemBinding binding;

        public CashierMovesHolder(View itemView, CashierMovesItemBinding binding) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.binding = binding;
        }

        @Override
        public void onClick(View v) {
            listener.onCashierMoveClick(list.get(getAdapterPosition()));
        }
    }
    public interface onCashierMoveListener{
        void onCashierMoveClick(CashierMoveData cashierMoveData);
    }
}
