import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dataflowstores.dataflow.databinding.DealerItemBinding
import com.dataflowstores.dataflow.pojo.report.dealersBalancesReport.DealersBalancesData

class DealerAdapter : ListAdapter<DealersBalancesData, DealerViewHolder>(DealerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealerViewHolder {
        val binding = DealerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DealerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DealerViewHolder, position: Int) {
        val dealer = getItem(position)
        holder.bind(dealer)
    }

    fun updateData(newData: List<DealersBalancesData>) {
        submitList(newData)
    }
}

class DealerViewHolder(private val binding: DealerItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(dealer: DealersBalancesData) {
        binding.dealerName.text = "${dealer.dealerName}"
        binding.initialBalance.text = " الرصيد الافتتاحي: ${dealer.initialBalance}"
        binding.currentBalance.text = " الرصيد النهائي: ${dealer.currentBalance}"
    }
}

class DealerDiffCallback : DiffUtil.ItemCallback<DealersBalancesData>() {

    override fun areItemsTheSame(
        oldItem: DealersBalancesData,
        newItem: DealersBalancesData
    ): Boolean {
        return oldItem.dealerISN == newItem.dealerISN // Compare based on unique identifier (id)
    }

    override fun areContentsTheSame(
        oldItem: DealersBalancesData,
        newItem: DealersBalancesData
    ): Boolean {
        return oldItem == newItem // Check for deep equality (optional)
    }
}
