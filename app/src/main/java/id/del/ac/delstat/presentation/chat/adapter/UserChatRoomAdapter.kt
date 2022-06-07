package id.del.ac.delstat.presentation.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.del.ac.delstat.BuildConfig
import id.del.ac.delstat.R
import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.databinding.ListItemChatroomBinding

// This adapter is used for recyclerview to show list of user that can be chat with
class UserChatRoomAdapter(
    private val clickListener: (User) -> Unit,
    private val imageProfileClickListener: (ImageView, String) -> Unit
): RecyclerView.Adapter<MyUserChatRoomViewHolder>() {
    private val listUser = ArrayList<User>()

    fun setList(user: List<User>) {
        listUser.clear()
        listUser.addAll(user)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyUserChatRoomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemChatroomBinding.inflate(layoutInflater, parent, false)

        return MyUserChatRoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyUserChatRoomViewHolder, position: Int) {
        holder.bind(listUser[position], clickListener, imageProfileClickListener)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

}

class MyUserChatRoomViewHolder(val binding: ListItemChatroomBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User, clickListener: (User) -> Unit, imageProfileClickListener: (ImageView, String) -> Unit) {
        binding.namaUserChat.text = user.nama
        binding.tanggalTerakhirChat.text = user.email

        val fotoProfilUrl = BuildConfig.BASE_URL + user.fotoProfil
        Glide.with(binding.root.context)
            .load(fotoProfilUrl)
            .error(R.drawable.ic_default_foto_profil)
            .into(binding.fotoProfilUserChat)

        binding.root.setOnClickListener {
            clickListener(user)
        }
        binding.fotoProfilUserChat.setOnClickListener {
            imageProfileClickListener(binding.fotoProfilUserChat, fotoProfilUrl)
        }
    }

}