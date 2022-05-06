package id.del.ac.delstat.presentation.testactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.ActivityMateriBinding
import id.del.ac.delstat.presentation.di.Injector
import id.del.ac.delstat.presentation.materi.viewmodel.MateriViewModel
import id.del.ac.delstat.presentation.materi.viewmodel.MateriViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MateriActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMateriBinding

    @Inject
    lateinit var materiViewModelFactory: MateriViewModelFactory
    private lateinit var materiViewModel: MateriViewModel

    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var bearerToken: String

    @Inject
    lateinit var delStatApiService: DelStatApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMateriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as Injector).createMateriSubComponent().inject(this)

        materiViewModel = ViewModelProvider(this, materiViewModelFactory)
            .get(MateriViewModel::class.java)

        prepareUI()
    }

    private fun prepareUI() {
        runBlocking {
            bearerToken = userPreferences.getUserToken.first()!!
            bearerToken = "Bearer $bearerToken"
        }

        binding.btnGetMateri.setOnClickListener {
            getMateri()
        }

        binding.btnGetMateriEdit.setOnClickListener {
            updateMateri()
        }
    }

    private fun updateMateri() {
        val idMateri = binding.etIdMateriEdit.text.toString().toInt()
        val linkYoutube = binding.etLinkVideoMateriEdit.text.toString()
        lifecycleScope.launch {
            val response = delStatApiService.updateMateri(bearerToken, idMateri, linkYoutube)
            Log.d("MyTag", response.toString())
            Log.d("MyTag", response.body().toString())
        }
    }

    private fun getMateri() {
        val idMateri = binding.etIdMateri.text.toString().toInt()
        lifecycleScope.launch {
            val response = delStatApiService.getMateri(idMateri)
            Log.d("MyTag", response.toString())
            Log.d("MyTag", response.body().toString())
        }
    }
}