package id.del.ac.delstat.presentation.kuis.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.BuildConfig
import id.del.ac.delstat.R
import id.del.ac.delstat.data.model.kuis.HasilKuis
import id.del.ac.delstat.data.model.kuis.Kuis
import id.del.ac.delstat.data.model.kuis.KumpulanKuis
import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.ActivityHasilKuisBinding
import id.del.ac.delstat.presentation.activity.LoginActivity
import id.del.ac.delstat.presentation.kuis.adapter.HasilKuisAdapter
import id.del.ac.delstat.presentation.kuis.viewmodel.HasilKuisViewModel
import id.del.ac.delstat.presentation.kuis.viewmodel.HasilKuisViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class HasilKuisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHasilKuisBinding

    @Inject
    lateinit var hasilKuisViewModelFactory: HasilKuisViewModelFactory
    private lateinit var hasilKuisViewModel: HasilKuisViewModel

    private lateinit var listKuis: ArrayList<Kuis>
    private lateinit var itemsDropdown: ArrayList<String>
    private lateinit var dropdownAdapter: ArrayAdapter<String>
    private var selectedIndexKuis: Int = HasilKuis.HASIL_KUIS_EMPTY
    private var idKuis: Int = HasilKuis.HASIL_KUIS_EMPTY

    private val linkExportHasilKuis: String = BuildConfig.BASE_URL + "hasilkuis/export"
    private lateinit var exportItem: MenuItem

    @Inject
    lateinit var userPreferences: UserPreferences
    lateinit var bearerToken: String
    lateinit var role: String

    lateinit var hasilKuisAdapter: HasilKuisAdapter

    companion object {
        const val EXTRA_ID_KUIS = "extra_id_kuis"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilKuisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hasilKuisViewModel = ViewModelProvider(this, hasilKuisViewModelFactory)
            .get(HasilKuisViewModel::class.java)

        prepareUI()
        initRecyclerView()
        prepareData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_actionbar_hasilkuiactivity, menu)
        exportItem = menu?.findItem(R.id.export_hasil_kuis_pdf)!!
        if(role != User.ROLE_DOSEN) {
            exportItem.isVisible = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.export_hasil_kuis_pdf -> {
                exportHasilKuisPdf()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun exportHasilKuisPdf() {
        val intent = Intent(Intent.ACTION_VIEW)
            .setDataAndType(Uri.parse(linkExportHasilKuis), "application/*")
            .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    private fun initRecyclerView() {
        hasilKuisAdapter = HasilKuisAdapter(role)
        binding.recyclerViewHasilKuis.adapter = hasilKuisAdapter
        binding.recyclerViewHasilKuis.layoutManager = LinearLayoutManager(this)
    }

    private fun getHasilKuis(bearerToken: String) {
        hasilKuisViewModel.getHasilKuis(bearerToken)
        displayHasilKuis()
    }

    private fun getHasilKuis(bearerToken: String, idKuis: Int) {
        hasilKuisViewModel.getDetailHasilKuis(bearerToken, idKuis)
        displayHasilKuis()
    }

    private fun displayHasilKuis() {
        binding.hasilKuisProgressbar.visibility = View.VISIBLE

        hasilKuisViewModel.hasilKuisApiResponse.observe(this, Observer {
            if(it.code == 200 && it.listHasilKuis != null) {
                hasilKuisAdapter.setList(it.listHasilKuis)
                hasilKuisAdapter.notifyDataSetChanged()
                binding.hasilKuisProgressbar.visibility = View.GONE
            }
            else {
                binding.hasilKuisProgressbar.visibility = View.GONE
                Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun prepareData() {
        idKuis = intent.getIntExtra(EXTRA_ID_KUIS, HasilKuis.HASIL_KUIS_EMPTY)
        if(idKuis != HasilKuis.HASIL_KUIS_EMPTY) {
            processIntent()
            return
        }
        getHasilKuis(bearerToken)
    }

    private fun prepareUI() {
        runBlocking {
            bearerToken = userPreferences.getUserToken.first()!!
            if(bearerToken.isEmpty()) {
                startActivity(
                    Intent(this@HasilKuisActivity, LoginActivity::class.java)
                        .putExtra(LoginActivity.LOGIN_MESSAGE, "Login untuk mengakses menu Hasil Kuis")
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                finish()
            }
            bearerToken = "Bearer $bearerToken"
            role = userPreferences.getUserRole.first()!!
        }

        setUpDropdown()

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Hasil Kuis"
    }

    private fun setUpDropdown() {
        itemsDropdown = ArrayList()

        listKuis = KumpulanKuis.kumpulanKuis
        itemsDropdown.add("Semua Kuis")
        for(kuis in listKuis) {
            itemsDropdown.add(kuis.nama!!)
        }

        dropdownAdapter = ArrayAdapter(this, R.layout.list_item_dropdown_hasilkuis, itemsDropdown)
        binding.autoCompleteTextViewPilihanKuis.setAdapter(dropdownAdapter)
        checkChangesOfDropdown()
    }

    private fun processIntent() {
        if(idKuis != HasilKuis.HASIL_KUIS_EMPTY) {
            Log.d("MyTag", "idKuis: $idKuis")
            val text = listKuis[idKuis - 1].nama
            binding.autoCompleteTextViewPilihanKuis.setText(text, false)
            binding.autoCompleteTextViewPilihanKuis.setSelection(
                idKuis
            )
            checkChangesOfDropdown()
            getHasilKuis(bearerToken, idKuis)
        }
    }

    private fun checkChangesOfDropdown() {
        binding.autoCompleteTextViewPilihanKuis.setOnItemClickListener { adapterView, view, i, l ->
            selectedIndexKuis = i
            if(i == 0) {
                getHasilKuis(bearerToken)
            }
            else {
                getHasilKuis(bearerToken, listKuis[selectedIndexKuis - 1].id!!)
            }
        }
    }
}