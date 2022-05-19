package id.del.ac.delstat.presentation.materi.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.davemorrissey.labs.subscaleview.ImageSource
import com.google.android.material.snackbar.Snackbar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import id.del.ac.delstat.R
import id.del.ac.delstat.databinding.FragmentDistribusiPeluangDiskritBinding
import id.del.ac.delstat.presentation.activity.HomeActivity
import id.del.ac.delstat.presentation.materi.viewmodel.MateriViewModel


class DistribusiPeluangDiskritFragment : Fragment() {
    private val IDMATERI = 3

    private lateinit var binding: FragmentDistribusiPeluangDiskritBinding
    private lateinit var materiViewModel: MateriViewModel

    private lateinit var linkVideo1: String
    private var linkVideo2: String = ""
    private var linkVideo3: String = ""
    private var linkVideo4: String = ""

    private lateinit var currentYoutubePlayerView: YouTubePlayerView

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
        materiViewModel = (activity as HomeActivity).materiViewModel

        prepareUI()
    }

    private fun prepareUI() {
        materiViewModel.getMateri(IDMATERI)

        materiViewModel.materiApiResponse.observe(viewLifecycleOwner, Observer {
            Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_SHORT).show()
            if(it.code == 200 && it.materi != null) {
                linkVideo1 = it.materi.linkVideo1!!
                if(it.materi.linkVideo2 != "" && it.materi.linkVideo2 != null) {
                    linkVideo2 = it.materi.linkVideo2
                }
                if(it.materi.linkVideo3 != "" && it.materi.linkVideo3 != null) {
                    linkVideo3 = it.materi.linkVideo3
                }
                if(it.materi.linkVideo4 != "" && it.materi.linkVideo4 != null) {
                    linkVideo4 = it.materi.linkVideo4
                }
                youtubePlayers()
            }
        })


        renderImages()
    }

    private fun youtubePlayers() {
        viewLifecycleOwner.lifecycle.addObserver(binding.youtubePlayerView1)

        binding.youtubePlayerView1.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(linkVideo1, 0f)
            }
        })
        currentYoutubePlayerView = binding.youtubePlayerView1

        if(linkVideo2 != "") {
            binding.button2.visibility = View.VISIBLE

            viewLifecycleOwner.lifecycle.addObserver(binding.youtubePlayerView2)
            binding.youtubePlayerView2.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(linkVideo2, 0f)
                }
            })
        }
        if(linkVideo3 != "") {
            binding.button3.visibility = View.VISIBLE

            viewLifecycleOwner.lifecycle.addObserver(binding.youtubePlayerView3)
            binding.youtubePlayerView3.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(linkVideo3, 0f)
                }
            })
        }
        if(linkVideo4 != "") {
            binding.button4.visibility = View.VISIBLE

            viewLifecycleOwner.lifecycle.addObserver(binding.youtubePlayerView4)
            binding.youtubePlayerView4.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(linkVideo4, 0f)
                }
            })
        }

        binding.toggleButton.visibility = View.VISIBLE
        binding.toggleButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            when(checkedId) {
                binding.button1.id -> {
                    currentYoutubePlayerView.visibility = View.GONE
                    currentYoutubePlayerView = binding.youtubePlayerView1
                    currentYoutubePlayerView.visibility = View.VISIBLE
                }
                binding.button2.id -> {
                    currentYoutubePlayerView.visibility = View.GONE
                    currentYoutubePlayerView = binding.youtubePlayerView2
                    currentYoutubePlayerView.visibility = View.VISIBLE
                }
                binding.button3.id -> {
                    currentYoutubePlayerView.visibility = View.GONE
                    currentYoutubePlayerView = binding.youtubePlayerView3
                    currentYoutubePlayerView.visibility = View.VISIBLE
                }
                binding.button4.id -> {
                    currentYoutubePlayerView.visibility = View.GONE
                    currentYoutubePlayerView = binding.youtubePlayerView4
                    currentYoutubePlayerView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun renderImages() {
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
        binding.youtubePlayerView1.release()
        binding.youtubePlayerView2.release()
        binding.youtubePlayerView3.release()
        binding.youtubePlayerView4.release()
    }

}