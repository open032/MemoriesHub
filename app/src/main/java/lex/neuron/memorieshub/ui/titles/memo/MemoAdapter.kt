package lex.neuron.memorieshub.ui.titles.memo

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.data.entity.MemoEntity
import lex.neuron.memorieshub.databinding.ItemMemoBinding


class MemoAdapter(
    private val renameItem: RenameItem,
    private val deleteItem: DeleteItem,
) :
    ListAdapter<MemoEntity, MemoAdapter.ListMemoViewHolder>(DiffCallback()) {

    lateinit var context: Context

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
        context = holder.itemView.context
    }

    inner class ListMemoViewHolder(private val binding: ItemMemoBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(memoEntity: MemoEntity) {
            binding.apply {
                tvMemoTitle.text = memoEntity.title
                tvMemoDes.text = memoEntity.description


                vert.setOnClickListener {

                    val popup = PopupMenu(context, vert)

                    popup.menuInflater.inflate(R.menu.menu_dir_item, popup.menu)
                    popup.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.rename -> {
                                val position = adapterPosition
                                if (position != RecyclerView.NO_POSITION) {
                                    val memo = getItem(position)
                                    renameItem.renameItem(memo)
                                }
                                Log.d(TAG, "rename: ")
                            }
                            R.id.delete -> {
                                val position = adapterPosition
                                if (position != RecyclerView.NO_POSITION) {
                                    val dir = getItem(position)
                                    deleteItem.deleteItem(dir)
                                }
                                Log.d(TAG, "delete: ")
                            }
                        }
                        true
                    }

                    popup.show()
                    Log.e(TAG, "bind: ")
                }
            }
        }
    }

    interface DeleteItem {
        fun deleteItem(memoEntity: MemoEntity)
    }

    interface RenameItem {
        fun renameItem(memoEntity: MemoEntity)
    }

    class DiffCallback : DiffUtil.ItemCallback<MemoEntity>() {
        override fun areItemsTheSame(oldItem: MemoEntity, newItem: MemoEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MemoEntity, newItem: MemoEntity) =
            oldItem == newItem
    }
}