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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.del.d3ti20.util.RealPathUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.BuildConfig
import id.del.ac.delstat.R
import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.ActivityDetailLiteraturBinding
import id.del.ac.delstat.presentation.literatur.viewmodel.LiteraturViewModel
import id.del.ac.delstat.presentation.literatur.viewmodel.LiteraturViewModelFactory
import id.del.ac.delstat.util.DateUtil
import id.del.ac.delstat.util.Helper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class DetailLiteraturActivity : AppCompatActivity() {
    companion object {
        const val ID_LITERATUR = "id_literatur"
    }

    private lateinit var binding: ActivityDetailLiteraturBinding

    @Inject
    lateinit var literaturViewModelFactory: LiteraturViewModelFactory
    private lateinit var literaturViewModel: LiteraturViewModel

    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var bearerToken: String
    private lateinit var role: String
    private var idLiteratur: Int = -1

    private lateinit var editItem: MenuItem
    private lateinit var deleteItem: MenuItem
    private var isEditMode: Boolean = false
    private var isDeleteMode: Boolean = false

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
        binding = ActivityDetailLiteraturBinding.inflate(layoutInflater)
        setContentView(binding.root)

        literaturViewModel = ViewModelProvider(this, literaturViewModelFactory)
            .get(LiteraturViewModel::class.java)

        prepareUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_action_bar_detailliteraturactivity, menu)
        editItem = menu?.findItem(R.id.editLiteratur)!!
        deleteItem = menu.findItem(R.id.deleteLiteratur)!!

        editItem.isVisible = false
        deleteItem.isVisible = false

        if(role == User.ROLE_DOSEN || role == User.ROLE_ADMIN) {
            editItem.isVisible = true
            deleteItem.isVisible = true
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.editLiteratur -> {
                toggleEditMode()
                return true
            }
            R.id.deleteLiteratur -> {
                toggleDeleteMode()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun toggleEditMode() {
        if (!isEditMode) {
            isEditMode = true
            binding.editTextJudul.isEnabled = true
            binding.editTextPenulis.isEnabled = true
            binding.editTextTahunTerbit.isEnabled = true
            binding.editTextTags.isEnabled = true

            deleteItem.isVisible = false

            binding.textInputLayout5.visibility = View.VISIBLE
            binding.buttonEditLiteratur.visibility = View.VISIBLE
            return
        }
        // else
        isEditMode = false
        binding.editTextJudul.isEnabled = false
        binding.editTextPenulis.isEnabled = false
        binding.editTextTahunTerbit.isEnabled = false
        binding.editTextTags.isEnabled = false
        binding.inputFile.isEnabled = false

        deleteItem.isVisible = true

        binding.textInputLayout5.visibility = View.GONE
        binding.buttonEditLiteratur.visibility = View.GONE
        getLiteratur()
    }

    private fun toggleDeleteMode() {
        if (!isDeleteMode) {
            isDeleteMode = true
            editItem.isVisible = false
            MaterialAlertDialogBuilder(this)
                .setTitle("Hapus literatur?")
                .setMessage(
                    "Literatur dengan judul \"${binding.editTextJudul.text}\" akan dihapus dan Anda tidak akan bisa mengembalikannya.\n" +
                            "Apakah Anda yakin?"
                )
                .setNegativeButton("Tidak") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("Ya") { dialog, which ->
                    deleteLiteratur()
                    dialog.dismiss()
                }
                // this is to handle when dialog is dismissed either using negative button or clicked on area outside of the dialog
                .setOnDismissListener {
                     toggleDeleteMode()
                }
                .show()
            return
        }
        // else
        isDeleteMode = false
        editItem.isVisible = true
        getLiteratur()
    }

    private fun prepareUI() {
        runBlocking {
            bearerToken = userPreferences.getUserToken.first()!!
            role = userPreferences.getUserRole.first()!!
        }

        // setting up the action bar
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail Literatur"

        // getting the idMateri from extra sent by the previous activity
        idLiteratur = intent.getIntExtra(ID_LITERATUR, -1)

        // getting and displaying literatur data
        getLiteratur()

        // ALl about inputs for update are here
        inputValidations()
        binding.inputFile.setOnClickListener {
            selectFile()
        }
        binding.buttonEditLiteratur.setOnClickListener {
            updateLiteratur()
        }
        literaturViewModel.literaturApiResponse.observe(this, Observer {
            if (it.code == 204 && it.message != null) {
                Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()
                if(isEditMode) {
                    toggleEditMode()
                }
            }
        })
    }

    /* Functions related to update literatur */
    private fun inputValidations() {
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
            binding.textInputLayout5.error = null
        }

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
    }

    private fun checkInput(): Boolean {
        val validInput = binding.textInputLayout1.error == null &&
                binding.textInputLayout2.error == null &&
                binding.textInputLayout3.error == null &&
                binding.textInputLayout4.error == null &&
                binding.textInputLayout5.error == null

        return validInput
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
        selectedFile = File(selectedFilePath!!)
        binding.inputFile.setText(selectedFile!!.name)
    }

    private fun updateLiteratur() {
        if (checkInput()) {
            bearerToken = "Bearer $bearerToken"
            val judul = binding.editTextJudul.text.toString()
            val penulis = binding.editTextPenulis.text.toString()
            val tahunTerbit = binding.editTextTahunTerbit.text.toString()
            val tag = binding.editTextTags.text.toString()
            val file = selectedFile

            literaturViewModel.updateLiteratur(
                bearerToken,
                idLiteratur,
                judul,
                penulis,
                tahunTerbit.toInt(),
                tag,
                file
            )

        }
    }
    /* End of functions related to update literatur */

    private fun deleteLiteratur() {
        bearerToken = "Bearer $bearerToken"
        literaturViewModel.deleteLiteratur(bearerToken, idLiteratur)
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 1000)
    }

    /* Functions related to get detail literatur and display it on the activity */
    private fun getLiteratur() {
        if (idLiteratur == -1) {
            Snackbar.make(
                binding.root,
                "Terjadi kesalahan saat mengakses literatur",
                Snackbar.LENGTH_LONG
            ).show()
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 500)
        }

        literaturViewModel.getDetailLieratur(idLiteratur)
        displayLiteratur()
    }

    private fun displayLiteratur() {
        binding.literaturProgressbar.visibility = View.VISIBLE

        literaturViewModel.literaturApiResponse.observe(this, Observer {
            if (it.code == 200 && it.literatur != null && it.literatur.user != null) {
                binding.editTextJudul.setText(it.literatur.judul)
                binding.editTextPenulis.setText(it.literatur.penulis)
                binding.editTextTahunTerbit.setText(it.literatur.tahunTerbit.toString())
                binding.editTextTags.setText(it.literatur.tag)

                val fileUrl = "${BuildConfig.BASE_URL}${it.literatur.file}"
                val fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1)
                binding.buttonAccessFile.text = fileName
                binding.buttonAccessFile.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                        .setDataAndType(Uri.parse(fileUrl), "application/*")
                        .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    startActivity(intent)
                }

                binding.textViewNamaUserLiteratur.text = it.literatur.user.nama

                if (it.literatur.createdAt!!.toLong() == it.literatur.updatedAt!!.toLong()) {
                    binding.titleCreatorEditorLiteratur.text =
                        resources.getString(R.string.created_at_literatur)
                    binding.textViewTanggalLiteratur.text =
                        DateUtil.getDateTime(it.literatur.createdAt)
                } else {
                    binding.titleCreatorEditorLiteratur.text =
                        resources.getString(R.string.updated_at_literatur)
                    binding.textViewTanggalLiteratur.text =
                        DateUtil.getDateTime(it.literatur.updatedAt)
                }
            }
        })

        binding.literaturProgressbar.visibility = View.GONE
    }
    /* End of functions related to get detail literatur and display it on the activity */
}