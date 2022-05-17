package id.del.ac.delstat.presentation.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.R
import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.ActivityHomeBinding
import id.del.ac.delstat.presentation.analisisdata.activity.ListAnalisisDataActivity
import id.del.ac.delstat.presentation.literatur.viewmodel.LiteraturViewModel
import id.del.ac.delstat.presentation.literatur.viewmodel.LiteraturViewModelFactory
import id.del.ac.delstat.presentation.materi.viewmodel.MateriViewModel
import id.del.ac.delstat.presentation.materi.viewmodel.MateriViewModelFactory
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModel
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding

    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    lateinit var userViewModel: UserViewModel

    @Inject
    lateinit var materiViewModelFactory: MateriViewModelFactory
    lateinit var materiViewModel: MateriViewModel

    @Inject
    lateinit var literaturViewModelFactory: LiteraturViewModelFactory
    lateinit var literaturViewModel: LiteraturViewModel

    @Inject
    lateinit var userPreferences: UserPreferences
    var bearerToken: String? = null

    @Inject
    lateinit var delStatApiService: DelStatApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this, userViewModelFactory)
            .get(UserViewModel::class.java)

        materiViewModel = ViewModelProvider(this, materiViewModelFactory)
            .get(MateriViewModel::class.java)

        literaturViewModel = ViewModelProvider(this, literaturViewModelFactory)
            .get(LiteraturViewModel::class.java)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.bottomNavigation.setupWithNavController(navController)

        prepareUI()
    }

    private fun prepareUI() {
        userViewModel.userApiResponse.observe(this, Observer {
            Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()
        })

        lifecycleScope.launch(Dispatchers.IO) {
            bearerToken = userPreferences.getUserToken.first()
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch(Dispatchers.IO) {
            bearerToken = userPreferences.getUserToken.first()
        }
    }

    /* This method is going to make the menu that appears on the top of the screen or the ActionBar */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_action_bar_homeactivity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.analisisDataActivity -> {
                proceedAnalisisDataAcitivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun proceedAnalisisDataAcitivity() {
        if (!bearerToken.isNullOrEmpty()) {
            startActivity(Intent(this@HomeActivity, ListAnalisisDataActivity::class.java))
            return
        }
        startActivity(
            Intent(this@HomeActivity, LoginActivity::class.java)
                .putExtra(LoginActivity.LOGIN_MESSAGE, "Login untuk mengakses menu analisis data")
        )
    }
}