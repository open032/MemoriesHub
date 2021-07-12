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

class TestingAdapterLeft(private val clickListener: TestingAdapterLeft.OnClickListener) :
    ListAdapter</*MemoEntity*/TestingList, TestingAdapterLeft.ListLeftViewHolder>(DiffCallback()) {

    var int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListLeftViewHolder {
        val binding = TestingItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ListLeftViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListLeftViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    inner class ListLeftViewHolder(private val binding: TestingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        var leftList = getItem(position)

//                        Log.d(TAG, "leftLst getItem: $leftList")
                        if (int == 1) {
                            int++
                            clickListener.onItemClickLeft(leftList.id, position)
                            root.setCardBackgroundColor(Color.parseColor("#006600"))
                        }/* else {
                            root.setCardBackgroundColor(Color.parseColor("#31394E"))
                        }*/
//                        Log.d(TAG, "ClickListener root: $position")
                    }
                }
            }
        }


        fun bind(memoEntity: TestingList) {
            binding.apply {
                tv.text = memoEntity.name
                root.setCardBackgroundColor(Color.parseColor("#31394E"))
            }
        }
    }

    interface OnClickListener {
        fun onItemClickLeft(id: Int, position: Int)
    }

    class DiffCallback : DiffUtil.ItemCallback<TestingList>() {
        override fun areItemsTheSame(oldItem: TestingList, newItem: TestingList) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TestingList, newItem: TestingList) =
            oldItem == newItem
    }
}