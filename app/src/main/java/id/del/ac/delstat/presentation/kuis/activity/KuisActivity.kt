package id.del.ac.delstat.presentation.kuis.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.R
import id.del.ac.delstat.data.model.kuis.Kuis
import id.del.ac.delstat.data.model.kuis.KumpulanKuis
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.ActivityKuisBinding
import id.del.ac.delstat.presentation.kuis.adapter.SoalKuisAdapter
import id.del.ac.delstat.presentation.kuis.viewmodel.HasilKuisViewModel
import id.del.ac.delstat.presentation.kuis.viewmodel.HasilKuisViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class KuisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKuisBinding

    @Inject
    lateinit var kuisViewModelFactory: HasilKuisViewModelFactory
    lateinit var kuisViewModel: HasilKuisViewModel

    private var idKuis = -1
    private lateinit var kuis: Kuis
    private lateinit var buttonSubmitKuisMenuItem: MenuItem
    private var isQuizMode = false
    private var quizDuration = TimeUnit.MINUTES.toMillis(10) // duration of quiz is 10 minutes

    private lateinit var soalKuisAdapter: SoalKuisAdapter

    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var bearerToken: String

    companion object{
        const val EXTRA_ID_KUIS = "extra_id_kuis"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKuisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kuisViewModel = ViewModelProvider(this, kuisViewModelFactory)
            .get(HasilKuisViewModel::class.java)

        prepareUI()
        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_actionbar_kuisactivity, menu)

        buttonSubmitKuisMenuItem = menu?.findItem(R.id.button_submit_kuis)!!
        buttonSubmitKuisMenuItem.isEnabled = false

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun prepareUI() {
        // setting up the action bar
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Kuis"

        idKuis = intent.getIntExtra(EXTRA_ID_KUIS, -1)

        if(idKuis == -1){
            Snackbar
                .make(binding.root, "Terjadi kesalahan saat mengakses kuis", Snackbar.LENGTH_SHORT)
                .show()

            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 500)
        }

        // else
        kuis = KumpulanKuis.kumpulanKuis.get(idKuis - 1)
        Log.d("MyTag", kuis.nama!!)

        binding.textViewNamaKuis.text = kuis.nama
        binding.deskripsiKuis.text = kuis.deskripsi

        binding.buttonStartKuis.setOnClickListener {
            toggleQuizMode()
        }

        runBlocking {
            bearerToken = userPreferences.getUserToken.first()!!
            bearerToken = "Bearer $bearerToken"
        }
    }

    private fun toggleQuizMode() {
        if(!isQuizMode) {
            isQuizMode = true

            binding.containerKuisNotStarted.visibility = View.GONE
            binding.recyclerViewSoalKuis.visibility = View.VISIBLE

            buttonSubmitKuisMenuItem.isEnabled = true

            // setting up the timer
            setUpQuizCountdownTimer()
        }
    }

    private fun setUpQuizCountdownTimer() {
        val timer  = object : CountDownTimer(quizDuration, 1000) {
            override fun onTick(millsUntilFinished: Long) {
                val sDuration = String.format(Locale("in", "ID"), "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millsUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millsUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millsUntilFinished))
                )
                binding.textViewTimerKuis.text = sDuration
            }

            override fun onFinish() {
                Snackbar
                    .make(binding.root, "Waktu kuis telah habis", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
        timer.start()
    }

    private fun initRecyclerView() {
        soalKuisAdapter = SoalKuisAdapter()
        binding.recyclerViewSoalKuis.adapter = soalKuisAdapter
        binding.recyclerViewSoalKuis.layoutManager = LinearLayoutManager(this)
        displaySoalKuis()
    }

    private fun displaySoalKuis() {
        soalKuisAdapter.setList(kuis.listSoal!!)
        soalKuisAdapter.notifyDataSetChanged()
    }
}