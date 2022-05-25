package id.del.ac.delstat.presentation.chat.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import id.del.ac.delstat.R
import id.del.ac.delstat.databinding.ActivityDetailChatRoomBinding

class DetailChatRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailChatRoomBinding

    private var chatRoomId: Int = -1
    private var message: String? = ""

    companion object{
        const val EXTRA_CHAT_ROOM_ID = "EXTRA_CHAT_ROOM_ID"
        const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareUI()
    }

    private fun prepareUI() {
        chatRoomId = intent.getIntExtra(EXTRA_CHAT_ROOM_ID, -1)
        message = intent.getStringExtra(EXTRA_MESSAGE)

        if(!message.isNullOrEmpty()) {
            Snackbar.make(binding.root, message!!, Snackbar.LENGTH_LONG).show()
        }
    }
}