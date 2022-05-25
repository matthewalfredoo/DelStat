package id.del.ac.delstat.presentation.chat.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.del.ac.delstat.data.model.analisisdata.AnalisisDataApiResponse
import id.del.ac.delstat.data.model.chat.ChatApiResponse
import id.del.ac.delstat.domain.repository.ChatRepository
import id.del.ac.delstat.util.Helper
import kotlinx.coroutines.launch

class ChatViewModel(
    private val app: Application,
    private val chatRepository: ChatRepository
) : AndroidViewModel(app) {
    /* Properties Declaration */
    val chatApiResponse: MutableLiveData<ChatApiResponse> = MutableLiveData()
    /* End of Properties Declaration */

    fun getChatRooms(bearerToken: String) {
        viewModelScope.launch {
            try {
                if(checkNetwork()) {
                    val response = chatRepository.getChatRooms(bearerToken)
                    if (response == null) {
                        error()
                        return@launch
                    }
                    chatApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception saat mengambil data chat")
                Log.e("ChatViewModel", e.message, e)
            }
        }
    }

    fun getChatRoom(bearerToken: String, id: Int) {
        viewModelScope.launch {
            try {
                if(checkNetwork()) {
                    val response = chatRepository.getChatRoom(bearerToken, id)
                    if (response == null) {
                        error()
                        return@launch
                    }
                    chatApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception saat mengambil data chat")
                Log.e("ChatViewModel", e.message, e)
            }
        }
    }

    fun storeChatRoom(bearerToken: String, idUser: Int) {
        viewModelScope.launch {
            try {
                if(checkNetwork()) {
                    val response = chatRepository.storeChatRoom(bearerToken, idUser)
                    if (response == null) {
                        error()
                        return@launch
                    }
                    chatApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception saat mengambil data chat")
                Log.e("ChatViewModel", e.message, e)
            }
        }
    }

    fun storeChat(bearerToken: String, id: Int, pesan: String) {
        viewModelScope.launch {
            try {
                if(checkNetwork()) {
                    val response = chatRepository.storeChat(bearerToken, id, pesan)
                    if (response == null) {
                        error()
                        return@launch
                    }
                    chatApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception saat mengambil data chat")
                Log.e("ChatViewModel", e.message, e)
            }
        }
    }

    fun updateChatRoom(bearerToken: String, id: Int, isAutomaticDeleted: Int) {
        viewModelScope.launch {
            try {
                if(checkNetwork()) {
                    val response = chatRepository.updateChatRoom(bearerToken, id, isAutomaticDeleted)
                    if (response == null) {
                        error()
                        return@launch
                    }
                    chatApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception saat mengambil data chat")
                Log.e("ChatViewModel", e.message, e)
            }
        }
    }

    /**
     * This method is used to check if the device is connected to the internet
     * This is a shorthand method of isNetworkAvailable(Context)
     */
    private fun checkNetwork(): Boolean {
        if (!Helper.isNetworkAvailable(app)) {
            error("Tidak dapat mengakses jaringan")
            return false
        }
        return true
    }

    /**
     * This method is used to show error message and post it to variable userApiResponse
     */
    private fun error(message: String = "Kesalahan terjadi, silahkan coba lagi nanti") {
        chatApiResponse.value = ChatApiResponse(
            message = message
        )
    }
}