package id.del.ac.delstat.presentation.literatur.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.del.ac.delstat.data.model.literatur.Literatur
import id.del.ac.delstat.databinding.ListItemLiteraturBinding

class LiteraturAdapter(
    private val clickListener: (Literatur) -> Unit
): RecyclerView.Adapter<MyLiteraturViewHolder>() {
    private val listLiteratur = ArrayList<Literatur>()

    fun setList(literatur: List<Literatur>) {
        listLiteratur.clear()
        listLiteratur.addAll(literatur)
    }

    fun emptyList() {
        listLiteratur.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyLiteraturViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemLiteraturBinding.inflate(layoutInflater, parent, false)

        return MyLiteraturViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyLiteraturViewHolder, position: Int) {
        holder.bind(listLiteratur[position], clickListener)
    }

    override fun getItemCount(): Int {
        return listLiteratur.size
    }

}

class MyLiteraturViewHolder(val binding: ListItemLiteraturBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(literatur: Literatur, clickListener: (Literatur) -> Unit) {
        binding.judulLiteratur.text = literatur.judul
        val deskripsi = "${literatur.penulis} - ${literatur.tahunTerbit}"
        binding.deskripsiLiteratur.text = deskripsi
        binding.root.setOnClickListener {
            clickListener(literatur)
        }
    }

}
