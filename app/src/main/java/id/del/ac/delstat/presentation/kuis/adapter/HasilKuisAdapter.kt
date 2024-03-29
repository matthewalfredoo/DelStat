package id.del.ac.delstat.presentation.kuis.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.del.ac.delstat.data.model.kuis.HasilKuis
import id.del.ac.delstat.data.model.kuis.KumpulanKuis
import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.databinding.ListItemHasilkuisBinding
import id.del.ac.delstat.util.DateUtil

class HasilKuisAdapter(
    private val role: String
): RecyclerView.Adapter<MyHasilKuisViewHolder>() {
    private val listHasilKuis = ArrayList<HasilKuis>()

    fun setList(hasilKuis: List<HasilKuis>) {
        listHasilKuis.clear()
        listHasilKuis.addAll(hasilKuis)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHasilKuisViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemHasilkuisBinding.inflate(layoutInflater, parent, false)

        return MyHasilKuisViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHasilKuisViewHolder, position: Int) {
        holder.bind(listHasilKuis[position], role)
    }

    override fun getItemCount(): Int {
        return listHasilKuis.size
    }

}

class MyHasilKuisViewHolder(val binding: ListItemHasilkuisBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(hasilKuis: HasilKuis, role: String) {
        binding.namaKuis.text = KumpulanKuis.kumpulanKuis[hasilKuis.idKuis - 1].nama
        binding.nilaiHasilKuis.text = "Nilai: ${hasilKuis.nilaiKuis}"
        if(role != User.ROLE_SISWA ) {
            binding.namaUserHasilKuis.visibility = View.VISIBLE
            binding.namaUserHasilKuis.text = "Dikerjakan oleh: ${hasilKuis.namaUser}"
        }
        binding.tanggalHasilKuis.text = DateUtil.getDateTimeWithoutSecond(hasilKuis.createdAt)
    }

}