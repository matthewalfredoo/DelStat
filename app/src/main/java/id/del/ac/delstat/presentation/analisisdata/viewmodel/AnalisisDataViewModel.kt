package id.del.ac.delstat.presentation.analisisdata.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.del.ac.delstat.data.model.analisisdata.AnalisisDataApiResponse
import id.del.ac.delstat.data.model.literatur.LiteraturApiResponse
import id.del.ac.delstat.domain.repository.AnalisisDataRepository
import id.del.ac.delstat.util.Helper
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class AnalisisDataViewModel(
    private val app: Application,
    private val analisisDataRepository: AnalisisDataRepository
) : AndroidViewModel(app) {
    /* Properties Declaration */
    val analisisDataApiResponse: MutableLiveData<AnalisisDataApiResponse> = MutableLiveData()
    /* End of Properties Declaration */

    fun getAnalisisData(bearerToken: String) {
        viewModelScope.launch {
            try {
                if (checkNetwork()) {
                    val response = analisisDataRepository.getAnalisisData(bearerToken)
                    if (response == null) {
                        error()
                        return@launch
                    }
                    analisisDataApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("LiteraturViewModel", e.message, e)
            }
        }
    }

    fun getDetailAnalisisData(bearerToken: String, id: Int) {
        viewModelScope.launch {
            try {
                if (checkNetwork()) {
                    val response = analisisDataRepository.getDetailAnalisisData(bearerToken, id)
                    if (response == null) {
                        error()
                        return@launch
                    }
                    analisisDataApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("LiteraturViewModel", e.message, e)
            }
        }
    }

    fun storeAnalisisData(
        bearerToken: String,
        judul: String,
        deskripsi: String,
        file: File?
    ) {
        viewModelScope.launch {
            try {
                if (checkNetwork()) {
                    val response = analisisDataRepository.storeAnalisisData(bearerToken, judul, deskripsi, file)
                    if (response == null) {
                        error()
                        return@launch
                    }
                    analisisDataApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("LiteraturViewModel", e.message, e)
            }
        }
    }

    fun updateAnalisisData(
        bearerToken: String,
        id: Int,
        judul: String,
        deskripsi: String,
        file: File?
    ) {
        viewModelScope.launch {
            try {
                if (checkNetwork()) {
                    val response = analisisDataRepository.updateAnalisisData(bearerToken, id, judul, deskripsi, file)
                    if (response == null) {
                        error()
                        return@launch
                    }
                    analisisDataApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("LiteraturViewModel", e.message, e)
            }
        }
    }

    fun updateStatusAnalisisData(
        bearerToken: String,
        id: Int,
        status: String,
    ) {
        viewModelScope.launch {
            try {
                if (checkNetwork()) {
                    val response = analisisDataRepository.updateStatusAnalisisData(bearerToken, id, status)
                    if (response == null) {
                        error()
                        return@launch
                    }
                    analisisDataApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("LiteraturViewModel", e.message, e)
            }
        }
    }

    fun cancelAnalisisData(bearerToken: String, id: Int) {
        viewModelScope.launch {
            try {
                if (checkNetwork()) {
                    val response = analisisDataRepository.cancelAnalisisData(bearerToken, id)
                    if (response == null) {
                        error()
                        return@launch
                    }
                    analisisDataApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("LiteraturViewModel", e.message, e)
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
        analisisDataApiResponse.value = AnalisisDataApiResponse(
            message = message
        )
    }
}