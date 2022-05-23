package id.del.ac.delstat.presentation.notifikasi.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import id.del.ac.delstat.data.model.notifikasi.Notifikasi
import id.del.ac.delstat.databinding.ListItemNotifikasiBinding
import id.del.ac.delstat.util.DateUtil

class NotifikasiAdapter(
    private val clickListener: (Notifikasi) -> Unit
): RecyclerView.Adapter<MyNotifikasiViewHolder>() {
    private val listNotifikasi = ArrayList<Notifikasi>()

    fun setList(notifikasi: List<Notifikasi>) {
        listNotifikasi.clear()
        listNotifikasi.addAll(notifikasi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNotifikasiViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemNotifikasiBinding.inflate(layoutInflater, parent, false)

        return MyNotifikasiViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MyNotifikasiViewHolder, position: Int) {
        holder.bind(listNotifikasi[position], clickListener)
    }

    override fun getItemCount(): Int {
        return listNotifikasi.size
    }

}

class MyNotifikasiViewHolder(val binding: ListItemNotifikasiBinding): RecyclerView.ViewHolder(binding.root) {

    @RequiresApi(Build.VERSION_CODES.M)
    fun bind(notifikasi: Notifikasi, clickListener: (Notifikasi) -> Unit) {
        binding.judulNotifikasi.text = notifikasi.jenisNotifikasi
        binding.deskripsiNotifikasi.text = notifikasi.deskripsi
        binding.tanggalNotifikasi.text = DateUtil.getDateTime(notifikasi.createdAt)
        if(!notifikasi.sudahDibaca) {
            binding.notificationBadge.visibility = View.VISIBLE
        }
        binding.root.setOnClickListener {
            clickListener(notifikasi)
        }
    }

}