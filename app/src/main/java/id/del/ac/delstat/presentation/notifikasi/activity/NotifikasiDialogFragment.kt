package id.del.ac.delstat.presentation.notifikasi.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import id.del.ac.delstat.R
import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.model.notifikasi.Notifikasi
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.FragmentNotifikasiDialogBinding
import id.del.ac.delstat.presentation.activity.HomeActivity
import id.del.ac.delstat.presentation.analisisdata.activity.DetailAnalisisDataActivity
import id.del.ac.delstat.presentation.literatur.activity.DetailLiteraturActivity
import id.del.ac.delstat.presentation.notifikasi.adapter.NotifikasiAdapter
import id.del.ac.delstat.presentation.notifikasi.viewmodel.NotifikasiViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class NotifikasiDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentNotifikasiDialogBinding

    private lateinit var notifikasiAdapter: NotifikasiAdapter

    private lateinit var notifikasiViewModel: NotifikasiViewModel

    private lateinit var userPreferences: UserPreferences
    private lateinit var bearerToken: String

    lateinit var delStatApiService: DelStatApiService

    companion object {
        const val TAG = "NotifikasiDialogFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_notifikasi_dialog, container, false)
        binding = FragmentNotifikasiDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notifikasiViewModel = (activity as HomeActivity).notifikasiViewModel
        userPreferences = (activity as HomeActivity).userPreferences
        delStatApiService = (activity as HomeActivity).delStatApiService

        prepareUI()
        initRecyclerView()
        getNotifikasi()
    }

    /*override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentNotifikasiDialogBinding.inflate(LayoutInflater.from(context))
        return MaterialAlertDialogBuilder(requireActivity())
            .setView(binding.root)
            .create()
    }*/

    private fun prepareUI() {
        runBlocking {
            bearerToken = "Bearer " + userPreferences.getUserToken.first()!!
        }
        Log.d("MyTag", "bearerToken: $bearerToken")
    }

    private fun initRecyclerView() {
        notifikasiAdapter = NotifikasiAdapter() { notifikasi ->
            onClickNotifikasi(notifikasi)
        }
        binding.recyclerViewNotifikasiDialog.adapter = notifikasiAdapter
        binding.recyclerViewNotifikasiDialog.layoutManager = LinearLayoutManager(context)
    }

    private fun getNotifikasi() {
        notifikasiViewModel.getNotifiksai(bearerToken)
        displayNotifikasi()
    }

    private fun displayNotifikasi() {
        binding.notifikasiProgressbar.visibility = View.VISIBLE

        notifikasiViewModel.notifikasiApiResponse.observe(viewLifecycleOwner, Observer {
            if(it.code == 200 && it.notifikasi != null && it.notifikasi.isNotEmpty()) {
                notifikasiAdapter.setList(it.notifikasi)
                notifikasiAdapter.notifyDataSetChanged()
                binding.notifikasiProgressbar.visibility = View.GONE
            } else {
                binding.recyclerViewNotifikasiDialog.visibility = View.GONE
                binding.messageEmptyNotifikasiDialog.visibility = View.VISIBLE
                binding.notifikasiProgressbar.visibility = View.GONE
            }
        })
    }

    fun onClickNotifikasi(notifikasi: Notifikasi) {
        if(notifikasi.jenisNotifikasi == Notifikasi.NOTIFIKASI_LITERATUR_BARU) {
            startActivity(
                Intent(requireActivity(), DetailLiteraturActivity::class.java)
                    .putExtra(DetailLiteraturActivity.ID_LITERATUR, notifikasi.idLiteratur)
            )
        }

        if(notifikasi.jenisNotifikasi == Notifikasi.NOTIFIKASI_ANALISIS_DATA_BARU) {
            startActivity(
                Intent(requireActivity(), DetailAnalisisDataActivity::class.java)
                    .putExtra(DetailAnalisisDataActivity.DATA_ANALISIS_DATA, notifikasi.idAnalisisData)
            )
        }
    }

}