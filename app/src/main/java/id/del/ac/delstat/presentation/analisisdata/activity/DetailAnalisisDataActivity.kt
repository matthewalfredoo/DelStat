package id.del.ac.delstat.presentation.analisisdata.activity

import android.app.Activity
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
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.del.d3ti20.util.RealPathUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.BuildConfig
import id.del.ac.delstat.R
import id.del.ac.delstat.data.model.analisisdata.AnalisisData
import id.del.ac.delstat.data.model.chat.ChatApiResponse
import id.del.ac.delstat.data.model.chat.ChatRoom
import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.ActivityDetailAnalisisDataBinding
import id.del.ac.delstat.presentation.analisisdata.viewmodel.AnalisisDataViewModel
import id.del.ac.delstat.presentation.analisisdata.viewmodel.AnalisisDataViewModelFactory
import id.del.ac.delstat.presentation.chat.activity.DetailChatRoomActivity
import id.del.ac.delstat.presentation.chat.viewmodel.ChatViewModel
import id.del.ac.delstat.presentation.chat.viewmodel.ChatViewModelFactory
import id.del.ac.delstat.util.Helper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File
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
    lateinit var chatViewModelFactory: ChatViewModelFactory
    private lateinit var chatViewModel: ChatViewModel
    private var idChatRoom: Int = ChatRoom.CHAT_ROOM_EMPTY

    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var bearerToken: String
    private lateinit var role: String
    private var idAnalisisData: Int = -1

    private lateinit var userRequesterAnalisisData: User

    private var isEditMode: Boolean = false
    private lateinit var editItem: MenuItem
    private var isCancelMode: Boolean = false
    private lateinit var cancelItem: MenuItem

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
                /*Snackbar.make(binding.root, "File not selected", Snackbar.LENGTH_SHORT).show()*/
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAnalisisDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        analisisDataViewModel = ViewModelProvider(this, analisisDataViewModelFactory)
            .get(AnalisisDataViewModel::class.java)

        chatViewModel = ViewModelProvider(this, chatViewModelFactory)
            .get(ChatViewModel::class.java)

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
                /*Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()*/
                Helper.createSnackbar(binding.root, it.message).show()
                toggleEditMode()
            }
            if (it.code == 204 && it.message != null && role == User.ROLE_DOSEN || role == User.ROLE_ADMIN) {
                getAnalisisData()
            }
        })

        binding.inputFile.setOnClickListener {
            selectFile()
        }

        inputValidations()

        binding.buttonEditAnalisisData.setOnClickListener {
            updateAnalisisData()
        }
    }

    private fun getAnalisisData() {
        if (idAnalisisData == -1) {
            /*Snackbar.make(
                binding.root,
                "Terjadi kesalahan saat mengakses analisis data",
                Snackbar.LENGTH_LONG
            ).show()*/
            Helper.createSnackbar(binding.root, "Terjadi kesalahan saat mengakses analisis data")
                .show()
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
                binding.textViewEmailRequesterAnalisisData.text = it.analisisData.user.email

                userRequesterAnalisisData = it.analisisData.user

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
        // For updating status analisis data
        binding.statusAnalisisData.isEnabled = true
        binding.statusAnalisisData.setOnClickListener {
            prepareDialogUpdateStatus()
        }

        prepareChatAdminDosen()
    }

    private fun prepareDialogUpdateStatus() {
        val statusList = arrayOf(
            AnalisisData.STATUS_DIPROSES,
            AnalisisData.STATUS_DITOLAK,
            AnalisisData.STATUS_SELESAI
        )

        val selectedStatus = binding.statusAnalisisData.text.toString()
        var selectedStatusIndex = statusList.indexOf(selectedStatus)

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

    private fun prepareChatAdminDosen() {
        // For button chat
        binding.buttonChatAnalisisData.visibility = View.VISIBLE
        binding.buttonChatAnalisisData.setOnClickListener {
            chatViewModel.storeChatRoom(bearerToken, userRequesterAnalisisData.id!!)
            if (idChatRoom != ChatRoom.CHAT_ROOM_EMPTY) {
                startActivity(
                    Intent(this, DetailChatRoomActivity::class.java)
                        .putExtra(DetailChatRoomActivity.EXTRA_CHAT_ROOM_ID, idChatRoom)
                )
                return@setOnClickListener
            }
        }

        // Observing response from server in LiveData if idChatRoom has not been retrieved yet
        if (idChatRoom == ChatRoom.CHAT_ROOM_EMPTY) {
            chatViewModel.chatApiResponse.observeOnce(this, Observer<ChatApiResponse> {
                if (it.chatRoom != null) {
                    idChatRoom = it.chatRoom.id!!
                }
            })
            binding.buttonChatAnalisisData.performClick()
        }
    }

    /**
     * LiveData extension function to observe once.
     */
    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                removeObserver(this)
                observer.onChanged(t)
            }
        })
    }

    private fun toggleEditMode() {
        if (!isEditMode) {
            isEditMode = true
            binding.editTextJudul.isEnabled = true
            binding.editTextDeskripsi.isEnabled = true
            binding.inputLayout3.visibility = View.VISIBLE

            binding.editTextJudul.requestFocus()
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
        binding.editTextJudul.isEnabled = false
        binding.editTextDeskripsi.isEnabled = false
        binding.inputLayout3.visibility = View.GONE

        cancelItem.isVisible = true

        binding.buttonEditAnalisisData.visibility = android.view.View.GONE
        binding.statusAnalisisData.visibility = android.view.View.VISIBLE
        getAnalisisData()
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
        analisisDataViewModel.analisisDataApiResponse.observe(this, Observer {
            if (it.code != 200 && it.errors != null) {
                if (it.errors.judul != null) {
                    binding.textInputLayout1.error = it.errors.judul.get(0)
                }
                if (it.errors.deskripsi != null) {
                    binding.textInputLayout2.error = it.errors.deskripsi.get(0)
                }
                if (it.errors.file != null) {
                    binding.inputLayout3.error = it.errors.file.get(0)
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

        binding.editTextDeskripsi.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                binding.textInputLayout2.error = "Field deskripsi wajib diisi"
            } else {
                binding.textInputLayout2.error = null
            }
        }

        binding.inputFile.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                binding.inputLayout3.error = "Field file wajib diisi"
            } else {
                binding.inputLayout3.error = null
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
            val judul = binding.editTextJudul.text.toString()
            val deskripsi = binding.editTextDeskripsi.text.toString()
            val file = selectedFile

            analisisDataViewModel.updateAnalisisData(
                bearerToken,
                idAnalisisData,
                judul,
                deskripsi,
                file
            )
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