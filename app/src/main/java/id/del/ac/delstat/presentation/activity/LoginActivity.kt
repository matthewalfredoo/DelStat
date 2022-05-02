package id.del.ac.delstat.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import id.del.ac.delstat.R
import id.del.ac.delstat.databinding.ActivityLoginBinding
import id.del.ac.delstat.presentation.di.Injector
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModel
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModelFactory
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as Injector).createUserSubComponent().inject(this)

        userViewModel = ViewModelProvider(this, userViewModelFactory)
            .get(UserViewModel::class.java)

        prepareUI()
    }

    private fun prepareUI() {
        supportActionBar?.hide()
        /*supportActionBar?.setBackgroundDrawable(AppCompatResources.getDrawable(applicationContext, R.drawable.gradient_list))*/

        val animationDrawable = binding.rootLayout.background as android.graphics.drawable.AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(4000)
        animationDrawable.start()

        binding.buttonLogin.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            login()
        }

        userViewModel.userApiResponse.observe(this, Observer {
            Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()
            if(it.code == 200) {
                // Go back to previous activity after successful login
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 3000)
            }
        })

        inputValidation()
    }

    private fun inputValidation(){
        // Check if email is empty or not
        binding.editTextEmailLogin.doOnTextChanged { text, start, before, count ->
            if(text == null || text.isEmpty()){
                binding.textInputLayout1.error = "Email harus diisi"
            }
            else{
                binding.textInputLayout1.error = null
            }
        }

        // Check if password is empty or not
        binding.editTextPasswordLogin.doOnTextChanged { text, start, before, count ->
            if(text == null || text.isEmpty()){
                binding.textInputLayout2.error = "Password harus diisi"
            }
            else{
                binding.textInputLayout2.error = null
            }
        }
    }

    private fun checkInput(): Boolean{
        // If error is null then input is not empty or valid and return true
        val validInput = binding.textInputLayout1.error == null && binding.textInputLayout2.error == null
        binding.buttonLogin.isClickable = validInput
        return true
    }

    private fun login() {
        if(checkInput()){
            val email = binding.editTextEmailLogin.text.toString()
            val password = binding.editTextPasswordLogin.text.toString()
            Log.d("LoginActivity", "email: $email, password: $password")
            userViewModel.login(email, password)
        }
        binding.progressBar.visibility = View.GONE
    }
}