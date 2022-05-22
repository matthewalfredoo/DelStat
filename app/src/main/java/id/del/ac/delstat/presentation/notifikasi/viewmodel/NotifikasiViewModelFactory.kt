package id.del.ac.delstat.presentation.notifikasi.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.del.ac.delstat.domain.repository.NotifikasiRepository

class NotifikasiViewModelFactory(
    private val app: Application,
    private val notifikasiRepository: NotifikasiRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NotifikasiViewModel(app, notifikasiRepository) as T
    }

}