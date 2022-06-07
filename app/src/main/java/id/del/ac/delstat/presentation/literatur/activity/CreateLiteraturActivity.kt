package id.del.ac.delstat.presentation.literatur.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.del.d3ti20.util.RealPathUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.data.model.materi.Materi
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.ActivityCreateLiteraturBinding
import id.del.ac.delstat.presentation.literatur.viewmodel.LiteraturViewModel
import id.del.ac.delstat.presentation.literatur.viewmodel.LiteraturViewModelFactory
import id.del.ac.delstat.util.Helper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class CreateLiteraturActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateLiteraturBinding

    @Inject
    lateinit var literaturViewModelFactory: LiteraturViewModelFactory
    private lateinit var literaturViewModel: LiteraturViewModel

    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var bearerToken: String

    private var intentData: Intent? = null
    private var selectedFileUri: Uri? = null
    private var selectedFilePath: String? = null
    private var selectedFile: File? = null

    private lateinit var tagsList: Array<String>
    private var selectedTagIndex = -1

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                Log.d("MyTag", "Access to external storage granted")
                selectFile()
            }
        }

    private val selectFileResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d("MyTag", "File selected")
                intentData = result.data
                if (intentData != null) {
                    selectedFileUri = intentData!!.data
                    Log.d("MyTag", "File uri: $selectedFileUri")
                    selectedFileResult()
                }
            } else {
                Snackbar.make(binding.root, "File not selected", Snackbar.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateLiteraturBinding.inflate(layoutInflater)
        setContentView(binding.root)

        literaturViewModel = ViewModelProvider(this, literaturViewModelFactory)
            .get(LiteraturViewModel::class.java)

        prepareUI()
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
        runBlocking {
            bearerToken = "Bearer ${userPreferences.getUserToken.first()!!}"
        }

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Tambah Literatur"

        literaturViewModel.literaturApiResponse.observe(this, Observer {
            Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()
            if (it.code == 201) {
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 500)
            }
        })

        // Related to inputs
        binding.inputFile.setOnClickListener {
            selectFile()
        }

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

        binding.editTextTags.setOnClickListener {
            tagDialog()
        }

        inputValidations()

        binding.buttonCreateLiteratur.setOnClickListener {
            addLiteratur()
        }
    }

    private fun tagDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Pilih Tag")
            .setSingleChoiceItems(tagsList, selectedTagIndex) { dialog, which ->
                selectedTagIndex = which
            }
            .setPositiveButton("Pilih") { dialog, which ->
                binding.editTextTags.setText(tagsList[selectedTagIndex])
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, which ->
                dialog.dismiss()
            }
            .setOnDismissListener {
                if(binding.editTextTags.text != null){
                    selectedTagIndex = tagsList.indexOf(binding.editTextTags.text.toString())
                }
                else {
                    selectedTagIndex = -1
                }
            }
            .show()
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    private fun selectFile() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.d("MyTag", "No permission needed, and do something")
        }
        if (hasPermissions(this, Helper.PERMISSIONS)) {
            Log.d("MyTag", "Permission already granted")
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                .setType("*/*")
                .addCategory(Intent.CATEGORY_OPENABLE)
            selectFileResultLauncher.launch(intent)
        } else {
            requestPermissionLauncher.launch(Helper.PERMISSIONS)
        }
    }

    private fun selectedFileResult() {
        selectedFilePath = RealPathUtil.copyFileToInternal(this, selectedFileUri!!)
        Log.d("MyTag", "Selected file path: $selectedFilePath")
        selectedFile = File(selectedFilePath!!)
        binding.inputFile.setText(selectedFile!!.name)
    }

    private fun inputValidations() {
        literaturViewModel.literaturApiResponse.observe(this, Observer {
            if (it.code == 400 && it.errors != null) {
                if (it.errors.judul != null) {
                    binding.textInputLayout1.error = it.errors.judul[0]
                }
                if (it.errors.penulis != null) {
                    binding.textInputLayout2.error = it.errors.penulis[0]
                }
                if (it.errors.tahunTerbit != null) {
                    binding.textInputLayout3.error = it.errors.tahunTerbit[0]
                }
                if (it.errors.tag != null) {
                    binding.textInputLayout4.error = it.errors.tag[0]
                }
                if (it.errors.file != null) {
                    binding.textInputLayout5.error = it.errors.file[0]
                }
            }
        })

        binding.editTextJudul.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                binding.textInputLayout1.error = "Field judul wajib diisi"
            } else {
                binding.textInputLayout1.error = null
            }
        }

        binding.editTextPenulis.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                binding.textInputLayout2.error = "Field penulis wajib diisi"
            } else {
                binding.textInputLayout2.error = null
            }
        }

        binding.editTextTahunTerbit.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                binding.textInputLayout3.error = "Field tahun terbit wajib diisi"
            } else {
                binding.textInputLayout3.error = null
            }
        }

        binding.editTextTags.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                binding.textInputLayout4.error = "Field tags wajib diisi"
            } else {
                binding.textInputLayout4.error = null
            }
        }

        binding.inputFile.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                binding.textInputLayout5.error = null
            }
        }
    }

    private fun checkInput(): Boolean {
        val validInput = binding.textInputLayout1.error == null &&
                binding.textInputLayout2.error == null &&
                binding.textInputLayout3.error == null &&
                binding.textInputLayout4.error == null &&
                binding.textInputLayout5.error == null

        return validInput
    }

    private fun addLiteratur() {
        if (checkInput()) {
            val judul = binding.editTextJudul.text.toString()
            val penulis = binding.editTextPenulis.text.toString()
            var tahunTerbit = -1
            if(binding.editTextTahunTerbit.text.toString() != "") {
                tahunTerbit = binding.editTextTahunTerbit.text.toString().toInt()
            }
            val tags = binding.editTextTags.text.toString()
            val file = selectedFile

            literaturViewModel.storeLiteratur(
                bearerToken,
                judul,
                penulis,
                tahunTerbit,
                tags,
                file
            )
        }
    }
}