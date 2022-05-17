package id.del.ac.delstat.presentation.analisisdata.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.model.analisisdata.AnalisisData
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.ActivityListAnalisisDataBinding
import id.del.ac.delstat.presentation.analisisdata.adapter.AnalisisDataAdapter
import id.del.ac.delstat.presentation.analisisdata.viewmodel.AnalisisDataViewModel
import id.del.ac.delstat.presentation.analisisdata.viewmodel.AnalisisDataViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class ListAnalisisDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListAnalisisDataBinding

    @Inject
    lateinit var analisisDataViewModelFactory: AnalisisDataViewModelFactory
    private lateinit var analisisDataViewModel: AnalisisDataViewModel

    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var bearerToken: String

    lateinit var analisisDataAdapter: AnalisisDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListAnalisisDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        analisisDataViewModel = ViewModelProvider(this, analisisDataViewModelFactory)
            .get(AnalisisDataViewModel::class.java)

        prepareUI()
        getAnalisisData()
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        getAnalisisData()
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
        analisisDataAdapter = AnalisisDataAdapter(){ analisisData ->
            onClickAnalisisData(analisisData)
        }
        binding.recyclerViewAnalisisData.adapter = analisisDataAdapter
        binding.recyclerViewAnalisisData.layoutManager = LinearLayoutManager(this)
    }

    private fun onClickAnalisisData(analisisData: AnalisisData) {
        val intent = Intent(this, DetailAnalisisDataActivity::class.java)
            .putExtra(DetailAnalisisDataActivity.DATA_ANALISIS_DATA, analisisData.id)

        startActivity(intent)
    }

    private fun getAnalisisData() {
        analisisDataViewModel.getAnalisisData(bearerToken)
        displayAnalisisData()
    }

    private fun displayAnalisisData() {
        binding.analisisDataProgressbar.visibility = android.view.View.VISIBLE

        analisisDataViewModel.analisisDataApiResponse.observe(this, Observer {
            if (it.code == 200) {
                analisisDataAdapter.setList(it.listAnalisisData!!)
                analisisDataAdapter.notifyDataSetChanged()
                binding.analisisDataProgressbar.visibility = android.view.View.GONE
            } else {
                binding.analisisDataProgressbar.visibility = android.view.View.GONE
                Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun prepareUI() {
        runBlocking {
            bearerToken = "Bearer ${userPreferences.getUserToken.first()!!}"
        }

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Analisis Data"

        binding.buttonAnalisisData.setOnClickListener {
            startActivity(Intent(this, CreateAnalisisDataActivity::class.java))
        }

        binding.swipeRefreshData.setOnRefreshListener {
            getAnalisisData()
            binding.swipeRefreshData.isRefreshing = false
        }
    }
}