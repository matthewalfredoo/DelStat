package id.del.ac.delstat.presentation.materi.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.del.ac.delstat.data.model.materi.MateriApiResponse
import id.del.ac.delstat.domain.repository.MateriRepository
import id.del.ac.delstat.util.Helper
import kotlinx.coroutines.launch

class MateriViewModel(
    private val app: Application,
    private val materiRepository: MateriRepository
) : AndroidViewModel(app) {
    /* Properties declaration */
    val materiApiResponse: MutableLiveData<MateriApiResponse> = MutableLiveData()
    /* End of properties declaration */

    fun getMateri(id: Int) {
        viewModelScope.launch {
            try {
                if(checkNetwork()) {
                    val response = materiRepository.getMateri(id)
                    if(response == null) {
                        error()
                        return@launch
                    }
                    materiApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("MateriViewModel", e.message, e)
            }
        }
    }

    fun updateMateri(bearerToken: String, id: Int, linkVideo: String) {
        viewModelScope.launch {
            try {
                if(checkNetwork()) {
                    val response = materiRepository.updateMateri(bearerToken, id, linkVideo)
                    if(response == null) {
                        error()
                        return@launch
                    }
                    materiApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("MateriViewModel", e.message, e)
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
        materiApiResponse.value = MateriApiResponse(
            400,
            message = message
        )
    }

}