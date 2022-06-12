package id.del.ac.delstat.presentation.kuis.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.R
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.ActivityHasilKuisBinding
import id.del.ac.delstat.presentation.activity.LoginActivity
import id.del.ac.delstat.presentation.kuis.adapter.HasilKuisAdapter
import id.del.ac.delstat.presentation.kuis.viewmodel.HasilKuisViewModel
import id.del.ac.delstat.presentation.kuis.viewmodel.HasilKuisViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class HasilKuisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHasilKuisBinding

    @Inject
    lateinit var hasilKuisViewModelFactory: HasilKuisViewModelFactory
    private lateinit var hasilKuisViewModel: HasilKuisViewModel

    @Inject
    lateinit var userPreferences: UserPreferences
    lateinit var bearerToken: String

    lateinit var hasilKuisAdapter: HasilKuisAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHasilKuisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hasilKuisViewModel = ViewModelProvider(this, hasilKuisViewModelFactory)
            .get(HasilKuisViewModel::class.java)

        prepareUI()
        initRecyclerView()
        getHasilKuis()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun initRecyclerView() {
        hasilKuisAdapter = HasilKuisAdapter()
        binding.recyclerViewHasilKuis.adapter = hasilKuisAdapter
        binding.recyclerViewHasilKuis.layoutManager = LinearLayoutManager(this)
    }

    private fun getHasilKuis() {
        hasilKuisViewModel.getHasilKuis(bearerToken)
        displayHasilKuis()
    }

    private fun displayHasilKuis() {
        binding.hasilKuisProgressbar.visibility = View.VISIBLE

        hasilKuisViewModel.hasilKuisApiResponse.observe(this, Observer {
            if(it.code == 200 && it.listHasilKuis != null) {
                hasilKuisAdapter.setList(it.listHasilKuis)
                hasilKuisAdapter.notifyDataSetChanged()
                binding.hasilKuisProgressbar.visibility = View.GONE
            }
            else {
                binding.hasilKuisProgressbar.visibility = View.GONE
                Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun prepareUI() {
        runBlocking {
            bearerToken = userPreferences.getUserToken.first()!!
            if(bearerToken.isEmpty()) {
                startActivity(
                    Intent(this@HasilKuisActivity, LoginActivity::class.java)
                        .putExtra(LoginActivity.LOGIN_MESSAGE, "Login untuk mengakses menu Hasil Kuis")
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                finish()
            }
            bearerToken = "Bearer $bearerToken"
        }

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Hasil Kuis"
    }
}