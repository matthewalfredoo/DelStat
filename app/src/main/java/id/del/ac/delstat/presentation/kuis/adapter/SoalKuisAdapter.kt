package id.del.ac.delstat.presentation.kuis.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.davemorrissey.labs.subscaleview.ImageSource
import id.del.ac.delstat.data.model.kuis.SoalKuis
import id.del.ac.delstat.databinding.ListItemSoalKuisBinding

class SoalKuisAdapter(
    private val onClickPilihanJawaban: (SoalKuis, String) -> Unit
): RecyclerView.Adapter<MySoalKuisViewHolder>() {
    private val listSoalKuis = ArrayList<SoalKuis>()

    fun setList(soalKuis: List<SoalKuis>) {
        listSoalKuis.clear()
        listSoalKuis.addAll(soalKuis)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySoalKuisViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemSoalKuisBinding.inflate(layoutInflater, parent, false)

        return MySoalKuisViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MySoalKuisViewHolder, position: Int) {
        holder.bind(listSoalKuis[position], onClickPilihanJawaban)
    }

    override fun getItemCount(): Int {
        return listSoalKuis.size
    }

}

class MySoalKuisViewHolder(val binding: ListItemSoalKuisBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(soalKuis: SoalKuis, onClickPilihanJawaban: (SoalKuis, String) -> Unit) {
        binding.soalSoalKuis.text = soalKuis.soal
        binding.radioJawaban1.text = soalKuis.listPilihanJawaban!![0]
        binding.radioJawaban2.text = soalKuis.listPilihanJawaban!![1]
        binding.radioJawaban3.text = soalKuis.listPilihanJawaban!![2]
        binding.radioJawaban4.text = soalKuis.listPilihanJawaban!![3]

        if(soalKuis.keterangan != null) {
            binding.keteranganSoalKuis.text = soalKuis.keterangan
            binding.keteranganSoalKuis.visibility = View.VISIBLE
        }
        if(soalKuis.gambar != null) {
            binding.gambarSoalKuis.setImage(ImageSource.resource(soalKuis.gambar!!))
            binding.gambarSoalKuis.visibility = View.VISIBLE
        }

        binding.radioGroupJawabanSoalKuis.setOnCheckedChangeListener { radioGroup, i ->
            val radioButton = radioGroup.findViewById<View>(i)
            val index = radioGroup.indexOfChild(radioButton)
            onClickPilihanJawaban(soalKuis, soalKuis.listPilihanJawaban!![index])
        }
    }

}
