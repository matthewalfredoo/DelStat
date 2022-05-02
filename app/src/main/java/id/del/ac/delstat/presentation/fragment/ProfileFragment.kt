package id.del.ac.delstat.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import id.del.ac.delstat.R
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.FragmentProfileBinding
import id.del.ac.delstat.presentation.activity.HomeActivity
import id.del.ac.delstat.presentation.activity.MainActivity
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModel


class ProfileFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var fragmentProfileBinding: FragmentProfileBinding
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

        fragmentProfileBinding = FragmentProfileBinding.bind(view)
        userViewModel = (activity as HomeActivity).userViewModel
        userPreferences = (activity as HomeActivity).userPreferences

        userPreferences.getUserToken.asLiveData().observe(viewLifecycleOwner) {
            Log.d("MyTag", "ProfileFragment Token: $it")
            if (it.isNullOrEmpty()) {
                fragmentProfileBinding.containerNotLogin.visibility = View.VISIBLE
                fragmentProfileBinding.containerLogin.visibility = View.GONE
            } else {
                fragmentProfileBinding.containerLogin.visibility = View.VISIBLE
                fragmentProfileBinding.containerNotLogin.visibility = View.GONE
                bearerToken = it
            }
        }

        fragmentProfileBinding.buttonLogout.setOnClickListener {
            logout()
        }

        fragmentProfileBinding.buttonLogin.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    private fun logout() {
        bearerToken = "Bearer $bearerToken"
        userViewModel.logout(bearerToken!!)
    }

}