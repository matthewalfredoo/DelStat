package id.del.ac.delstat.presentation.chat.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.databinding.ActivityDetailChatRoomBinding
import id.del.ac.delstat.presentation.chat.adapter.ChatRoomAdapter
import id.del.ac.delstat.presentation.chat.viewmodel.ChatViewModel
import id.del.ac.delstat.presentation.chat.viewmodel.ChatViewModelFactory
import id.del.ac.delstat.util.Helper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class DetailChatRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailChatRoomBinding

    private var chatRoomId: Int = -1
    private var message: String? = ""

    @Inject
    lateinit var chatViewModelFactory: ChatViewModelFactory
    private lateinit var chatViewModel: ChatViewModel
    lateinit var runGetChats: Runnable
    var handlerRunGetChats: Handler = Handler(Looper.getMainLooper())
    val delayInterval: Long = 4000

    @Inject
    lateinit var userPreferences: UserPreferences
    private lateinit var bearerToken: String
    private var loggedInUserId: Int = -1
    private var mustScrollToLatest: Boolean = true
    private var chatSize: Int = 0
    private var chatSizePrev: Int = 0

    lateinit var chatRoomAdapter: ChatRoomAdapter

    @Inject
    lateinit var delStatApiService: DelStatApiService

    companion object {
        const val EXTRA_CHAT_ROOM_ID = "EXTRA_CHAT_ROOM_ID"
        const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatViewModel = ViewModelProvider(this, chatViewModelFactory)
            .get(ChatViewModel::class.java)

        prepareUI()
        initRecyclerView()
        getChats()
        handleGetChatsPeriodically()
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
        chatRoomAdapter = ChatRoomAdapter(loggedInUserId)
        binding.recyclerViewChat.adapter = chatRoomAdapter
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(this)
    }

    private fun getChats() {
        chatViewModel.getChatRoom(bearerToken, chatRoomId)
        displayChats()
    }

    private fun displayChats() {
        binding.chatProgressbar.visibility = View.VISIBLE

        chatViewModel.chatApiResponse.observe(this, Observer {
            if (it.code == 200 && it.chatRoom != null && it.chats != null) {
                Log.d("MyTag", "${chatSize}")
                supportActionBar?.title = it.chatRoom.user.nama

                chatRoomAdapter.setList(it.chats)
                chatRoomAdapter.notifyDataSetChanged()

                // scroll down to the last message in the list
                chatSize = it.chats.size - 1

                scrollChat()

                binding.chatProgressbar.visibility = View.GONE
            }

            if (it.code != 200 && it.message != null) {
                binding.chatProgressbar.visibility = View.GONE
                Snackbar.make(binding.root, it.message, Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun scrollChat() {
        if(mustScrollToLatest && chatSizePrev != chatSize) {
            binding.recyclerViewChat.scrollToPosition(chatSize)
            chatSizePrev = chatSize // chatSize will always change dynamically since it is updated in the displayChats() function using LiveData observer
            if(chatSizePrev == chatSize) {
                mustScrollToLatest = false
            }
        }
    }

    private fun handleGetChatsPeriodically() {
        handlerRunGetChats.postDelayed(Runnable {
            handlerRunGetChats.postDelayed(runGetChats, delayInterval)
            getChats()
        }.also { runGetChats = it }, delayInterval)
    }

    private fun prepareUI() {
        chatRoomId = intent.getIntExtra(EXTRA_CHAT_ROOM_ID, -1)
        if (chatRoomId == -1) {
            /*Snackbar.make(
                binding.root,
                "Terjadi kesalahan saat mengakses chat",
                Snackbar.LENGTH_SHORT
            ).show()*/
            Helper.createSnackbar(binding.root, "Terjadi kesalahan saat mengakses chat").show()
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 800)
            finish()
        }

        message = intent.getStringExtra(EXTRA_MESSAGE)
        if (!message.isNullOrEmpty()) {
            Snackbar.make(binding.root, message!!, Snackbar.LENGTH_LONG).show()
        }

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        runBlocking {
            loggedInUserId = userPreferences.getUserId.first()!!
            bearerToken = userPreferences.getUserToken.first()!!
            bearerToken = "Bearer $bearerToken"
            Log.d("MyTag", "Bearer Token: $bearerToken")
        }

        inputValidations()

        binding.buttonKirim.setOnClickListener {
            sendMessage()
        }

        binding.swipeRefreshData.setOnRefreshListener {
            getChats()
            binding.swipeRefreshData.isRefreshing = false
            binding.recyclerViewChat.scrollToPosition(chatSize)
        }
    }

    private fun inputValidations() {
        binding.editTextChat.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                binding.buttonKirim.visibility = View.GONE
            } else {
                binding.buttonKirim.visibility = View.VISIBLE
            }
        }
    }

    private fun sendMessage() {
        val message = binding.editTextChat.text.toString()
        if (message.isNotEmpty()) {
            binding.editTextChat.text?.clear()
            chatViewModel.storeChat(bearerToken, chatRoomId, message)
        }
        mustScrollToLatest = true
        getChats()
        while(chatSizePrev != chatSize) {
            binding.recyclerViewChat.scrollToPosition(chatSize)
            if(chatSizePrev == chatSize) {
                break
            }
        }
    }
}