package id.del.ac.delstat.presentation.materi.fragment

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.davemorrissey.labs.subscaleview.ImageSource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialSharedAxis
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import id.del.ac.delstat.R
import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.model.materi.Materi
import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.CustomLayoutEditMateriDialogBinding
import id.del.ac.delstat.databinding.FragmentDistribusiPeluangDiskritBinding
import id.del.ac.delstat.presentation.activity.HomeActivity
import id.del.ac.delstat.presentation.materi.viewmodel.MateriViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class DistribusiPeluangDiskritFragment : Fragment() {
    private val IDMATERI = Materi.ID_MATERI_3_DISTRIBUSI_PROBABILITAS_DISKRIT

    private lateinit var binding: FragmentDistribusiPeluangDiskritBinding
    private lateinit var materiViewModel: MateriViewModel
    private lateinit var userPreferences: UserPreferences

    private lateinit var role: String
    private lateinit var bearerToken: String

    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var materialAlertDialogLayout: View
    private lateinit var bindingCustomLayout: CustomLayoutEditMateriDialogBinding

    private lateinit var linkVideo1: String
    private var linkVideo2: String = ""
    private var linkVideo3: String = ""
    private var linkVideo4: String = ""

    private lateinit var currentYoutubePlayerView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myEnterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        myEnterTransition.duration = 400

        val myReturnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        myReturnTransition.duration = 400

        enterTransition = myEnterTransition
        returnTransition = myReturnTransition
    }

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
        userPreferences = (activity as HomeActivity).userPreferences

        prepareUI()
    }

    private fun prepareUI() {
        materiViewModel.getMateri(IDMATERI)

        renderVideos()
        renderImages()

        runBlocking {
            role = userPreferences.getUserRole.first()!!
            if (role == User.ROLE_DOSEN || role == User.ROLE_ADMIN) {
                bearerToken = userPreferences.getUserToken.first()!!
                bearerToken = "Bearer $bearerToken"
                binding.buttonEditMateri.visibility = View.VISIBLE
                activateEditButton()
            }
        }
    }

    private fun renderVideos() {
        materiViewModel.materiApiResponse.observe(viewLifecycleOwner, Observer {
            if (it.code == 200 && it.materi != null) {
                linkVideo1 = it.materi.linkVideo1!!
                if (it.materi.linkVideo2 != "" && it.materi.linkVideo2 != null) {
                    linkVideo2 = it.materi.linkVideo2
                }
                if (it.materi.linkVideo3 != "" && it.materi.linkVideo3 != null) {
                    linkVideo3 = it.materi.linkVideo3
                }
                if (it.materi.linkVideo4 != "" && it.materi.linkVideo4 != null) {
                    linkVideo4 = it.materi.linkVideo4
                }
                youtubePlayers()
            }
        })
    }

    private fun activateEditButton() {
        binding.buttonEditMateri.setOnClickListener {
            // Handling the layout first
            materialAlertDialogLayout =
                layoutInflater.inflate(R.layout.custom_layout_edit_materi_dialog, null)
            bindingCustomLayout =
                CustomLayoutEditMateriDialogBinding.bind(materialAlertDialogLayout)
            prepareInputs()

            // Creating the dialog
            materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireActivity())
            materialAlertDialogBuilder.setTitle("Edit Materi")

            materialAlertDialogBuilder.setView(materialAlertDialogLayout)

            materialAlertDialogBuilder.setPositiveButton("Simpan") { dialog, _ ->
                if(checkInput()) {
                    materiViewModel.updateMateri(
                        bearerToken,
                        IDMATERI,
                        bindingCustomLayout.editTextLinkVideo1.text.toString(),
                        bindingCustomLayout.editTextLinkVideo2.text.toString(),
                        bindingCustomLayout.editTextLinkVideo3.text.toString(),
                        bindingCustomLayout.editTextLinkVideo4.text.toString()
                    )
                    renderVideos()
                    dialog.dismiss()
                }
            }

            materialAlertDialogBuilder.setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }

            materialAlertDialogBuilder.show()
        }
    }

    private fun checkInput(): Boolean {
        val validInput = bindingCustomLayout.textInputLayout1.error == null &&
                bindingCustomLayout.textInputLayout2.error == null &&
                bindingCustomLayout.textInputLayout3.error == null &&
                bindingCustomLayout.textInputLayout4.error == null

        return validInput
    }

    private fun prepareInputs() {
        if (linkVideo1 != "") {
            bindingCustomLayout.editTextLinkVideo1.setText("https://www.youtube.com/watch?v=$linkVideo1")
        }
        if (linkVideo2 != "") {
            bindingCustomLayout.editTextLinkVideo2.setText("https://www.youtube.com/watch?v=$linkVideo2")
        }
        if (linkVideo3 != "") {
            bindingCustomLayout.editTextLinkVideo3.setText("https://www.youtube.com/watch?v=$linkVideo3")
        }
        if (linkVideo4 != "") {
            bindingCustomLayout.editTextLinkVideo4.setText("https://www.youtube.com/watch?v=$linkVideo4")
        }

        bindingCustomLayout.editTextLinkVideo1.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                bindingCustomLayout.textInputLayout1.error =
                    "Field link video 1 wajib diisi, karena materi minimal memiliki 1 video"
            } else if (!Patterns.WEB_URL.matcher(text).matches()) {
                bindingCustomLayout.textInputLayout1.error = "Link video 1 tidak valid"
            } else {
                bindingCustomLayout.textInputLayout1.error = null
            }
        }

        bindingCustomLayout.editTextLinkVideo2.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                bindingCustomLayout.textInputLayout2.error = null
            } else if (!Patterns.WEB_URL.matcher(text).matches()) {
                bindingCustomLayout.textInputLayout2.error = "Link video 2 tidak valid"
            } else {
                bindingCustomLayout.textInputLayout2.error = null
            }
        }

        bindingCustomLayout.editTextLinkVideo3.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                bindingCustomLayout.textInputLayout3.error = null
            } else if (!Patterns.WEB_URL.matcher(text).matches()) {
                bindingCustomLayout.textInputLayout3.error = "Link video 3 tidak valid"
            } else {
                bindingCustomLayout.textInputLayout3.error = null
            }
        }

        bindingCustomLayout.editTextLinkVideo4.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                bindingCustomLayout.textInputLayout4.error = null
            } else if (!Patterns.WEB_URL.matcher(text).matches()) {
                bindingCustomLayout.textInputLayout4.error = "Link video 4 tidak valid"
            } else {
                bindingCustomLayout.textInputLayout4.error = null
            }
        }
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
            when(toggleButton.checkedButtonId) {
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
        binding.imageViewDistribusiPeluangDiskrit1.setImage(ImageSource.resource(R.drawable.materi_distribusi_peluang_diskrit_1))
        binding.imageViewDistribusiPeluangDiskrit2.setImage(ImageSource.resource(R.drawable.materi_distribusi_peluang_diskrit_2))
        binding.imageViewDistribusiPeluangDiskrit3.setImage(ImageSource.resource(R.drawable.materi_distribusi_peluang_diskrit_3))
        binding.imageViewDistribusiPeluangDiskrit4.setImage(ImageSource.resource(R.drawable.materi_distribusi_peluang_diskrit_4))
        binding.imageViewDistribusiPeluangDiskrit5.setImage(ImageSource.resource(R.drawable.materi_distribusi_peluang_diskrit_5))
        binding.imageViewDistribusiPeluangDiskrit6.setImage(ImageSource.resource(R.drawable.materi_distribusi_peluang_diskrit_6))
        binding.imageViewDistribusiPeluangDiskrit7a.setImage(ImageSource.resource(R.drawable.materi_distribusi_peluang_diskrit_7a))
        binding.imageViewDistribusiPeluangDiskrit7b.setImage(ImageSource.resource(R.drawable.materi_distribusi_peluang_diskrit_7b))
        binding.imageViewDistribusiPeluangDiskrit8.setImage(ImageSource.resource(R.drawable.materi_distribusi_peluang_diskrit_8))
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