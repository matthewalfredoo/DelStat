package id.del.ac.delstat.presentation.analisisdata.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.del.ac.delstat.R
import id.del.ac.delstat.data.model.analisisdata.AnalisisData
import id.del.ac.delstat.databinding.ListItemAnalisisDataBinding
import id.del.ac.delstat.util.DateUtil

class AnalisisDataAdapter(
    private val clickListener: (AnalisisData) -> Unit
): RecyclerView.Adapter<MyAnalisisDataViewHolder>() {
    private val listAnalisisData = ArrayList<AnalisisData>()

    fun setList(analisisData: List<AnalisisData>) {
        listAnalisisData.clear()
        listAnalisisData.addAll(analisisData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAnalisisDataViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemAnalisisDataBinding.inflate(layoutInflater, parent, false)

        return MyAnalisisDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyAnalisisDataViewHolder, position: Int) {
        holder.bind(listAnalisisData[position], clickListener)
    }

    override fun getItemCount(): Int {
        return listAnalisisData.size
    }


}

class MyAnalisisDataViewHolder(val binding: ListItemAnalisisDataBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(analisisData: AnalisisData, clickListener: (AnalisisData) -> Unit) {
        binding.judulAnalisisData.text = analisisData.judul
        binding.statusAnalisisData.text = analisisData.status
        if(analisisData.deskripsi!!.length > 50) {
            binding.deskripsiAnalisisData.text = analisisData.deskripsi.substring(0, 40) + "..."
        } else {
            binding.deskripsiAnalisisData.text = analisisData.deskripsi
        }
        binding.tanggalAnalisisData.text = DateUtil.getDateTimeWithoutSecond(analisisData.createdAt!!)
        binding.root.setOnClickListener {
            clickListener(analisisData)
        }
    }

}