package id.del.ac.delstat.presentation.user.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.domain.repository.UserRepository

class UserViewModelFactory(
    private val app: Application,
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(app, userRepository, userPreferences) as T
    }

}