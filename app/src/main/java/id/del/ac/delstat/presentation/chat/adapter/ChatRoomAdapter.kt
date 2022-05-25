package id.del.ac.delstat.presentation.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.del.ac.delstat.R
import id.del.ac.delstat.data.model.chat.Chat
import id.del.ac.delstat.databinding.ItemChatInBinding
import id.del.ac.delstat.databinding.ItemChatOutBinding
import id.del.ac.delstat.util.DateUtil

class ChatRoomAdapter(
    private val loggedInUserId: Int
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val listChat = ArrayList<Chat>()

    private val CHAT_TYPE_IN = 1
    private val CHAT_TYPE_OUT = 2

    fun setList(chats: List<Chat>) {
        listChat.clear()
        listChat.addAll(chats)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        if(viewType == CHAT_TYPE_IN) {
            val binding = ItemChatInBinding.inflate(layoutInflater, parent, false)
            return MyChatInViewHolder(binding)
        } else {
            val binding = ItemChatOutBinding.inflate(layoutInflater, parent, false)
            return MyChatOutViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // If the current position of listChat has the same idUser as loggedInUserId, then it's a chat from the loggedInUser
        // Which means that chat is going out to the other user
        // And if it's not the same, then it's a chat from the other user
        // Which means that chat is going to the loggedInUser
        if(listChat[position].idUser == loggedInUserId){
            (holder as MyChatOutViewHolder).bind(listChat[position])
        } else {
            (holder as MyChatInViewHolder).bind(listChat[position])
        }
    }

    override fun getItemCount(): Int {
        return listChat.size
    }

    override fun getItemViewType(position: Int): Int {
        if(listChat[position].idUser == loggedInUserId){
            return CHAT_TYPE_OUT
        } else {
            return CHAT_TYPE_IN
        }
    }

}

class MyChatOutViewHolder(val binding: ItemChatOutBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(chat: Chat) {
        binding.chatText.text = chat.pesan
        binding.dateText.text = DateUtil.getDateTimeWithoutSecond(chat.createdAt!!)
        if(chat.sudahDibaca) {
            binding.statusReadText.setText(R.string.message_read)
        }
    }

}

class MyChatInViewHolder(val binding: ItemChatInBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(chat: Chat) {
        binding.chatText.text = chat.pesan
        binding.dateText.text = DateUtil.getDateTimeWithoutSecond(chat.createdAt!!)
    }

}