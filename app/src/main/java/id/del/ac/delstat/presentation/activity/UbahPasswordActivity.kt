package id.del.ac.delstat.presentation.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.media.session.MediaButtonReceiver.handleIntent
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.R
import id.del.ac.delstat.databinding.ActivityUbahPasswordBinding
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModel
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModelFactory
import id.del.ac.delstat.util.Helper
import javax.inject.Inject

@AndroidEntryPoint
class UbahPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUbahPasswordBinding

    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    lateinit var userViewModel: UserViewModel

    private lateinit var token: String
    private lateinit var email: String

    companion object {
        const val PARAM_TOKEN = "token"
        const val PARAM_EMAIL = "email"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUbahPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this, userViewModelFactory)
            .get(UserViewModel::class.java)

        val appLinkIntent = intent
        handleIntent(intent)

        prepareUI()
    }

    private fun handleIntent(intent: Intent) {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        if (Intent.ACTION_VIEW == appLinkAction) {
            token = appLinkData?.getQueryParameter(PARAM_TOKEN) ?: ""
            email = appLinkData?.getQueryParameter(PARAM_EMAIL) ?: ""
            Log.d("MyTag", "token: $token")
            Log.d("MyTag", "email: $email")
        }
    }

    private fun prepareUI() {
        supportActionBar?.hide()
        /*supportActionBar?.setBackgroundDrawable(AppCompatResources.getDrawable(applicationContext, R.drawable.gradient_list))*/

        val animationDrawable =
            binding.rootLayout.background as android.graphics.drawable.AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()

        binding.buttonUbahPassword.setOnClickListener {
            /* Hiding the keyboard */
            val view = this.currentFocus
            if (view != null) {
                /*hideKeyboardFrom(applicationContext, binding.root)*/
                Helper.hideKeyboardFrom(applicationContext, view)
            }
            /* End of Hiding the keyboard */

            ubahPassword()
        }

        inputValidation()

        userViewModel.userApiResponse.observe(this, Observer {
            if (it.message != null) {
                Helper.createSnackbar(binding.root, it.message).show()

                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(
                        Intent(this, LoginActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra(LoginActivity.FROM_UBAH_PASSWORD, true)
                    )
                    finish()
                }, 2000)
            }
        })
    }

    private fun inputValidation() {
        userViewModel.userApiResponse.observe(this, Observer {
            if (it.code == 400 && it.errors != null) {
                if (it.errors.password != null) {
                    binding.textInputLayout1.error = it.errors.password[0]
                }
            }
        })

        binding.editTextPassword.doOnTextChanged { text, start, before, count ->
            if (text == null || text.isEmpty()) {
                binding.textInputLayout1.error = "Password baru harus diisi"
            } else if (text.length < 8) {
                binding.textInputLayout1.error = "Password minimal 8 karakter"
            } else {
                binding.textInputLayout1.error = null
            }

            if (binding.editTextPassword.text.toString() != binding.editTextPasswordConfirmation.text.toString()) {
                binding.textInputLayout2.error = "Password tidak sama"
            } else if (binding.editTextPassword.text.toString() == binding.editTextPasswordConfirmation.text.toString()) {
                binding.textInputLayout2.error = null
            }
        }

        binding.editTextPasswordConfirmation.doOnTextChanged { text, start, before, count ->
            if (text == null || text.isEmpty()) {
                binding.textInputLayout2.error = "Konfirmasi password baru harus diisi"
            } else if (binding.editTextPassword.text.toString() != binding.editTextPasswordConfirmation.text.toString()) {
                binding.textInputLayout2.error = "Password tidak sama"
            } else {
                binding.textInputLayout2.error = null
            }
        }
    }

    private fun checkInput(): Boolean {
        val validInput =
            binding.textInputLayout1.error == null &&
                    binding.textInputLayout2.error == null
        return validInput
    }

    private fun ubahPassword() {
        if (checkInput()) {
            val password = binding.editTextPassword.text.toString()
            val passwordConfirmation = binding.editTextPasswordConfirmation.text.toString()

            userViewModel.changePassword(token, email, password, passwordConfirmation)
        }
    }
}