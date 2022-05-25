package id.del.ac.delstat.data.repository.chat

import id.del.ac.delstat.data.model.chat.ChatApiResponse
import id.del.ac.delstat.data.repository.chat.datasource.ChatRemoteDataSource
import id.del.ac.delstat.domain.repository.ChatRepository

class ChatRepositoryImpl(
    private val chatRemoteDataSource: ChatRemoteDataSource
): ChatRepository {

    override suspend fun getChatRooms(bearerToken: String): ChatApiResponse? {
        try {
            val response = chatRemoteDataSource.getChatRooms(bearerToken)
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun getChatRoom(bearerToken: String, id: Int): ChatApiResponse? {
        try {
            val response = chatRemoteDataSource.getChatRoom(bearerToken, id)
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun storeChatRoom(bearerToken: String, idUser: Int): ChatApiResponse? {
        try {
            val response = chatRemoteDataSource.storeChatRoom(bearerToken, idUser)
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun storeChat(bearerToken: String, id: Int, pesan: String): ChatApiResponse? {
        try {
            val response = chatRemoteDataSource.storeChat(bearerToken, id, pesan)
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun updateChatRoom(
        bearerToken: String,
        id: Int,
        isAutomaticDeleted: Int
    ): ChatApiResponse? {
        try {
            val response = chatRemoteDataSource.updateChatRoom(bearerToken, id, isAutomaticDeleted)
            return response.body()!!
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ChatApiResponse()
    }
}