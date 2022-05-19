package id.del.ac.delstat.presentation.materi.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import com.davemorrissey.labs.subscaleview.ImageSource
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
        binding.imageViewDistribusiPeluangDiskrit1.setImage(ImageSource.resource(R.drawable.distribusi_peluang_diskrit_1))
        binding.imageViewDistribusiPeluangDiskrit2.setImage(ImageSource.resource(R.drawable.distribusi_peluang_diskrit_2))
        binding.imageViewDistribusiPeluangDiskrit3.setImage(ImageSource.resource(R.drawable.distribusi_peluang_diskrit_3))
        binding.imageViewDistribusiPeluangDiskrit4.setImage(ImageSource.resource(R.drawable.distribusi_peluang_diskrit_4))
        binding.imageViewDistribusiPeluangDiskrit5.setImage(ImageSource.resource(R.drawable.distribusi_peluang_diskrit_5))
        binding.imageViewDistribusiPeluangDiskrit6.setImage(ImageSource.resource(R.drawable.distribusi_peluang_diskrit_6))
        binding.imageViewDistribusiPeluangDiskrit7a.setImage(ImageSource.resource(R.drawable.distribusi_peluang_diskrit_7a))
        binding.imageViewDistribusiPeluangDiskrit7b.setImage(ImageSource.resource(R.drawable.distribusi_peluang_diskrit_7b))
        binding.imageViewDistribusiPeluangDiskrit8.setImage(ImageSource.resource(R.drawable.distribusi_peluang_diskrit_8))
    }

    /*private fun renderMathViews() {
        val formula1 = "x_{i\\ }=data\\ ke-i\\\\k = banyaknya \\space data"
        binding.mathView1.formula = formula1
        binding.mathView1.zoomOut()
    }*/

    override fun onDestroy() {
        super.onDestroy()
        binding.youtubePlayerView.release()
    }

}