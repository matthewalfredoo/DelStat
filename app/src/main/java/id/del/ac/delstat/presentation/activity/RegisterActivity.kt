package id.del.ac.delstat.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import id.del.ac.delstat.databinding.ActivityRegisterBinding
import id.del.ac.delstat.presentation.di.Injector
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModel
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModelFactory
import javax.inject.Inject

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as Injector).createUserSubComponent().inject(this)

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

        binding.buttonRegister.setOnClickListener {
            register()
        }

        binding.buttonLogin.setOnClickListener {
            finish()
        }

        userViewModel.userApiResponse.observe(this, Observer {
            Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()
            if (it.code == 201) {
                // Go back to previous activity after successful registration
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 500)
            }
        })

        inputValidation()
    }

    private fun inputValidation() {
        userViewModel.userApiResponse.observe(this, Observer {
            if(it.code == 400 && it.errors != null) {
                if(it.errors.nama != null) {
                    binding.textInputLayout1.error = it.errors.nama.get(0)
                }
                if(it.errors.email != null) {
                    binding.textInputLayout2.error = it.errors.email.get(0)
                }
                if(it.errors.noHp != null) {
                    binding.textInputLayout3.error = it.errors.noHp.get(0)
                }
                if(it.errors.password != null) {
                    binding.textInputLayout4.error = it.errors.password.get(0)
                }
            }
        })

        binding.editTextNamaRegister.doOnTextChanged { text, start, before, count ->
            if (text == null || text.isEmpty()) {
                binding.textInputLayout1.error = "Field nama wajib diisi"
            } else {
                binding.textInputLayout1.error = null
            }
        }

        binding.editTextEmailRegister.doOnTextChanged { text, start, before, count ->
            if (text == null || text.isEmpty()) {
                binding.textInputLayout2.error = "Field email wajib diisi"
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                binding.textInputLayout2.error = "Email tidak valid"
            } else {
                binding.textInputLayout2.error = null
            }
        }

        binding.editTextNoHpRegister.doOnTextChanged { text, start, before, count ->
            if (text == null || text.isEmpty()) {
                binding.textInputLayout3.error = "Field nomor telepon wajib diisi"
            } else if (text.length < 10) {
                binding.textInputLayout3.error = "Nomor telepon tidak valid"
            } else {
                binding.textInputLayout3.error = null
            }
        }

        binding.editTextPasswordRegister.doOnTextChanged { text, start, before, count ->
            if (text == null || text.isEmpty()) {
                binding.textInputLayout4.error = "Field password wajib diisi"
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

    private fun register() {
        /*Log.d("MyTag", "Register")
        Log.d("MyTag", binding.editTextNamaRegister.text.toString())
        Log.d("MyTag", binding.editTextEmailRegister.text.toString())
        Log.d("MyTag", binding.editTextPasswordRegister.text.toString())
        Log.d("MyTag", binding.editTextNoHpRegister.text.toString())
        Log.d("MyTag", checkInput().toString())*/
        if (checkInput()) {
            val nama = binding.editTextNamaRegister.text.toString()
            val email = binding.editTextEmailRegister.text.toString()
            val noHp = binding.editTextNoHpRegister.text.toString()
            val password = binding.editTextPasswordRegister.text.toString()
            Log.d("MyTag", "Register: $nama, $email, $password, $noHp")
            userViewModel.register(nama, email, noHp, password)
        }
    }
}