package id.del.ac.delstat.presentation.kuis.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.del.ac.delstat.domain.repository.HasilKuisRepository

class HasilKuisViewModelFactory(
    private val app: Application,
    private val hasilKuisRepository: HasilKuisRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HasilKuisViewModel(app, hasilKuisRepository) as T
    }
}