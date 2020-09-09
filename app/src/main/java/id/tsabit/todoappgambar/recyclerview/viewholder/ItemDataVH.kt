package id.tsabit.todoappgambar.recyclerview.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.tsabit.todoappgambar.R
import id.tsabit.todoappgambar.databinding.ItemDataBinding
import id.tsabit.todoappgambar.model.ModelData

class ItemDataVH(private val binding: ItemDataBinding) : RecyclerView.ViewHolder(binding.root) {


    fun bind(data: ModelData) {
        Glide.with(binding.root.context)
            .load(data.profile_gambar)
            .centerCrop()
            .placeholder(R.drawable.gambar_placeholder)
            .into(binding.imgProfile)

        binding.run {
            txtNama.text = data.profile_nama
            txtKelas.text = data.profile_kelas
            txtAlamat.text = data.profile_alamat
        }
    }
}