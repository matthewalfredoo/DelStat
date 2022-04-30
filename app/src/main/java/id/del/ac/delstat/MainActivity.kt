package id.del.ac.delstat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.databinding.ActivityMainBinding
import id.del.ac.delstat.presentation.di.Injector
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModel
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = id.del.ac.delstat.databinding.ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as Injector).createUserSubComponent().inject(this)

        userViewModel = ViewModelProvider(this, userViewModelFactory).get(UserViewModel::class.java)

        userViewModel.userApiResponse.observe(this, Observer {
            Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()
            binding.textViewToken.text = it.token
        })

        binding.buttonLogin.setOnClickListener {
            Log.d("MyTag", BuildConfig.BASE_URL)
            login()
        }
    }

    fun login() {
        Log.d(TAG, "login")
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        userViewModel.login(email, password)
    }
}