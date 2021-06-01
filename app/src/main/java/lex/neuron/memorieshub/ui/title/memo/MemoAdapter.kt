package lex.neuron.memorieshub.ui.title.memo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import lex.neuron.memorieshub.data.entity.MemoEntity
import lex.neuron.memorieshub.databinding.ItemMemoBinding


class MemoAdapter(private val clickListener: OnLongClickListener) :
    ListAdapter<MemoEntity, MemoAdapter.ListMemoViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMemoViewHolder {
        val binding = ItemMemoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ListMemoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListMemoViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ListMemoViewHolder(private val binding: ItemMemoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnLongClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val memo = getItem(position)
                        clickListener.onLongItemClick(memo)
                    }
                    return@setOnLongClickListener true
                }
            }
        }

        fun bind(memoEntity: MemoEntity) {
            binding.apply {
                tvMemoTitle.text = memoEntity.title
                tvMemoDes.text = memoEntity.description
            }
        }
    }

    interface OnLongClickListener {
        fun onLongItemClick(memoEntity: MemoEntity)
    }

    class DiffCallback : DiffUtil.ItemCallback<MemoEntity>() {
        override fun areItemsTheSame(oldItem: MemoEntity, newItem: MemoEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MemoEntity, newItem: MemoEntity) =
            oldItem == newItem
    }
}