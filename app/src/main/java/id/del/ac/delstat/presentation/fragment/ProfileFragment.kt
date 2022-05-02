package id.del.ac.delstat.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import id.del.ac.delstat.R
import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.model.user.UserApiResponse
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.FragmentProfileBinding
import id.del.ac.delstat.presentation.activity.HomeActivity
import id.del.ac.delstat.presentation.activity.MainActivity
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class ProfileFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var fragmentProfileBinding: FragmentProfileBinding
    private lateinit var userPreferences: UserPreferences

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

        fragmentProfileBinding.buttonLogout.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
        }
    }

}