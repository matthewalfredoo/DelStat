package id.del.ac.delstat.presentation.chat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.ActivityCreateChatRoomBinding
import id.del.ac.delstat.presentation.chat.adapter.UserChatRoomAdapter
import id.del.ac.delstat.presentation.chat.viewmodel.ChatViewModel
import id.del.ac.delstat.presentation.chat.viewmodel.ChatViewModelFactory
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModel
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class CreateChatRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateChatRoomBinding

    @Inject
    lateinit var chatViewModelFactory: ChatViewModelFactory
    private lateinit var chatViewModel: ChatViewModel

    @Inject
    lateinit var userViewModelFactory: UserViewModelFactory
    private lateinit var userViewModel: UserViewModel

    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var bearerToken: String

    lateinit var userChatRoomAdapter: UserChatRoomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatViewModel = ViewModelProvider(this, chatViewModelFactory)
            .get(ChatViewModel::class.java)

        userViewModel = ViewModelProvider(this, userViewModelFactory)
            .get(UserViewModel::class.java)

        prepareUI()
        initRecyclerView()
        getUserChatRoom()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun initRecyclerView() {
        userChatRoomAdapter = UserChatRoomAdapter(){ user ->
            onClickUserChatRoom(user)
        }
        binding.recyclerViewChat.adapter = userChatRoomAdapter
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(this)
    }

    private fun onClickUserChatRoom(user: User) {
        chatViewModel.storeChatRoom(bearerToken, user.id!!)
    }

    private fun getUserChatRoom() {
        userViewModel.findUsersByRole(bearerToken)
        displayUserChatRoom()
    }

    private fun displayUserChatRoom() {
        binding.chatProgressbar.visibility = View.VISIBLE

        userViewModel.userApiResponse.observe(this, Observer {
            if(it.code == 200 && it.listUser != null) {
                userChatRoomAdapter.setList(it.listUser)
                userChatRoomAdapter.notifyDataSetChanged()
                binding.chatProgressbar.visibility = View.GONE
            } else {
                binding.chatProgressbar.visibility = View.GONE
                Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun prepareUI() {
        runBlocking {
            bearerToken = userPreferences.getUserToken.first()!!
            bearerToken = "Bearer $bearerToken"
        }

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chat Baru"

        chatViewModel.chatApiResponse.observe(this, Observer {
            Log.d("ChatRoom", it.toString())
            /*Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_LONG).show()*/

            if(it.code == 200 && it.chatRoom != null) {
                startActivity(
                    Intent(this, DetailChatRoomActivity::class.java)
                        .putExtra(DetailChatRoomActivity.EXTRA_CHAT_ROOM_ID, it.chatRoom.id)
                )
                finish()
            }
            if(it.code == 400 && it.chatRoom != null) {
                startActivity(
                    Intent(this, DetailChatRoomActivity::class.java)
                        .putExtra(DetailChatRoomActivity.EXTRA_CHAT_ROOM_ID, it.chatRoom.id)
                        .putExtra(DetailChatRoomActivity.EXTRA_MESSAGE, it.message)
                )
                finish()
            }
        })

        binding.swipeRefreshData.setOnRefreshListener {
            getUserChatRoom()
            binding.swipeRefreshData.isRefreshing = false
        }
    }
}