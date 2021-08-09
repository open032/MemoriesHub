package lex.neuron.memorieshub.ui.titles.dir

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
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.databinding.ItemDirBinding

class DirAdapter(
    private val renameItem: RenameItem,
    private val deleteItem: DeleteItem,
    private val longListener: OnLongItemClickListener,
    private val clickListener: OnClickListener
) :
    ListAdapter<DirEntity, DirAdapter.ListDirViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDirViewHolder {
        val binding = ItemDirBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ListDirViewHolder(binding)
    }

    lateinit var context: Context

    override fun onBindViewHolder(holder: ListDirViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
        context = holder.itemView.context
    }

    inner class ListDirViewHolder(private val binding: ItemDirBinding) :
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
                vert.setOnClickListener {

                    val popup = PopupMenu(context, vert)

                    popup.menuInflater.inflate(R.menu.menu_dir_item, popup.menu)
                    popup.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.rename -> {
                                val position = adapterPosition
                                if (position != RecyclerView.NO_POSITION) {
                                    val dir = getItem(position)
                                    renameItem.renameItem(dir)
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

    interface OnClickListener {
        fun onItemClick(dirEntity: DirEntity)
    }

    interface DeleteItem {
        fun deleteItem(dirEntity: DirEntity)
    }

    interface RenameItem {
        fun renameItem(dirEntity: DirEntity)
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