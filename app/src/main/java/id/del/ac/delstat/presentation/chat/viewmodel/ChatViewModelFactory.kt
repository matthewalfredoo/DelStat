package id.del.ac.delstat.presentation.chat.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.del.ac.delstat.domain.repository.ChatRepository

class ChatViewModelFactory(
    private val app: Application,
    private val chatRepository: ChatRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(app, chatRepository) as T
    }

}