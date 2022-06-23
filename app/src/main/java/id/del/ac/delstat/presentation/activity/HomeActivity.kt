package id.del.ac.delstat.presentation.activity

import android.R.attr.delay
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import id.del.ac.delstat.presentation.chat.activity.ListChatRoomActivity
import id.del.ac.delstat.presentation.kuis.viewmodel.HasilKuisViewModel
import id.del.ac.delstat.presentation.kuis.viewmodel.HasilKuisViewModelFactory
import id.del.ac.delstat.presentation.literatur.viewmodel.LiteraturViewModel
import id.del.ac.delstat.presentation.literatur.viewmodel.LiteraturViewModelFactory
import id.del.ac.delstat.presentation.materi.viewmodel.MateriViewModel
import id.del.ac.delstat.presentation.materi.viewmodel.MateriViewModelFactory
import id.del.ac.delstat.presentation.notifikasi.activity.NotifikasiDialogFragment
import id.del.ac.delstat.presentation.notifikasi.viewmodel.NotifikasiViewModel
import id.del.ac.delstat.presentation.notifikasi.viewmodel.NotifikasiViewModelFactory
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModel
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModelFactory
import id.del.ac.delstat.util.Helper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var notifikasi: View
    lateinit var notifikasiBadge: TextView

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
    lateinit var notifikasiViewModelFactory: NotifikasiViewModelFactory
    lateinit var notifikasiViewModel: NotifikasiViewModel
    lateinit var runCheckCountNotifikasi: Runnable
    var handlerCheckCountNotifikasi: Handler = Handler(Looper.getMainLooper())
    val delayInterval: Long = 8000

    @Inject
    lateinit var hasilKuisViewModelFactory: HasilKuisViewModelFactory
    lateinit var hasilKuisViewModel: HasilKuisViewModel

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

        notifikasiViewModel = ViewModelProvider(this, notifikasiViewModelFactory)
            .get(NotifikasiViewModel::class.java)

        hasilKuisViewModel = ViewModelProvider(this, hasilKuisViewModelFactory)
            .get(HasilKuisViewModel::class.java)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.bottomNavigation.setupWithNavController(navController)

        prepareUI()
    }

    private fun prepareUI() {
        userViewModel.userApiResponse.observe(this, Observer {
            /*Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()*/
            Helper.createSnackbar(binding.root, it.message!!).show()
        })

        materiViewModel.materiApiResponse.observe(this, Observer {
            if(it.code == 204) {
                /*Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()*/
                Helper.createSnackbar(binding.root, it.message!!).show()
            }
        })

        lifecycleScope.launch(Dispatchers.IO) {
            bearerToken = userPreferences.getUserToken.first()

            // This is to find whether user's token is expired or not
            if(!bearerToken.isNullOrEmpty()) {
                userViewModel.getUser("Bearer $bearerToken")
                // bearerToken = ""
            }

            // When user's token is not expired, then it will get notifications count for user
            // This will check the notification count every 2 seconds
            handlerCheckCountNotifikasi.postDelayed(Runnable {
                handlerCheckCountNotifikasi.postDelayed(runCheckCountNotifikasi, delayInterval)
                notifikasiViewModel.getCountNotifikasi("Bearer $bearerToken")
            }.also { runCheckCountNotifikasi = it }, delayInterval)
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

        /* Handling notifikasi */
        // Finding the menu item of notifikasi in the menu and setting the click listener
        notifikasi = menu?.findItem(R.id.notifikasiActivity)!!.actionView
        notifikasiBadge = notifikasi.findViewById(R.id.notification_badge)
        notifikasi.setOnClickListener {
            proceedNotifikasiActivity()
        }

        // Getting notifiksai count
        notifikasiViewModel.notifikasiApiResponse.observe(this, Observer {
            if(it.code == 200 && it.countNotifikasi != null && it.countNotifikasi > 0) {
                notifikasiBadge.visibility = View.VISIBLE
                notifikasiBadge.text = it.countNotifikasi.toString()
            }
            if(it.code == 200 && it.countNotifikasi != null && it.countNotifikasi <= 0) {
                notifikasiBadge.visibility = View.GONE
            }
        })
        /* End of handling notifikasi */

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.analisisDataActivity -> {
                proceedAnalisisDataAcitivity()
            }
            R.id.chatActivity -> {
                proceedChatActivity()
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

    private fun proceedChatActivity() {
        if (!bearerToken.isNullOrEmpty()) {
            startActivity(Intent(this@HomeActivity, ListChatRoomActivity::class.java))
            return
        }
        startActivity(
            Intent(this@HomeActivity, LoginActivity::class.java)
                .putExtra(LoginActivity.LOGIN_MESSAGE, "Login untuk mengakses menu chat")
        )
    }

    private fun proceedNotifikasiActivity() {
        Log.d("MyTag", "Proceed notifikasi")
        if(!bearerToken.isNullOrEmpty()) {
            NotifikasiDialogFragment().show(supportFragmentManager, NotifikasiDialogFragment.TAG)
            return
        }
        startActivity(
            Intent(this@HomeActivity, LoginActivity::class.java)
                .putExtra(LoginActivity.LOGIN_MESSAGE, "Login untuk mengakses menu notifikasi")
        )
    }

    private fun getCountNotifikasi() {
        if(!bearerToken.isNullOrEmpty()) {
            notifikasiViewModel.getCountNotifikasi("Bearer $bearerToken")
            Log.d("MyTag", "Get count notifikasi")
        }
    }
}