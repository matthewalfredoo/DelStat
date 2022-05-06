package id.del.ac.delstat.presentation.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.del.ac.delstat.R
import id.del.ac.delstat.databinding.FragmentMateriBinding
import id.del.ac.delstat.presentation.testactivity.MateriActivity

class MateriFragment : Fragment() {

    private lateinit var binding: FragmentMateriBinding

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
        binding.tvTest.setOnClickListener {
            startActivity(Intent(activity, MateriActivity::class.java))
        }
    }

}