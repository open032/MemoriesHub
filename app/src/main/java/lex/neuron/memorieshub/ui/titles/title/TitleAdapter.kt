package lex.neuron.memorieshub.ui.titles.title


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
import lex.neuron.memorieshub.data.entity.TitleEntity
import lex.neuron.memorieshub.databinding.ItemTitleBinding


class TitleAdapter(
    private val renameItem: RenameItem,
    private val deleteItem: DeleteItem,
    private val testingItem: TestingItem,
    private val clickListener: OnClickListener
) :
    ListAdapter<TitleEntity, TitleAdapter.ListMainViewHolder>(DiffCallback()) {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListMainViewHolder {
        val binding = ItemTitleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ListMainViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ListMainViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
        context = holder.itemView.context
    }

    inner class ListMainViewHolder(private val binding: ItemTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val titleCard = getItem(position)
                        clickListener.onItemClick(titleCard)
                    }
                }
                root.setOnLongClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val titleCard = getItem(position)
//                        longListener.onLongItemClick(titleCard)
                    }
                    return@setOnLongClickListener true
                }
            }
        }

        fun bind(titleEntity: TitleEntity) {
            binding.apply {
                textViewName.text = titleEntity.name

                vert.setOnClickListener {

                    val popup = PopupMenu(context, vert)

                    popup.menuInflater.inflate(R.menu.menu_title_item, popup.menu)
                    popup.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.rename -> {
                                val position = adapterPosition
                                if (position != RecyclerView.NO_POSITION) {
                                    val title = getItem(position)
                                    renameItem.renameItem(title)
                                }
                                Log.d(TAG, "rename: ")
                            }
                            R.id.delete -> {
                                val position = adapterPosition
                                if (position != RecyclerView.NO_POSITION) {
                                    val title = getItem(position)
                                    deleteItem.deleteItem(title)
                                }
                                Log.d(TAG, "delete: ")
                            }
                            R.id.testing -> {
                                val position = adapterPosition
                                if (position != RecyclerView.NO_POSITION) {
                                    val title = getItem(position)
                                    testingItem.testingItem(title)
                                }
                                Log.d(TAG, "testing: ")
                            }
                        }
                        true
                    }

                    popup.show()
                }
            }
        }
    }


    interface DeleteItem {
        fun deleteItem(titleEntity: TitleEntity)
    }

    interface RenameItem {
        fun renameItem(titleEntity: TitleEntity)
    }

    interface TestingItem {
        fun testingItem(titleEntity: TitleEntity)
    }

    interface OnClickListener {
        fun onItemClick(titleEntity: TitleEntity)
    }

    class DiffCallback : DiffUtil.ItemCallback<TitleEntity>() {
        override fun areItemsTheSame(oldItem: TitleEntity, newItem: TitleEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TitleEntity, newItem: TitleEntity) =
            oldItem == newItem
    }
}
