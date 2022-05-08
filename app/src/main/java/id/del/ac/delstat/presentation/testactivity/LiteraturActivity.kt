package id.del.ac.delstat.presentation.testactivity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.del.d3ti20.util.RealPathUtil
import com.google.android.material.snackbar.Snackbar
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.ActivityLiteraturBinding
import id.del.ac.delstat.presentation.di.Injector
import id.del.ac.delstat.presentation.literatur.viewmodel.LiteraturViewModel
import id.del.ac.delstat.presentation.literatur.viewmodel.LiteraturViewModelFactory
import id.del.ac.delstat.util.Helper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File
import javax.inject.Inject

class LiteraturActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLiteraturBinding

    @Inject
    lateinit var literaturViewModelFactory: LiteraturViewModelFactory
    private lateinit var literaturViewModel: LiteraturViewModel

    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var bearerToken: String

    private lateinit var judul: String
    private lateinit var penulis: String
    private var tahunTerbit: Int = 0
    private lateinit var tag: String

    private var idLiteraturEdit: Int = 0
    private var idLiteraturDelete: Int = 0

    private var intentData: Intent? = null
    private var selectedFileUri: Uri? = null
    private var selectedFilePath: String? = null
    private var selectedFile: File? = null

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
            if(result.resultCode == RESULT_OK) {
                Log.d("MyTag", "File selected")
                intentData = result.data
                if(intentData != null) {
                    selectedFileUri = intentData!!.data
                    Log.d("MyTag", "File uri: $selectedFileUri")
                    selectedFileResult()
                }
            }
            else {
                Snackbar.make(binding.root, "File not selected", Snackbar.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiteraturBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as Injector).createLiteraturSubComponent().inject(this)

        literaturViewModel = ViewModelProvider(this, literaturViewModelFactory)
            .get(LiteraturViewModel::class.java)

        prepareUI()
    }

    private fun prepareUI() {
        runBlocking {
            bearerToken = userPreferences.getUserToken.first()!!
            bearerToken = "Bearer $bearerToken"
        }

        binding.btnGetLiteratur.setOnClickListener {
            getLiteratur()
        }

        binding.btnGetLiteraturDetail.setOnClickListener {
            getLiteraturDetail()
        }

        binding.btnAddLiteratur.setOnClickListener {
            addLiteratur()
        }

        binding.fileLiteratur.setOnClickListener {
            selectFile()
        }

        binding.btnEditLiteratur.setOnClickListener {
            editLiteratur()
        }

        binding.fileEditLiteratur.setOnClickListener {
            selectFile()
        }

        binding.btnDeleteLiteratur.setOnClickListener {
            deleteLiteratur()
        }

        literaturViewModel.literaturApiResponse.observe(this, Observer {
            Log.d("MyTag", it.toString())
        })
    }

    private fun deleteLiteratur() {
        idLiteraturDelete = binding.etIdDeleteLiteratur.text.toString().toInt()
        literaturViewModel.deleteLiteratur(bearerToken, idLiteraturDelete)
    }

    private fun editLiteratur() {
        judul = binding.etJudulEditLiteratur.text.toString()
        penulis = binding.etPenulisEditLiteratur.text.toString()
        tahunTerbit = binding.etTahunTerbitEditLiteratur.text.toString().toInt()
        tag = binding.etTagEditLiteratur.text.toString()
        idLiteraturEdit = binding.etIdEditLiteratur.text.toString().toInt()

        literaturViewModel.updateLiteratur(bearerToken, idLiteraturEdit, judul, penulis, tahunTerbit, tag, selectedFile)
    }

    private fun selectFile() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.d("MyTag", "No permission needed, and do something")
        }
        if(hasPermissions(this, Helper.PERMISSIONS)) {
            Log.d("MyTag", "Permission granted, and do something")
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                .setType("application/pdf")
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
        binding.fileLiteratur.text = selectedFile!!.name
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    private fun addLiteratur() {
        judul = binding.etJudulLiteratur.text.toString()
        penulis = binding.etPenulisLiteratur.text.toString()
        tahunTerbit = binding.etTahunTerbitLiteratur.text.toString().toInt()
        tag = binding.etTagLiteratur.text.toString()

        literaturViewModel.storeLiteratur(bearerToken, judul, penulis, tahunTerbit, tag, selectedFile)
    }

    private fun getLiteratur() {
        literaturViewModel.getLiteratur()
    }

    private fun getLiteraturDetail() {
        val id = binding.etIdLiteratur.text.toString().toInt()
        literaturViewModel.getDetailLieratur(id)
    }

}