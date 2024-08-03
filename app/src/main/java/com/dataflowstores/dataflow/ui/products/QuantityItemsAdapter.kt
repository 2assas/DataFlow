package com.dataflowstores.dataflow.ui.products

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dataflowstores.dataflow.databinding.QuantityListItemBinding
import com.dataflowstores.dataflow.pojo.product.ProductData
import java.util.Locale

class QuantityItemsAdapter(
    private val context: Context,
    private val listener: QuantityItemListener
) :
    RecyclerView.Adapter<QuantityItemsAdapter.ItemViewHolder>() {

    private var items = mutableListOf<QuantityItem>() // List to hold items

    // Interface to define what data an Item class should hold (replace with your actual data)
    data class QuantityItem(
        val serial: Int,
        val quantity: Double
    )

    // Inner class for the ViewHolder using View Binding
    inner class ItemViewHolder(private val binding: QuantityListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: QuantityItem) {
            val pos = adapterPosition + 1
            binding.serialTV.text = pos.toString()
            binding.quantityTV.text = String.format(Locale.ENGLISH, "%.3f", item.quantity)
        }
    }

    fun callDeleteFunction(pos: Int) {
        notifyItemRemoved(pos)
        notifyItemRangeChanged(pos, itemCount - pos)
        listener.onDeleteItem(items[pos].quantity)
        items.removeAt(pos)
    }

    // Function to add items to the adapter's list
    fun addItems(newItem: QuantityItem) {
        items.add(newItem)
        notifyItemInserted(items.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = QuantityListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}

public interface QuantityItemListener {
    fun onDeleteItem(itemQuantity: Double)
}
