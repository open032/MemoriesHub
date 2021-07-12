package lex.neuron.memorieshub.ui.titles.testing

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import lex.neuron.memorieshub.databinding.TestingItemBinding
import lex.neuron.memorieshub.permission.internet.TAG

class TestingAdapterRight(private val clickListener: TestingAdapterRight.OnClickListenerRight) :
    ListAdapter<TestingList, TestingAdapterRight.ListRightViewHolder>(DiffCallback()) {

    var int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListRightViewHolder {
        val binding = TestingItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ListRightViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListRightViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    inner class ListRightViewHolder(private val binding: TestingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        var leftList = getItem(position)
                        clickListener.onItemClickRight(leftList.id, position)
//                        Log.d(TAG, "leftLst getItem: $leftList")
                        /*if (int == 1) {
                            int++
                            root.setCardBackgroundColor(Color.parseColor("#006600"))
                        }*/
//                        Log.d(TAG, "ClickListener root: $position")
                    }
                }
            }
        }


        fun bind(memoEntity: TestingList) {
            binding.apply {
                tv.text = memoEntity.name

            }
        }
    }

    interface OnClickListenerRight {
        fun onItemClickRight(id: Int, position: Int)
    }

    class DiffCallback : DiffUtil.ItemCallback<TestingList>() {
        override fun areItemsTheSame(oldItem: TestingList, newItem: TestingList) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TestingList, newItem: TestingList) =
            oldItem == newItem
    }
}