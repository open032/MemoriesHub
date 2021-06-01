package lex.neuron.memorieshub.ui.title.title


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import lex.neuron.memorieshub.data.entity.TitleEntity
import lex.neuron.memorieshub.databinding.ItemTitleBinding


class TitleAdapter(
    private val longListener: OnLongItemClickListener,
    private val clickListener: OnClickListener
) :
    ListAdapter<TitleEntity, TitleAdapter.ListMainViewHolder>(DiffCallback()) {

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
                        longListener.onLongItemClick(titleCard)
                    }
                    return@setOnLongClickListener true
                }
            }
        }

        fun bind(titleEntity: TitleEntity) {
            binding.apply {
                textViewName.text = titleEntity.name
            }
        }
    }

    interface OnClickListener {
        fun onItemClick(titleEntity: TitleEntity)
    }

    interface OnLongItemClickListener {
        fun onLongItemClick(titleEntity: TitleEntity)
    }

    class DiffCallback : DiffUtil.ItemCallback<TitleEntity>() {
        override fun areItemsTheSame(oldItem: TitleEntity, newItem: TitleEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TitleEntity, newItem: TitleEntity) =
            oldItem == newItem
    }
}
