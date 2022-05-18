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
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.BuildConfig
import id.del.ac.delstat.R
import id.del.ac.delstat.data.model.analisisdata.AnalisisData
import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.ActivityDetailAnalisisDataBinding
import id.del.ac.delstat.presentation.analisisdata.viewmodel.AnalisisDataViewModel
import id.del.ac.delstat.presentation.analisisdata.viewmodel.AnalisisDataViewModelFactory
import id.del.ac.delstat.util.Helper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class DetailAnalisisDataActivity : AppCompatActivity() {

    companion object {
        const val DATA_ANALISIS_DATA = "data_analisis_data"
    }

    private lateinit var binding: ActivityDetailAnalisisDataBinding

    @Inject
    lateinit var analisisDataViewModelFactory: AnalisisDataViewModelFactory
    private lateinit var analisisDataViewModel: AnalisisDataViewModel

    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var bearerToken: String
    private lateinit var role: String
    private var idAnalisisData: Int = -1

    private var isEditMode: Boolean = false
    private lateinit var editItem: MenuItem
    private var isCancelMode: Boolean = false
    private lateinit var cancelItem: MenuItem

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
        editItem = menu?.findItem(R.id.editAnalisisData)!!
        cancelItem = menu.findItem(R.id.cancelAnalisisData)!!

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.editAnalisisData -> {
                toggleEditMode()
                return true
            }
            R.id.cancelAnalisisData -> {
                toggleCancelMode()
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
            role = userPreferences.getUserRole.first()!!
        }

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail Analisis Data"

        idAnalisisData = intent.getIntExtra(DATA_ANALISIS_DATA, -1)

        getAnalisisData()

        analisisDataViewModel.analisisDataApiResponse.observe(this, Observer {
            if (it.code == 204 && it.message != null && role == User.ROLE_SISWA) {
                Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()
                toggleEditMode()
            }
            if(it.code == 204 && it.message != null && role == User.ROLE_DOSEN || role == User.ROLE_ADMIN) {
                Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_SHORT).show()
                getAnalisisData()
            }
        })

        inputValidations()
        binding.buttonEditAnalisisData.setOnClickListener {
            updateAnalisisData()
        }
    }

    private fun getAnalisisData() {
        if (idAnalisisData == -1) {
            Snackbar.make(
                binding.root,
                "Terjadi kesalahan saat mengakses analisis data",
                Snackbar.LENGTH_LONG
            ).show()
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
            if (it.code == 200 && it.analisisData != null && it.analisisData.user != null) {
                binding.editTextJudul.setText(it.analisisData.judul)
                binding.editTextDeskripsi.setText(it.analisisData.deskripsi)

                binding.textViewNamaRequesterAnalisisData.text = it.analisisData.user.nama
                binding.textViewNoHpRequesterAnalisisData.text = it.analisisData.user.noHp

                displayStatusAnalisisData(it.analisisData.status!!)

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

    private fun displayStatusAnalisisData(status: String) {
        binding.statusAnalisisData.text = status
        editItem.isVisible = false
        cancelItem.isVisible = false
        when (status) {
            AnalisisData.STATUS_DIPESAN -> {
                editItem.isVisible = true
                cancelItem.isVisible = true
                binding.statusAnalisisData.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.blue_primary
                    )
                )
            }
            AnalisisData.STATUS_DIBATALKAN -> {
                binding.statusAnalisisData.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.grey_input
                    )
                )
            }
            AnalisisData.STATUS_DIPROSES -> {
                binding.statusAnalisisData.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.yellow_dark
                    )
                )
            }
            AnalisisData.STATUS_DITOLAK -> {
                binding.statusAnalisisData.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.red_dark
                    )
                )
            }
            AnalisisData.STATUS_SELESAI -> {
                binding.statusAnalisisData.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.green_dark
                    )
                )
            }
        }

        if (role == User.ROLE_ADMIN || role == User.ROLE_DOSEN) {
            editItem.isVisible = false
            cancelItem.isVisible = false
            prepareUIAdminDosen()
        }
    }

    private fun prepareUIAdminDosen() {
        binding.statusAnalisisData.isEnabled = true
        binding.statusAnalisisData.setOnClickListener {

            val statusList = arrayOf(
                AnalisisData.STATUS_DIPROSES,
                AnalisisData.STATUS_DITOLAK,
                AnalisisData.STATUS_SELESAI
            )

            val selectedStatus = binding.statusAnalisisData.text.toString()
            var selectedStatusIndex = statusList.indexOf(selectedStatus)

            /*val builder = AlertDialog.Builder(this)
            builder.setTitle("Ubah Status Analisis Data")
                .setItems(statusList) { dialog, which ->
                    // The 'which' argument contains the index position
                    // of the selected item
                    selectedStatusIndex = which
                    binding.statusAnalisisData.text = statusList[selectedStatusIndex]
                    Log.d("Selected", statusList[which])
                }
                .create()
                .show()*/

            MaterialAlertDialogBuilder(this)
                .setTitle("Ubah Status Request Analisis Data")
                .setSingleChoiceItems(statusList, selectedStatusIndex) { dialog, which ->
                    selectedStatusIndex = which
                    Log.d("status", statusList[which])
                }
                .setPositiveButton("Ubah") { dialog, which ->
                    val status = statusList[selectedStatusIndex]
                    Log.d("status", status)
                    dialog.dismiss()
                    updateStatusAnalisisData(status)
                }
                .setNegativeButton("Batal") { dialog, which ->
                    dialog.dismiss()
                }
                .show()

            Log.d("MyTag", statusList[0])
        }
    }

    private fun toggleEditMode() {
        if (!isEditMode) {
            isEditMode = true
            binding.editTextDeskripsi.isEnabled = true
            binding.editTextDeskripsi.requestFocus()
            val imm: InputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.editTextDeskripsi, InputMethodManager.SHOW_IMPLICIT)

            cancelItem.isVisible = false

            binding.buttonEditAnalisisData.visibility = android.view.View.VISIBLE
            binding.statusAnalisisData.visibility = android.view.View.GONE
            return
        }
        // else
        isEditMode = false
        binding.editTextDeskripsi.isEnabled = false
        cancelItem.isVisible = true

        binding.buttonEditAnalisisData.visibility = android.view.View.GONE
        binding.statusAnalisisData.visibility = android.view.View.VISIBLE
        displayAnalisisData()
    }

    private fun toggleCancelMode() {
        if (!isCancelMode) {
            isCancelMode = true
            editItem.isVisible = false
            MaterialAlertDialogBuilder(this)
                .setTitle("Batalkan Request Analisis Data?")
                .setMessage("Anda akan membatalkan request analisis data dengan judul ${binding.editTextJudul.text}.")
                .setNegativeButton("Tidak") { dialog, which ->
                    toggleCancelMode()
                    dialog.dismiss()
                }
                .setPositiveButton("Ya") { dialog, which ->
                    cancelAnalisisData()
                    toggleCancelMode()
                    dialog.dismiss()
                }
                .show()
            return
        }
        // else
        isCancelMode = false
        editItem.isVisible = true
        getAnalisisData()
    }

    private fun inputValidations() {
        binding.editTextDeskripsi.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                binding.textInputLayout2.error = "Field deskripsi wajib diisi"
            } else {
                binding.textInputLayout2.error = null
            }
        }
    }

    private fun checkInput(): Boolean {
        val validInput = binding.textInputLayout1.error == null
        return validInput
    }

    private fun updateAnalisisData() {
        /* Hiding the keyboard */
        val view = this.currentFocus
        if (view != null) {
            /*hideKeyboardFrom(applicationContext, binding.root)*/
            Helper.hideKeyboardFrom(applicationContext, view)
        }
        /* End of Hiding the keyboard */

        if (checkInput()) {
            val deskripsi = binding.editTextDeskripsi.text.toString()

            analisisDataViewModel.updateAnalisisData(bearerToken, idAnalisisData, deskripsi)
        }
    }

    private fun updateStatusAnalisisData(status: String) {
        analisisDataViewModel.updateStatusAnalisisData(bearerToken, idAnalisisData, status)
        displayAnalisisData()
    }

    private fun cancelAnalisisData() {
        analisisDataViewModel.cancelAnalisisData(bearerToken, idAnalisisData)
    }
}