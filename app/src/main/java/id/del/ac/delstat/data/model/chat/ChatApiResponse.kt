package id.del.ac.delstat.data.model.chat


import com.google.gson.annotations.SerializedName

data class ChatApiResponse(
    @SerializedName("code")
    val code: Int = 500,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("listChatRoom")
    val listChatRoom: List<ChatRoom>? = null,

    @SerializedName("chatRoom")
    val chatRoom: ChatRoom? = null,

    @SerializedName("chats")
    val chats: List<Chat>? = null
)