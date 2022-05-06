package id.del.ac.delstat.presentation.materi.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.del.ac.delstat.domain.repository.MateriRepository

class MateriViewModelFactory(
    private val app: Application,
    private val materiRepository: MateriRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MateriViewModel(app, materiRepository) as T
    }
}