package id.del.ac.delstat.presentation.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.del.ac.delstat.BuildConfig
import id.del.ac.delstat.R
import id.del.ac.delstat.data.model.chat.ChatRoom
import id.del.ac.delstat.databinding.ListItemChatroomBinding
import id.del.ac.delstat.util.DateUtil

class ListChatRoomAdapter(
    private val clickListener: (ChatRoom) -> Unit
): RecyclerView.Adapter<MyChatRoomViewHolder>() {
    private val listChatRoom = ArrayList<ChatRoom>()

    fun setList(chatRoom: List<ChatRoom>) {
        listChatRoom.clear()
        listChatRoom.addAll(chatRoom)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyChatRoomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemChatroomBinding.inflate(layoutInflater, parent, false)

        return MyChatRoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyChatRoomViewHolder, position: Int) {
        holder.bind(listChatRoom[position], clickListener)
    }

    override fun getItemCount(): Int {
        return listChatRoom.size
    }

}

class MyChatRoomViewHolder(val binding: ListItemChatroomBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(chatRoom: ChatRoom, clickListener: (ChatRoom) -> Unit) {
        binding.namaUserChat.text = chatRoom.user.nama
        binding.tanggalTerakhirChat.text = DateUtil.getDateTime(chatRoom.updatedAt!!)

        val fotoProfilUrl = BuildConfig.BASE_URL + chatRoom.user.fotoProfil
        Glide.with(binding.root.context)
            .load(fotoProfilUrl)
            .error(R.drawable.ic_default_foto_profil)
            .into(binding.fotoProfilUserChat)

        binding.root.setOnClickListener {
            clickListener(chatRoom)
        }
    }

}