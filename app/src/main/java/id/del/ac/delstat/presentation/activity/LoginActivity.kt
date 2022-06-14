package id.del.ac.delstat.presentation.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.databinding.ActivityLoginBinding
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModel
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModelFactory
import id.del.ac.delstat.util.Helper
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    lateinit var userViewModel: UserViewModel

    var fromUbahPasswordActivity = false

    companion object{
        const val LOGIN_MESSAGE = "login_message"
        const val FROM_UBAH_PASSWORD = "from_ubah_password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this, userViewModelFactory)
            .get(UserViewModel::class.java)

        prepareUI()
    }

    private fun prepareUI() {
        val intentLogin = intent
        val loginMessage = intentLogin.getStringExtra(LOGIN_MESSAGE)
        if(loginMessage != null){
            /*Snackbar.make(binding.root, loginMessage, Snackbar.LENGTH_LONG).show()*/
            Helper.createSnackbar(binding.root, loginMessage).show()
        }

        fromUbahPasswordActivity = intentLogin.getBooleanExtra(FROM_UBAH_PASSWORD, false)

        supportActionBar?.hide()
        /*supportActionBar?.setBackgroundDrawable(AppCompatResources.getDrawable(applicationContext, R.drawable.gradient_list))*/

        val animationDrawable =
            binding.rootLayout.background as android.graphics.drawable.AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()

        binding.buttonLogin.setOnClickListener {
            login()

            /* Hiding the keyboard */
            val view = this.currentFocus
            if(view != null) {
                /*hideKeyboardFrom(applicationContext, binding.root)*/
                Helper.hideKeyboardFrom(applicationContext, view)
            }
            /* End of Hiding the keyboard */
        }

        binding.buttonLupaPassword.setOnClickListener {
            startActivity(Intent(this, LupaPasswordActivity::class.java))
        }

        binding.buttonRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        userViewModel.userApiResponse.observe(this, Observer {
            /*Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()*/
            Helper.createSnackbar(binding.root, it.message!!).show()
            if (it.code == 200) {
                // Go back to previous activity after successful login
                if(!fromUbahPasswordActivity) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        finish()
                    }, 500)
                    return@Observer
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this, HomeActivity::class.java))
                }, 500)
            }
        })

        inputValidation()
    }

    private fun inputValidation() {
        userViewModel.userApiResponse.observe(this, Observer {
            if(it.code == 400 && it.errors != null) {
                if(it.errors.email != null) {
                    binding.textInputLayout1.error = it.errors.email.get(0)
                }
                if(it.errors.password != null) {
                    binding.textInputLayout2.error = it.errors.password.get(0)
                }
            }
        })

        // Check if email is empty or not
        binding.editTextEmailLogin.doOnTextChanged { text, start, before, count ->
            if (text == null || text.isEmpty()) {
                binding.textInputLayout1.error = "Field email wajib diisi"
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                binding.textInputLayout1.error = "Email tidak valid"
            } else {
                binding.textInputLayout1.error = null
            }
        }

        // Check if password is empty or not
        binding.editTextPasswordLogin.doOnTextChanged { text, start, before, count ->
            if (text == null || text.isEmpty()) {
                binding.textInputLayout2.error = "Field password wajib diisi"
            } else {
                binding.textInputLayout2.error = null
            }
        }
    }

    private fun checkInput(): Boolean {
        // If error is null then input is not empty or valid and return true
        val validInput = binding.textInputLayout1.error == null && binding.textInputLayout2.error == null
        // binding.buttonLogin.isClickable = validInput - doesn't work
        return validInput
    }

    private fun login() {
        if (checkInput()) {
            val email = binding.editTextEmailLogin.text.toString()
            val password = binding.editTextPasswordLogin.text.toString()
            // Log.d("LoginActivity", "email: $email, password: $password")
            userViewModel.login(email, password)
        }
    }
}