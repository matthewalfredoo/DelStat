package id.del.ac.delstat.presentation.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.del.ac.delstat.R
import id.del.ac.delstat.databinding.FragmentLiteraturBinding
import id.del.ac.delstat.presentation.testactivity.LiteraturActivity

class LiteraturFragment : Fragment() {
    private lateinit var binding: FragmentLiteraturBinding

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

        prepareUI()
    }

    private fun prepareUI() {
        binding.tvLiteratur.setOnClickListener {
            startActivity(Intent(activity, LiteraturActivity::class.java))
        }
    }

}