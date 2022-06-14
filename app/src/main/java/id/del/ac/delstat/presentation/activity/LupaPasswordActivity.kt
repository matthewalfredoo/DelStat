package id.del.ac.delstat.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.databinding.ActivityLupaPasswordBinding
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModel
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModelFactory
import id.del.ac.delstat.util.Helper
import javax.inject.Inject

@AndroidEntryPoint
class LupaPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLupaPasswordBinding

    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLupaPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this, userViewModelFactory)
            .get(UserViewModel::class.java)

        prepareUI()
    }

    private fun prepareUI() {
        supportActionBar?.hide()

        val animationDrawable =
            binding.rootLayout.background as android.graphics.drawable.AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()

        binding.buttonSendEmailResetPassword.setOnClickListener {
            forgotPassword()

            /* Hiding the keyboard */
            val view = this.currentFocus
            if(view != null) {
                /*hideKeyboardFrom(applicationContext, binding.root)*/
                Helper.hideKeyboardFrom(applicationContext, view)
            }
            /* End of Hiding the keyboard */
        }

        inputValidation()

        userViewModel.userApiResponse.observe(this, Observer {
            if(it.code == 200 && it.message != null) {
                Helper.createSnackbar(binding.rootLayout, it.message).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 1500)
            }
        })
    }

    private fun inputValidation() {
        userViewModel.userApiResponse.observe(this, Observer {
            if(it.code == 400 && it.errors != null) {
                if(it.errors.email != null) {
                    binding.textInputLayout1.error = it.errors.email.get(0)
                }
            }
        })

        binding.editTextEmailLogin.doOnTextChanged { text, start, before, count ->
            if (text == null || text.isEmpty()) {
                binding.textInputLayout1.error = "Field email wajib diisi"
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                binding.textInputLayout1.error = "Email tidak valid"
            } else {
                binding.textInputLayout1.error = null
            }
        }
    }

    private fun checkInput(): Boolean {
        val validInput = binding.textInputLayout1.error == null
        return validInput
    }

    private fun forgotPassword() {
        if(checkInput()) {
            val email = binding.editTextEmailLogin.text.toString()
            userViewModel.forgotPassword(email)
        }
    }
}