package id.del.ac.delstat.presentation.literatur.fragment

import android.app.ProgressDialog.show
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import id.del.ac.delstat.R
import id.del.ac.delstat.data.model.literatur.Literatur
import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.FragmentLiteraturBinding
import id.del.ac.delstat.presentation.activity.HomeActivity
import id.del.ac.delstat.presentation.literatur.activity.DetailLiteraturActivity
import id.del.ac.delstat.presentation.literatur.adapter.LiteraturAdapter
import id.del.ac.delstat.presentation.literatur.viewmodel.LiteraturViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class LiteraturFragment : Fragment() {
    private lateinit var binding: FragmentLiteraturBinding

    private lateinit var literaturViewModel: LiteraturViewModel
    private lateinit var literaturAdapter: LiteraturAdapter

    private lateinit var userPreferences: UserPreferences
    private var role: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_literatur, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLiteraturBinding.bind(view)
        literaturViewModel = (activity as HomeActivity).literaturViewModel
        userPreferences = (activity as HomeActivity).userPreferences

        prepareUI()
        initRecyclerView()
        getLiteratur()
    }

    private fun prepareUI() {
        runBlocking {
            role = userPreferences.getUserRole.first()!!
        }

        if(role == User.ROLE_DOSEN || role == User.ROLE_ADMIN) {
            binding.buttonAddLiteratur.visibility = View.VISIBLE
        } else {
            binding.buttonAddLiteratur.visibility = View.GONE
        }

        binding.swipeRefreshData.setOnRefreshListener {
            getLiteratur()
            binding.swipeRefreshData.isRefreshing = false
        }
    }

    private fun initRecyclerView() {
        literaturAdapter = LiteraturAdapter() { literatur: Literatur ->
            onClickLiteratur(literatur)
        }
        binding.recyclerViewLiteratur.adapter = literaturAdapter
        binding.recyclerViewLiteratur.layoutManager = LinearLayoutManager(context)
    }

    private fun onClickLiteratur(literatur: Literatur) {
        startActivity(
            Intent(requireActivity(), DetailLiteraturActivity::class.java)
                .putExtra(DetailLiteraturActivity.ID_LITERATUR, literatur.id)
        )
    }

    private fun getLiteratur() {
        literaturViewModel.getLiteratur()
        displayLiteratur()
    }

    private fun displayLiteratur() {
        binding.literaturProgressbar.visibility = View.VISIBLE

        literaturViewModel.literaturApiResponse.observe(viewLifecycleOwner, Observer {
            if(it.code == 200) {
                literaturAdapter.setList(it.literaturList!!)
                literaturAdapter.notifyDataSetChanged()
                binding.literaturProgressbar.visibility = View.GONE
            } else {
                binding.literaturProgressbar.visibility = View.GONE
            }
        })
    }

}