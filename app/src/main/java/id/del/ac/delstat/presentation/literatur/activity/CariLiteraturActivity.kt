package id.del.ac.delstat.presentation.literatur.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.R
import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.model.literatur.Literatur
import id.del.ac.delstat.data.model.materi.Materi
import id.del.ac.delstat.databinding.ActivityCariLiteraturBinding
import id.del.ac.delstat.presentation.literatur.adapter.LiteraturAdapter
import id.del.ac.delstat.presentation.literatur.viewmodel.LiteraturViewModel
import id.del.ac.delstat.presentation.literatur.viewmodel.LiteraturViewModelFactory
import id.del.ac.delstat.util.Helper
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class CariLiteraturActivity : AppCompatActivity() {
    lateinit var binding: ActivityCariLiteraturBinding

    @Inject
    lateinit var literaturViewModelFactory: LiteraturViewModelFactory
    private lateinit var literaturViewModel: LiteraturViewModel
    private lateinit var literaturAdapter: LiteraturAdapter

    private var timer: Timer = Timer()
    private val delay: Long = 250 // 0.25 seconds

    private lateinit var tagsList: Array<String>
    private var selectedTagIndex = -1
    private var selectedTag: String? = null

    private var judul: String? = null

    @Inject
    lateinit var delStatApiService: DelStatApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCariLiteraturBinding.inflate(layoutInflater)
        setContentView(binding.root)

        literaturViewModel = ViewModelProvider(this, literaturViewModelFactory)
            .get(LiteraturViewModel::class.java)

        prepareUI()
        initRecyclerView()
        initSearchBar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun prepareUI() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Cari Literatur"

        focusOnSearchEditText()
        observeLoadingStatus()

        tagsList = arrayOf(
            Materi.TAG_MATERI_1_KONSEP_PELUANG,
            Materi.TAG_MATERI_2_VARIABEL_ACAK,
            Materi.TAG_MATERI_3_DISTRIBUSI_PROBABILITAS_DISKRIT,
            Materi.TAG_MATERI_4_DISTRIBUSI_PROBABILITAS_KONTINU,
            Materi.TAG_MATERI_5_PENGANTAR_STATISTIK_ANALISIS_DATA,
            Materi.TAG_MATERI_6_TEKNIK_SAMPLING,
            Materi.TAG_MATERI_7_ANOVA,
            Materi.TAG_MATERI_8_KONSEP_ESTIMASI,
            Materi.TAG_MATERI_9_PENGUJIAN_HIPOTESIS,
            Materi.TAG_MATERI_10_REGRESI_KORELASI
        )

        binding.buttonFilterLiteratur.setOnClickListener {
            tagDialog()
        }
    }

    private fun observeLoadingStatus() {
        literaturViewModel.loadingProgressBar.observe(this, Observer {
            if (it) {
                binding.literaturProgressbar.visibility = View.VISIBLE
                binding.recyclerViewLiteratur.visibility = View.GONE
            } else {
                binding.literaturProgressbar.visibility = View.GONE
                binding.recyclerViewLiteratur.visibility = View.VISIBLE
            }
        })
    }

    private fun focusOnSearchEditText() {
        binding.editTextSearchJudul.requestFocus()
        val imm: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.editTextSearchJudul, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun initRecyclerView() {
        literaturAdapter = LiteraturAdapter() { literatur: Literatur ->
            onClickLiteratur(literatur)
        }
        binding.recyclerViewLiteratur.adapter = literaturAdapter
        binding.recyclerViewLiteratur.layoutManager = LinearLayoutManager(applicationContext)
    }

    private fun onClickLiteratur(literatur: Literatur) {
        startActivity(
            Intent(this, DetailLiteraturActivity::class.java)
                .putExtra(DetailLiteraturActivity.ID_LITERATUR, literatur.id)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }

    private fun getLiteratur(judul: String? = null, tag: String? = null) {
        literaturViewModel.getLiteratur(judul, tag)
    }

    private fun displayLiteratur() {
        literaturViewModel.literaturApiResponse.observe(this, Observer {
            if (it.code == 200 && it.literaturList != null) {
                literaturAdapter.setList(it.literaturList)
                literaturAdapter.notifyDataSetChanged()
            } else {
                Helper.createSnackbar(binding.root, it.message!!).show()
            }
        })
    }

    private fun initSearchBar() {
        binding.editTextSearchJudul.doOnTextChanged { text, start, before, count ->
            timer.cancel()
        }
        binding.editTextSearchJudul.doAfterTextChanged { text ->
            if (text.isNullOrEmpty() || text.length < 3) {
                judul = null
            } else {
                judul = text.toString()
            }
            proceedSearching()
        }
    }

    private fun proceedSearching() {
        if (judul == null && selectedTag == null) {
            literaturAdapter.emptyList()
            literaturAdapter.notifyDataSetChanged()
            binding.recyclerViewLiteratur.visibility = View.GONE
            return
        }

        timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    getLiteratur(judul, selectedTag)
                }
            },
            delay
        )
        displayLiteratur()
        if (binding.recyclerViewLiteratur.visibility == View.GONE) {
            binding.recyclerViewLiteratur.visibility = View.VISIBLE
        }
    }

    private fun tagDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Pilih Tag")
            .setSingleChoiceItems(tagsList, selectedTagIndex) { dialog, which ->
                selectedTagIndex = which
            }
            .setPositiveButton("Pilih") { dialog, which ->
                selectedTag = tagsList[selectedTagIndex]
                proceedSearching()
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, which ->
                dialog.dismiss()
            }
            .setNeutralButton("Hapus") { dialog, which ->
                selectedTag = null
                proceedSearching()
                dialog.dismiss()
            }
            .setOnDismissListener {
                if (selectedTag != null) {
                    selectedTagIndex = tagsList.indexOf(selectedTag)
                } else {
                    selectedTagIndex = -1
                }
            }
            .show()
    }

}