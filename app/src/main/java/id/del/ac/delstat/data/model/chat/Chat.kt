package id.del.ac.delstat.data.model.chat


import com.google.gson.annotations.SerializedName

data class Chat(
    @SerializedName("id")
    val id: Int,

    @SerializedName("id_chat_room")
    val idChatRoom: Int,

    @SerializedName("id_user")
    val idUser: Int,

    @SerializedName("pesan")
    val pesan: String? = null,

    @SerializedName("role")
    val role: String? = null,

    @SerializedName("read_at")
    val readAt: String? = null,

    @SerializedName("sudah_dibaca")
    val sudahDibaca: Boolean = false,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,
)