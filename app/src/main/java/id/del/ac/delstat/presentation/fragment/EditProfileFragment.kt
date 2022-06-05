package id.del.ac.delstat.presentation.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import id.del.ac.delstat.BuildConfig
import id.del.ac.delstat.R
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.FragmentEditProfileBinding
import id.del.ac.delstat.presentation.activity.HomeActivity
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModel
import id.del.ac.delstat.util.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File

class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var userPreferences: UserPreferences

    private lateinit var nama: String
    private lateinit var email: String
    private lateinit var noHp: String
    private lateinit var jenjang: String
    private lateinit var fotoProfil: String
    private lateinit var bearerToken: String

    private var intentData: Intent? = null
    private var selectedImageUri: Uri? = null
    private var selectedImagePath: String? = null
    private var selectedImageFile: File? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                Log.d("MyTag", "Access to external storage granted")
                selectPhoto()
            }
        }

    private val selectPhotoResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                intentData = result.data
                if (intentData != null) {
                    selectedImageUri = intentData!!.data
                    selectPhotoResult()
                }
            } else {
                Snackbar.make(
                    binding.root,
                    "Batal mengubah foto.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    private val cropPhotoLauncher = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            selectedImageUri = result.uriContent
            selectedImagePath = result.getUriFilePath(requireContext())
            cropPhotoResult()
        } else {
            Snackbar.make(binding.root, "Batal mengubah foto.", Snackbar.LENGTH_SHORT).show()
            Log.e("MyTag", "Crop image failed", result.error)
        }
    }

    companion object {
        var PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myTransition = MaterialFadeThrough()
        myTransition.duration = 500

        enterTransition = MaterialFadeThrough()
        exitTransition = myTransition
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEditProfileBinding.bind(view)
        userViewModel = (activity as HomeActivity).userViewModel
        userPreferences = (activity as HomeActivity).userPreferences

        prepareUI()
    }

    private fun selectPhoto() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.d("MyTag", "No permission needed, and do something")
        }
        activity?.let {
            if (hasPermissions(requireContext(), Helper.PERMISSIONS)) {
                Log.d("MyTag", "Permission granted, and do something")
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        .setType("image/*")
                selectPhotoResultLauncher.launch(intent)
            } else {
                requestPermissionLauncher.launch(Helper.PERMISSIONS)
            }
        }
    }

    private fun cropPhoto() {
        cropPhotoLauncher.launch(
            options(uri = selectedImageUri) {
                setGuidelines(CropImageView.Guidelines.ON)
                setAspectRatio(1, 1)
                setOutputCompressQuality(80)
                setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
            }
        )
    }

    private fun selectPhotoResult() {
        // binding.imageViewProfile.setImageURI(selectedImageUri)
        /*Log.d(
            "MyTag",
            "SelectPhotoResult: ${RealPathUtil.getRealPath(requireContext(), selectedImageUri!!)}"
        )*/
        cropPhoto()
    }

    private fun cropPhotoResult() {
        binding.imageViewProfile.setImageURI(selectedImageUri)
        Log.d("MyTag", "CropPhotoResult: $selectedImagePath")
        selectedImageFile = File(selectedImagePath!!)
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    private fun prepareUI() {
        runBlocking(Dispatchers.IO) {
            nama = userPreferences.getUserNama.first()!!
            email = userPreferences.getUserEmail.first()!!
            noHp = userPreferences.getUserNoHp.first()!!
            jenjang = userPreferences.getUserJenjang.first()!!
            fotoProfil = BuildConfig.BASE_URL + userPreferences.getUserFotoProfil.first()!!
            bearerToken = userPreferences.getUserToken.first()!!
        }

        binding.apply {
            editTextNamaEditProfile.setText(nama)
            editTextEmailEditProfile.setText(email)
            editTextNoHpEditProfile.setText(noHp)
            editTextJenjangEditProfile.setText(jenjang)
            /*Glide.with(requireActivity().applicationContext)
                .load(fotoProfil)
                .apply(RequestOptions().signature(ObjectKey(System.currentTimeMillis().toString())))
                .into(imageViewProfile)*/
            Helper.showImageGlide(requireContext(), fotoProfil, binding.imageViewProfile)
        }

        binding.buttonEditProfile.setOnClickListener {
            editProfile()

            /* Hiding the keyboard */
            val view = requireActivity().currentFocus
            if(view != null) {
                /*hideKeyboardFrom(applicationContext, binding.root)*/
                Helper.hideKeyboardFrom(requireContext(), view)
            }
            /* End of Hiding the keyboard */
        }

        binding.buttonEditPassword.setOnClickListener {
            findNavController().navigate(R.id.action_editProfileFragment_to_editPasswordFragment)
        }

        binding.imageViewProfile.setOnClickListener {
            selectPhoto()
        }

        inputValidation()
    }

    private fun inputValidation() {
        binding.editTextNamaEditProfile.doOnTextChanged { text, start, before, count ->
            if (text == null || text.isEmpty()) {
                binding.textInputLayout1.error = "Nama harus diisi"
            } else {
                binding.textInputLayout1.error = null
            }
        }

        binding.editTextEmailEditProfile.doOnTextChanged { text, start, before, count ->
            if (text == null || text.isEmpty()) {
                binding.textInputLayout2.error = "Email harus diisi"
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                binding.textInputLayout2.error = "Email tidak valid"
            } else {
                binding.textInputLayout2.error = null
            }
        }

        binding.editTextNoHpEditProfile.doOnTextChanged { text, start, before, count ->
            if (text == null || text.isEmpty()) {
                binding.textInputLayout3.error = "Nomor telepon harus diisi"
            } else if (text.length < 10) {
                binding.textInputLayout3.error = "Nomor telepon tidak valid"
            } else {
                binding.textInputLayout3.error = null
            }
        }

        binding.editTextJenjangEditProfile.doOnTextChanged { text, start, before, count ->
            if (text == null || text.isEmpty()) {
                binding.textInputLayout4.error = "Jenjang harus diisi"
            } else {
                binding.textInputLayout4.error = null
            }
        }
    }

    private fun checkInput(): Boolean {
        val validInput =
            binding.textInputLayout1.error == null &&
                    binding.textInputLayout2.error == null &&
                    binding.textInputLayout3.error == null &&
                    binding.textInputLayout4.error == null

        return validInput
    }

    private fun editProfile() {
        if (checkInput()) {
            bearerToken = "Bearer $bearerToken"
            /*Log.d("MyTag", "Edit Profile")
            Log.d("MyTag", binding.editTextNamaEditProfile.text.toString())
            Log.d("MyTag", binding.editTextEmailEditProfile.text.toString())
            Log.d("MyTag", binding.editTextNoHpEditProfile.text.toString())
            Log.d("MyTag", binding.editTextJenjangEditProfile.text.toString())*/
            nama = binding.editTextNamaEditProfile.text.toString()
            email = binding.editTextEmailEditProfile.text.toString()
            noHp = binding.editTextNoHpEditProfile.text.toString()
            jenjang = binding.editTextJenjangEditProfile.text.toString()
            if (selectedImageFile != null) {
                Log.d("MyTag", "Selected Image File: ${selectedImageFile!!.absolutePath}")
            }
            userViewModel.updateProfile(bearerToken, nama, email, noHp, jenjang, selectedImageFile)
        }
    }

}