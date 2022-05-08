package id.del.ac.delstat.presentation.literatur.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.domain.repository.LiteraturRepository

class LiteraturViewModelFactory(
    private val app: Application,
    private val literaturRepository: LiteraturRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LiteraturViewModel(app, literaturRepository) as T
    }
}