package id.del.ac.delstat.data.repository.chat.datasourceimpl

import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.model.chat.ChatApiResponse
import id.del.ac.delstat.data.repository.chat.datasource.ChatRemoteDataSource
import retrofit2.Response

class ChatRemoteDataSourceImpl(
    private val delStatApiService: DelStatApiService
): ChatRemoteDataSource {

    override suspend fun getChatRooms(bearerToken: String): Response<ChatApiResponse> {
        return delStatApiService.getChatRooms(bearerToken)
    }

    override suspend fun getChatRoom(bearerToken: String, id: Int): Response<ChatApiResponse> {
        return delStatApiService.getChatRoom(bearerToken, id)
    }

    override suspend fun storeChatRoom(
        bearerToken: String,
        idUser: Int
    ): Response<ChatApiResponse> {
        return delStatApiService.storeChatRoom(bearerToken, idUser)
    }

    override suspend fun storeChat(
        bearerToken: String,
        id: Int,
        pesan: String
    ): Response<ChatApiResponse> {
        return delStatApiService.storeChat(bearerToken, id, pesan)
    }

    override suspend fun updateChatRoom(
        bearerToken: String,
        id: Int,
        isAutomaticDeleted: Int
    ): Response<ChatApiResponse> {
        return delStatApiService.updateChatRoom(bearerToken, id, isAutomaticDeleted)
    }
}