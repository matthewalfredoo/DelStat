package id.del.ac.delstat.presentation.analisisdata.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.del.ac.delstat.domain.repository.AnalisisDataRepository

class AnalisisDataViewModelFactory(
    private val app: Application,
    private val analisisDataRepository: AnalisisDataRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AnalisisDataViewModel(app, analisisDataRepository) as T
    }
}