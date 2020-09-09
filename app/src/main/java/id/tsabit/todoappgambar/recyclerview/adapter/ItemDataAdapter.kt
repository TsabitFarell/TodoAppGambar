package id.tsabit.todoappgambar.recyclerview.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import id.tsabit.todoappgambar.AddDataActivity
import id.tsabit.todoappgambar.MainActivity
import id.tsabit.todoappgambar.databinding.ItemDataBinding
import id.tsabit.todoappgambar.model.ModelData
import id.tsabit.todoappgambar.recyclerview.viewholder.ItemDataVH

class ItemDataAdapter : RecyclerView.Adapter<ItemDataVH>() {
    private val listData = arrayListOf<ModelData>()

    fun addData(data: List<ModelData>) {
        listData.clear()
        listData.addAll(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDataVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDataBinding.inflate(inflater, parent, false)
        return ItemDataVH(binding)
    }

    override fun onBindViewHolder(holder: ItemDataVH, position: Int) {
        val data = listData[position]
        holder.bind(data)

        holder.itemView.setOnClickListener { view ->
            val intent = Intent(view.context, AddDataActivity::class.java)
            intent.putExtra("DATA", data)
            view.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listData.size
}