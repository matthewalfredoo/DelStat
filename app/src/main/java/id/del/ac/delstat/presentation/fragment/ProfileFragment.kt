package id.del.ac.delstat.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import id.del.ac.delstat.BuildConfig
import id.del.ac.delstat.R
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.FragmentProfileBinding
import id.del.ac.delstat.presentation.activity.HomeActivity
import id.del.ac.delstat.presentation.activity.LoginActivity
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModel


class ProfileFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var userPreferences: UserPreferences
    private var bearerToken: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProfileBinding.bind(view)
        userViewModel = (activity as HomeActivity).userViewModel
        userPreferences = (activity as HomeActivity).userPreferences

        userPreferences.getUserToken.asLiveData().observe(viewLifecycleOwner) {
            // Log.d("MyTag", "ProfileFragment Token: $it")
            if (it.isNullOrEmpty()) {
                binding.containerNotLogin.visibility = View.VISIBLE
                binding.containerLogin.visibility = View.GONE
            } else {
                binding.containerLogin.visibility = View.VISIBLE
                binding.containerNotLogin.visibility = View.GONE
                bearerToken = it
            }
        }

        prepareUI()
    }

    private fun logout() {
        bearerToken = "Bearer $bearerToken"
        userViewModel.logout(bearerToken!!)
    }

    private fun prepareUI() {
        userPreferences.getUserNama.asLiveData().observe(viewLifecycleOwner) {
            binding.textViewNamaProfile.text = it
        }
        userPreferences.getUserEmail.asLiveData().observe(viewLifecycleOwner) {
            binding.textViewEmailProfile.text = it
        }
        userPreferences.getUserNoHp.asLiveData().observe(viewLifecycleOwner) {
            binding.textViewNoHpProfile.text = it
        }
        userPreferences.getUserJenjang.asLiveData().observe(viewLifecycleOwner) {
            binding.textViewJenjangProfile.text = it
        }
        userPreferences.getUserFotoProfil.asLiveData().observe(viewLifecycleOwner) {
            val fotoProfilUrl = BuildConfig.BASE_URL + it
            Log.d("MyTag", "FotoProfilUrl: $fotoProfilUrl")
            Glide.with(requireActivity().applicationContext)
                .load(fotoProfilUrl)
                .into(binding.imageViewProfile)
        }

        binding.buttonLogin.setOnClickListener {
            // Log.d("MyTag", "Button Login Clicked")
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        binding.buttonLogout.setOnClickListener {
            logout()
        }

        binding.buttonEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
    }

}