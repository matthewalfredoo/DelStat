package id.del.ac.delstat.presentation.analisisdata.activity

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
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.del.d3ti20.util.RealPathUtil
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.R
import id.del.ac.delstat.data.model.analisisdata.AnalisisData
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.ActivityCreateAnalisisDataBinding
import id.del.ac.delstat.presentation.analisisdata.viewmodel.AnalisisDataViewModel
import id.del.ac.delstat.presentation.analisisdata.viewmodel.AnalisisDataViewModelFactory
import id.del.ac.delstat.util.Helper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class CreateAnalisisDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAnalisisDataBinding

    @Inject
    lateinit var analisisDataViewModelFactory: AnalisisDataViewModelFactory
    private lateinit var analisisDataViewModel: AnalisisDataViewModel

    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var bearerToken: String

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
                /*Snackbar.make(binding.root, "File not selected", Snackbar.LENGTH_SHORT).show()*/
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_analisis_data)

        binding = ActivityCreateAnalisisDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        analisisDataViewModel = ViewModelProvider(this, analisisDataViewModelFactory)
            .get(AnalisisDataViewModel::class.java)

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
        supportActionBar?.title = "Request Analisis Data"

        analisisDataViewModel.analisisDataApiResponse.observe(this, Observer {
            /*Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_SHORT).show()*/
            Helper.createSnackbar(binding.root, it.message!!).show()
            if (it.code == 200) {
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 500)
            }
        })

        binding.inputFile.setOnClickListener {
            selectFile()
        }

        inputValidations()

        binding.buttonCreateAnalisisData.setOnClickListener {
            createAnalisisData()
        }
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
        analisisDataViewModel.analisisDataApiResponse.observe(this, Observer {
            if (it.code != 200 && it.errors != null){
                if(it.errors.judul != null){
                    binding.textInputLayout1.error = it.errors.judul.get(0)
                }
                if(it.errors.deskripsi != null){
                    binding.textInputLayout2.error = it.errors.deskripsi.get(0)
                }
                if(it.errors.file != null){
                    binding.inputLayout3.error = it.errors.file.get(0)
                }
            }
        })

        binding.editTextJudul.doOnTextChanged { text, start, before, count ->
            if(text.isNullOrEmpty()) {
                binding.textInputLayout1.error = "Field judul wajib diisi"
            } else {
                binding.textInputLayout1.error = null
            }
        }

        binding.editTextDeskripsi.doOnTextChanged { text, start, before, count ->
            if(text.isNullOrEmpty()) {
                binding.textInputLayout2.error = "Field deskripsi wajib diisi"
            } else {
                binding.textInputLayout2.error = null
            }
        }

        binding.inputFile.doOnTextChanged { text, start, before, count ->
            if(text.isNullOrEmpty()) {
                binding.inputLayout3.error = "Field file wajib diisi"
            } else {
                binding.inputLayout3.error = null
            }
        }
    }

    private fun checkInput(): Boolean {
        val validInput = binding.textInputLayout1.error == null &&
                binding.textInputLayout2.error == null &&
                binding.inputLayout3.error == null

        return validInput
    }

    private fun createAnalisisData() {
        /* Hiding the keyboard */
        val view = this.currentFocus
        if(view != null) {
            /*hideKeyboardFrom(applicationContext, binding.root)*/
            Helper.hideKeyboardFrom(applicationContext, view)
        }
        /* End of Hiding the keyboard */

        if(checkInput()) {
            val judul = binding.editTextJudul.text.toString()
            val deskripsi = binding.editTextDeskripsi.text.toString()
            val file = selectedFile

            analisisDataViewModel.storeAnalisisData(bearerToken, judul, deskripsi, file)
        }
    }
}