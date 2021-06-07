package lex.neuron.memorieshub.ui.title.deletedir

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import lex.neuron.memorieshub.R
import lex.neuron.memorieshub.ui.title.title.TitleAdapter


class DeleteDirAdapter(
    val list: MutableList<DeleteDirItem>,
    private val clickListener: OnClickListener
) :
    RecyclerView.Adapter<DeleteDirAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            itemView =
            LayoutInflater.from(parent.context).inflate(
                R.layout.delete_dir,
                parent, false
            ), clickListener
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemView = list[position])
    }

    override fun getItemCount(): Int {
        return list.count()
    }

    class ViewHolder(itemView: View, click: OnClickListener) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private val tv: TextView = itemView.findViewById(R.id.tv)

        init {
            tv.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    click.onItemClick()
                }
            }
        }

        override fun onClick(v: View?) {
        }

        fun bind(itemView: DeleteDirItem) {
            tv.text = itemView.deleteDir
        }

    }

    interface OnClickListener {
        fun onItemClick()
    }
}