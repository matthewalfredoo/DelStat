package id.del.ac.delstat.presentation.analisisdata.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.BuildConfig
import id.del.ac.delstat.R
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.ActivityDetailAnalisisDataBinding
import id.del.ac.delstat.presentation.analisisdata.viewmodel.AnalisisDataViewModel
import id.del.ac.delstat.presentation.analisisdata.viewmodel.AnalisisDataViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class DetailAnalisisDataActivity : AppCompatActivity() {

    companion object{
        const val DATA_ANALISIS_DATA = "data_analisis_data"
    }

    private lateinit var binding: ActivityDetailAnalisisDataBinding

    @Inject
    lateinit var analisisDataViewModelFactory: AnalisisDataViewModelFactory
    private lateinit var analisisDataViewModel: AnalisisDataViewModel

    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var bearerToken: String
    private var idAnalisisData: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAnalisisDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        analisisDataViewModel = ViewModelProvider(this, analisisDataViewModelFactory)
            .get(AnalisisDataViewModel::class.java)

        prepareUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_action_bar_detailanalisisdataactivity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.editAnalisisData -> {
                binding.editTextDeskripsi.isEnabled = true
                binding.editTextDeskripsi.requestFocus()
                val imm: InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.editTextDeskripsi, InputMethodManager.SHOW_IMPLICIT)

                binding.buttonEditAnalisisData.visibility = android.view.View.VISIBLE
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun prepareUI() {
        runBlocking {
            bearerToken = "Bearer ${userPreferences.getUserToken.first()!!}"
        }

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail Analisis Data"

        idAnalisisData = intent.getIntExtra(DATA_ANALISIS_DATA, -1)

        getAnalisisData()

        binding.buttonEditAnalisisData.setOnClickListener {
            updateAnalisisData()
        }
    }

    private fun updateAnalisisData() {
        TODO("Not yet implemented")
    }

    private fun getAnalisisData() {
        if(idAnalisisData == -1) {
            Snackbar.make(binding.root, "Terjadi kesalahan saat mengakses analisis data", Snackbar.LENGTH_LONG).show()
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 500)
        }

        analisisDataViewModel.getDetailAnalisisData(bearerToken, idAnalisisData)
        displayAnalisisData()
    }

    private fun displayAnalisisData() {
        binding.analisisDataProgressbar.visibility = android.view.View.VISIBLE

        analisisDataViewModel.analisisDataApiResponse.observe(this, Observer {
            if(it.code == 200 && it.analisisData != null) {
                binding.editTextJudul.setText(it.analisisData.judul)
                binding.editTextDeskripsi.setText(it.analisisData.deskripsi)

                val fileUrl = "${BuildConfig.BASE_URL}${it.analisisData.file}"
                val fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1)

                /*val fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1)
                val intent = Intent(Intent.ACTION_VIEW)
                        .setDataAndType(Uri.parse(fileUrl), "application/$fileExtension")
                        .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)*/

                binding.buttonAccessFile.text = fileName
                binding.buttonAccessFile.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                        .setDataAndType(Uri.parse(fileUrl), "application/*")
                        .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    startActivity(intent)
                }
            }
        })

        binding.analisisDataProgressbar.visibility = android.view.View.GONE
    }
}