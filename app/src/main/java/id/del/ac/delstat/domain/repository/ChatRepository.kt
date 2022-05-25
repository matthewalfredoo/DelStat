package id.del.ac.delstat.domain.repository

import id.del.ac.delstat.data.model.chat.ChatApiResponse

interface ChatRepository {

    suspend fun getChatRooms(
        bearerToken: String
    ): ChatApiResponse?

    suspend fun getChatRoom(
        bearerToken: String,
        id: Int
    ): ChatApiResponse?

    suspend fun storeChatRoom(
        bearerToken: String,
        idUser: Int
    ): ChatApiResponse?

    suspend fun storeChat(
        bearerToken: String,
        id: Int,
        pesan: String
    ): ChatApiResponse?

    suspend fun updateChatRoom(
        bearerToken: String,
        id: Int,
        isAutomaticDeleted: Int
    ): ChatApiResponse?

}