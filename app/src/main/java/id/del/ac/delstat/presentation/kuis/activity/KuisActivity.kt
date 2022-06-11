package id.del.ac.delstat.presentation.kuis.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.R
import id.del.ac.delstat.data.model.kuis.Kuis
import id.del.ac.delstat.data.model.kuis.KumpulanKuis
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.ActivityKuisBinding
import id.del.ac.delstat.presentation.kuis.adapter.KuisAdapter
import id.del.ac.delstat.presentation.kuis.adapter.SoalKuisAdapter
import id.del.ac.delstat.presentation.kuis.viewmodel.HasilKuisViewModel
import id.del.ac.delstat.presentation.kuis.viewmodel.HasilKuisViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class KuisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKuisBinding

    @Inject
    lateinit var kuisViewModelFactory: HasilKuisViewModelFactory
    lateinit var kuisViewModel: HasilKuisViewModel

    private var idKuis = -1
    private lateinit var kuis: Kuis

    private lateinit var soalKuisAdapter: SoalKuisAdapter

    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var bearerToken: String

    companion object{
        const val EXTRA_ID_KUIS = "extra_id_kuis"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKuisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kuisViewModel = ViewModelProvider(this, kuisViewModelFactory)
            .get(HasilKuisViewModel::class.java)

        prepareUI()
        initRecyclerView()
    }

    private fun prepareUI() {
        idKuis = intent.getIntExtra(EXTRA_ID_KUIS, -1)

        if(idKuis == -1){
            Snackbar
                .make(binding.root, "Terjadi kesalahan saat mengakses kuis", Snackbar.LENGTH_SHORT)
                .show()

            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 500)
        }

        // else
        kuis = KumpulanKuis.kumpulanKuis.get(idKuis - 1)
        Log.d("MyTag", kuis.nama!!)

        runBlocking {
            bearerToken = userPreferences.getUserToken.first()!!
            bearerToken = "Bearer $bearerToken"
        }
    }

    private fun initRecyclerView() {
        soalKuisAdapter = SoalKuisAdapter()
        binding.recyclerViewSoalKuis.adapter = soalKuisAdapter
        binding.recyclerViewSoalKuis.layoutManager = LinearLayoutManager(this)
        displaySoalKuis()
    }

    private fun displaySoalKuis() {
        soalKuisAdapter.setList(kuis.listSoal!!)
        soalKuisAdapter.notifyDataSetChanged()
    }
}