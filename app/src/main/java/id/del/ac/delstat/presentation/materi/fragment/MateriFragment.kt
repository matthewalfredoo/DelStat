package id.del.ac.delstat.presentation.materi.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import id.del.ac.delstat.R
import id.del.ac.delstat.databinding.FragmentMateriBinding
import id.del.ac.delstat.presentation.testactivity.MateriActivity

class MateriFragment : Fragment() {

    private lateinit var binding: FragmentMateriBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myExitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        myExitTransition.duration = 200

        val myReenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        myReenterTransition.duration = 500

        enterTransition = myReenterTransition
        exitTransition = myExitTransition
        reenterTransition = myReenterTransition
        returnTransition = myExitTransition
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_materi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMateriBinding.bind(view)

        prepareUI()
    }

    private fun prepareUI() {
        binding.cardviewMateri1.setOnClickListener {
            findNavController().navigate(R.id.action_materiFragment_to_konsepPeluangFragment)
        }
        binding.cardviewMateri2.setOnClickListener {
            findNavController().navigate(R.id.action_materiFragment_to_variabelAcakFragment)
        }
        binding.cardviewMateri3.setOnClickListener {
            findNavController().navigate(R.id.action_materiFragment_to_distribusiPeluangDiskritFragment)
        }
        binding.cardviewMateri4.setOnClickListener {
            findNavController().navigate(R.id.action_materiFragment_to_distribusiKontinuFragment)
        }
        binding.cardviewMateri5.setOnClickListener {
            findNavController().navigate(R.id.action_materiFragment_to_pengantarStatistikAnalisisDataFragment)
        }
        binding.cardviewMateri6.setOnClickListener {
            findNavController().navigate(R.id.action_materiFragment_to_teknikSamplingFragment)
        }
        binding.cardviewMateri7.setOnClickListener {
            findNavController().navigate(R.id.action_materiFragment_to_anovaFragment)
        }
        binding.cardviewMateri8.setOnClickListener {
            findNavController().navigate(R.id.action_materiFragment_to_estimasiSatuDanDuaSampelFragment)
        }
        binding.cardviewMateri9.setOnClickListener {
            findNavController().navigate(R.id.action_materiFragment_to_pengujianHipotesisFragment)
        }
        binding.cardviewMateri10.setOnClickListener {
            findNavController().navigate(R.id.action_materiFragment_to_regresiKorelasiFragment)
        }
    }

}