package id.del.ac.delstat.data.repository.chat.datasource

import id.del.ac.delstat.data.model.chat.ChatApiResponse
import retrofit2.Response

interface ChatRemoteDataSource {

    suspend fun getChatRooms(
        bearerToken: String
    ): Response<ChatApiResponse>

    suspend fun getChatRoom(
        bearerToken: String,
        id: Int
    ): Response<ChatApiResponse>

    suspend fun storeChatRoom(
        bearerToken: String,
        idUser: Int
    ): Response<ChatApiResponse>

    suspend fun storeChat(
        bearerToken: String,
        id: Int,
        pesan: String
    ): Response<ChatApiResponse>

    suspend fun updateChatRoom(
        bearerToken: String,
        id: Int,
        isAutomaticDeleted: Int
    ): Response<ChatApiResponse>

}