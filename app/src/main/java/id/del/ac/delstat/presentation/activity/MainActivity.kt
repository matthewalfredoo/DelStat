package id.del.ac.delstat.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.ActivityMainBinding
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModel
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModelFactory
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    private lateinit var userViewModel: UserViewModel

    @Inject
    lateinit var userPreferences: UserPreferences

    @Inject
    lateinit var delStatApiService: DelStatApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this, userViewModelFactory)
            .get(UserViewModel::class.java)

        userViewModel.userApiResponse.observe(this, Observer {
            Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()
        })

        userPreferences.getUserEmail.asLiveData().observe(this, Observer {
            binding.textViewEmail.text = it
        })

        userPreferences.getUserToken.asLiveData().observe(this, Observer {
            binding.textViewToken.text = it
        })

        binding.buttonLogin.setOnClickListener {
            login()
        }

        binding.buttonSignup.setOnClickListener {
            register()
        }

        binding.buttonLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        var bearerToken: String? = null
        userPreferences.getUserToken.asLiveData().observe(this, Observer {
            bearerToken = it
        })
        // Log.d("MyTag", "logout: $bearerToken")
        bearerToken = "Bearer $bearerToken"

        userViewModel.logout(bearerToken!!)
    }

    fun login() {
        val email = binding.editTextEmailLogin.text.toString()
        val password = binding.editTextPasswordLogin.text.toString()
        userViewModel.login(email, password)
    }

    fun register() {
        val nama = binding.editTextNamaSignup.text.toString()
        val email = binding.editTextEmailSignup.text.toString()
        val noHp = binding.editTextNoHpSignup.text.toString()
        val password = binding.editTextPasswordSignup.text.toString()
        val passwordConfirmation = binding.editTextPasswordConfirmationSignup.text.toString()
        val jenjang = binding.editTextJenjangSignup.text.toString()

        val user = User(
            nama = nama,
            email = email,
            noHp = noHp,
            jenjang = jenjang,
        )

        userViewModel.register(nama, email, noHp, password)
    }
}