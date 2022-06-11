package id.del.ac.delstat.presentation.kuis.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.del.ac.delstat.data.model.kuis.Kuis
import id.del.ac.delstat.databinding.ListItemKuisBinding

class KuisAdapter(
    private val clickListener: (Kuis) -> Unit
): RecyclerView.Adapter<MyKuisViewHolder>() {
    private val listKuis = ArrayList<Kuis>()

    fun setList(kuis: List<Kuis>) {
        listKuis.clear()
        listKuis.addAll(kuis)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyKuisViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemKuisBinding.inflate(layoutInflater, parent, false)

        return MyKuisViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyKuisViewHolder, position: Int) {
        holder.bind(listKuis[position], clickListener)
    }

    override fun getItemCount(): Int {
        return listKuis.size
    }


}

class MyKuisViewHolder(val binding: ListItemKuisBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(kuis: Kuis, clickListener: (Kuis) -> Unit) {
        binding.idKuis.text = "#${kuis.id}"
        binding.namaKuis.text = kuis.nama
        binding.root.setOnClickListener {
            clickListener(kuis)
        }
    }

}