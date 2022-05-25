package id.del.ac.delstat.presentation.chat.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.data.model.chat.ChatRoom
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.ActivityListChatRoomBinding
import id.del.ac.delstat.presentation.activity.LoginActivity
import id.del.ac.delstat.presentation.chat.adapter.ListChatRoomAdapter
import id.del.ac.delstat.presentation.chat.viewmodel.ChatViewModel
import id.del.ac.delstat.presentation.chat.viewmodel.ChatViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class ListChatRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListChatRoomBinding

    @Inject
    lateinit var chatViewModelFactory: ChatViewModelFactory
    private lateinit var chatViewModel: ChatViewModel

    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var bearerToken: String

    lateinit var listChatRoomAdapter: ListChatRoomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatViewModel = ViewModelProvider(this, chatViewModelFactory)
            .get(ChatViewModel::class.java)

        prepareUI()
        initRecyclerView()
        getChatRooms()
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
        listChatRoomAdapter = ListChatRoomAdapter(){ chatRoom ->
            onChatRoomClickListener(chatRoom)
        }
        binding.recyclerViewChat.adapter = listChatRoomAdapter
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(this)
    }

    private fun onChatRoomClickListener(chatRoom: ChatRoom){
        startActivity(
            Intent(this, DetailChatRoomActivity::class.java)
                .putExtra(DetailChatRoomActivity.EXTRA_CHAT_ROOM_ID, chatRoom.id)
        )
    }

    private fun getChatRooms() {
        chatViewModel.getChatRooms(bearerToken)
        displayChatRooms()
    }

    private fun displayChatRooms() {
        binding.chatProgressbar.visibility = View.VISIBLE

        chatViewModel.chatApiResponse.observe(this, Observer {
            if(it.code == 200 && it.listChatRoom != null) {
                listChatRoomAdapter.setList(it.listChatRoom)
                listChatRoomAdapter.notifyDataSetChanged()
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
            if(bearerToken.isEmpty()) {
                startActivity(
                    Intent(this@ListChatRoomActivity, LoginActivity::class.java)
                        .putExtra(LoginActivity.LOGIN_MESSAGE, "Login untuk mengakses menu chat")
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                )
                finish()
            }
            bearerToken = "Bearer $bearerToken"
        }

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chat"

        binding.buttonChat.setOnClickListener {
            startActivity(
                Intent(this, CreateChatRoomActivity::class.java)
            )
        }

        binding.swipeRefreshData.setOnRefreshListener {
            getChatRooms()
            binding.swipeRefreshData.isRefreshing = false
        }
    }
}