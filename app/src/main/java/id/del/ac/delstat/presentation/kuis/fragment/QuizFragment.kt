package id.del.ac.delstat.presentation.kuis.fragment

import android.content.ClipData.newIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import id.del.ac.delstat.R
import id.del.ac.delstat.data.model.kuis.Kuis
import id.del.ac.delstat.data.model.kuis.KumpulanKuis
import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.FragmentQuizBinding
import id.del.ac.delstat.presentation.activity.HomeActivity
import id.del.ac.delstat.presentation.activity.LoginActivity
import id.del.ac.delstat.presentation.kuis.activity.HasilKuisActivity
import id.del.ac.delstat.presentation.kuis.activity.KuisActivity
import id.del.ac.delstat.presentation.kuis.adapter.KuisAdapter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class QuizFragment : Fragment() {
    private lateinit var binding: FragmentQuizBinding

    private lateinit var kuisAdapter: KuisAdapter

    private lateinit var userPreferences: UserPreferences
    private var bearerToken: String? = null
    private var role: String? = null

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
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = (activity as HomeActivity).userPreferences
        binding = FragmentQuizBinding.bind(view)

        prepareData()
        initRecyclerView()
    }

    private fun prepareData() {
        runBlocking {
            bearerToken = userPreferences.getUserToken.first()!!
            role = userPreferences.getUserRole.first()!!
        }
    }

    private fun initRecyclerView() {
        kuisAdapter = KuisAdapter() { kuis: Kuis ->
            onClickKuis(kuis)
        }
        binding.recyclerViewKuis.adapter = kuisAdapter
        binding.recyclerViewKuis.layoutManager = LinearLayoutManager(context)
        displayKuis()
    }

    private fun displayKuis() {
        kuisAdapter.setList(KumpulanKuis.kumpulanKuis)
        kuisAdapter.notifyDataSetChanged()
    }

    private fun onClickKuis(kuis: Kuis) {
        if (!bearerToken.isNullOrEmpty()) {
            if(role == User.ROLE_ADMIN || role == User.ROLE_DOSEN) {
                startActivity(
                    Intent(requireActivity(), HasilKuisActivity::class.java)
                )
                return
            }
            startActivity(
                Intent(requireActivity(), KuisActivity::class.java)
                    .putExtra(KuisActivity.EXTRA_ID_KUIS, kuis.id)
            )
        } else {
            startActivity(
                Intent(requireActivity(), LoginActivity::class.java)
                    .putExtra(LoginActivity.LOGIN_MESSAGE, "Login untuk mengakses kuis")
            )
        }
    }

}