package id.del.ac.delstat.data.model.chat


import com.google.gson.annotations.SerializedName
import id.del.ac.delstat.data.model.user.User

data class ChatRoom(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("id_user_1")
    val idUser1: Int? = null,

    @SerializedName("id_user_2")
    val idUser2: Int? = null,

    @SerializedName("is_automatic_deleted")
    val isAutomaticDeleted: Boolean = true,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("last_edited_at")
    val lastEditedAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    @SerializedName("user")
    val user: User
)