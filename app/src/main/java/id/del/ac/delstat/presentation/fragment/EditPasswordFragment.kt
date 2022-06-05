package id.del.ac.delstat.presentation.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import com.google.android.material.transition.MaterialFadeThrough
import id.del.ac.delstat.R
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.FragmentEditPasswordBinding
import id.del.ac.delstat.databinding.FragmentEditProfileBinding
import id.del.ac.delstat.presentation.activity.HomeActivity
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModel
import id.del.ac.delstat.util.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class EditPasswordFragment : Fragment() {
    private lateinit var binding: FragmentEditPasswordBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var userPreferences: UserPreferences

    private lateinit var password: String
    private lateinit var newPassword: String
    private lateinit var newPasswordConfirmation: String
    private lateinit var bearerToken: String

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
        return inflater.inflate(R.layout.fragment_edit_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEditPasswordBinding.bind(view)
        userViewModel = (activity as HomeActivity).userViewModel
        userPreferences = (activity as HomeActivity).userPreferences

        prepareUI()
    }

    private fun prepareUI() {
        runBlocking(Dispatchers.IO) {
            bearerToken = userPreferences.getUserToken.first()!!
        }

        binding.buttonEditPassword.setOnClickListener {
            updatePassword()

            /* Hiding the keyboard */
            val view = requireActivity().currentFocus
            if(view != null) {
                /*hideKeyboardFrom(applicationContext, binding.root)*/
                Helper.hideKeyboardFrom(requireContext(), view)
            }
            /* End of Hiding the keyboard */
        }

        inputValidation()
    }

    private fun checkInput(): Boolean {
        val validInput =
            binding.textInputLayout1.error == null &&
                    binding.textInputLayout2.error == null &&
                    binding.textInputLayout3.error == null

        return validInput
    }

    private fun inputValidation() {
        userViewModel.userApiResponse.observe(viewLifecycleOwner) {
            if(it.code == 400 && it.errors != null) {
                if(it.errors.password != null) {
                    binding.textInputLayout1.error = it.errors.password.get(0)
                }
                if(it.errors.newPassword != null) {
                    binding.textInputLayout2.error = it.errors.newPassword.get(0)
                }
            }
        }

        binding.editTextPasswordSaatIni.doOnTextChanged { text, start, before, count ->
            if (text == null || text.isEmpty()) {
                binding.textInputLayout1.error = "Password saat ini harus diisi"
            } else {
                binding.textInputLayout1.error = null
            }
        }

        binding.editTextPasswordBaru.doOnTextChanged { text, start, before, count ->
            if (text == null || text.isEmpty()) {
                binding.textInputLayout2.error = "Password baru harus diisi"
            } else if (text.length < 8) {
                binding.textInputLayout2.error = "Password minimal 8 karakter"
            } else {
                binding.textInputLayout2.error = null
            }

            if (binding.editTextPasswordBaru.text.toString() != binding.editTextPasswordBaruKonfirmasi.text.toString()) {
                binding.textInputLayout3.error = "Password tidak sama"
            } else if (binding.editTextPasswordBaru.text.toString() == binding.editTextPasswordBaruKonfirmasi.text.toString()) {
                binding.textInputLayout3.error = null
            }
        }

        binding.editTextPasswordBaruKonfirmasi.doOnTextChanged { text, start, before, count ->
            if (text == null || text.isEmpty()) {
                binding.textInputLayout3.error = "Konfirmasi password baru harus diisi"
            } else if (binding.editTextPasswordBaru.text.toString() != binding.editTextPasswordBaruKonfirmasi.text.toString()) {
                binding.textInputLayout3.error = "Password tidak sama"
            } else {
                binding.textInputLayout3.error = null
            }
        }
    }

    private fun updatePassword() {
        if (checkInput()) {
            bearerToken = "Bearer $bearerToken"

            Log.d("MyTag", "updatePassword")
            Log.d(
                "MyTag",
                "password saat ini: ${binding.editTextPasswordSaatIni.text.toString()}"
            )
            Log.d("MyTag", "updatePassword: " + binding.editTextPasswordBaru.text.toString())
            Log.d(
                "MyTag",
                "updatePassword: " + binding.editTextPasswordBaruKonfirmasi.text.toString()
            )
            Log.d("MyTag", "bearerToken: $bearerToken")

            password = binding.editTextPasswordSaatIni.text.toString()
            newPassword = binding.editTextPasswordBaru.text.toString()
            newPasswordConfirmation = binding.editTextPasswordBaruKonfirmasi.text.toString()

            userViewModel.updatePassword(
                bearerToken,
                password,
                newPassword,
                newPasswordConfirmation
            )
        }
    }

}