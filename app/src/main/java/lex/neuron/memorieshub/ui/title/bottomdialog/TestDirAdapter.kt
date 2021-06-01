package lex.neuron.memorieshub.ui.title.bottomdialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.data.entity.MemoEntity
import lex.neuron.memorieshub.data.entity.TitleEntity
import lex.neuron.memorieshub.databinding.ItemMemoBinding
import lex.neuron.memorieshub.databinding.TestItemBinding
import lex.neuron.memorieshub.ui.title.memo.MemoAdapter
import lex.neuron.memorieshub.ui.title.title.TitleAdapter

class TestDirAdapter(
    private val longListener: TestDirAdapter.OnLongItemClickListener,
    private val clickListener: TestDirAdapter.OnClickListener
) :
    ListAdapter<DirEntity, TestDirAdapter.ListMemoViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMemoViewHolder {
        val binding = TestItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ListMemoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListMemoViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ListMemoViewHolder(private val binding: TestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener{
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