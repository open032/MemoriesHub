package lex.neuron.memorieshub.ui.titles.dir

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.databinding.DirItemBinding

class DirAdapter(
    private val longListener: DirAdapter.OnLongItemClickListener,
    private val clickListener: DirAdapter.OnClickListener
) :
    ListAdapter<DirEntity, DirAdapter.ListMemoViewHolder>(DiffCallback()) {
//    androidx.recyclerview.widget.

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMemoViewHolder {
        val binding = DirItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ListMemoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListMemoViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ListMemoViewHolder(private val binding: DirItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val dir = getItem(position)
                        clickListener.onItemClick(dir)
                    }
                }
                root.setOnLongClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val dir = getItem(position)
                        longListener.onLongItemClick(dir)
                    }
                    return@setOnLongClickListener true
                }
            }
        }

        fun bind(dirEntity: DirEntity) {
            binding.apply {
                tv.text = dirEntity.name
            }
        }
    }

    interface OnClickListener {
        fun onItemClick(dirEntity: DirEntity)
    }

    interface OnLongItemClickListener {
        fun onLongItemClick(dirEntity: DirEntity)
    }

    class DiffCallback : DiffUtil.ItemCallback<DirEntity>() {
        override fun areItemsTheSame(oldItem: DirEntity, newItem: DirEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: DirEntity, newItem: DirEntity) =
            oldItem == newItem
    }
}