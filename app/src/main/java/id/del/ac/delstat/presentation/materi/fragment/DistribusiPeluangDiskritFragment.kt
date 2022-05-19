package id.del.ac.delstat.presentation.materi.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.del.ac.delstat.R
import id.del.ac.delstat.databinding.FragmentDistribusiPeluangDiskritBinding

class DistribusiPeluangDiskritFragment : Fragment() {
    private lateinit var binding: FragmentDistribusiPeluangDiskritBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_distribusi_peluang_diskrit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentDistribusiPeluangDiskritBinding.bind(view)

        prepareUI()
    }

    private fun prepareUI() {

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.youtubePlayerView.release()
    }

}