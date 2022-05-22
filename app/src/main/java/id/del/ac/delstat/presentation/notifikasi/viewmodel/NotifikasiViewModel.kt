package id.del.ac.delstat.presentation.notifikasi.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.del.ac.delstat.data.model.materi.MateriApiResponse
import id.del.ac.delstat.data.model.notifikasi.NotifikasiApiResponse
import id.del.ac.delstat.domain.repository.NotifikasiRepository
import id.del.ac.delstat.util.Helper
import kotlinx.coroutines.launch

class NotifikasiViewModel(
    private val app: Application,
    private val notifikasiRepository: NotifikasiRepository
) : AndroidViewModel(app){
    /* Properties declaration */
    val notifikasiApiResponse: MutableLiveData<NotifikasiApiResponse> = MutableLiveData()
    /* End of properties declaration */

    fun getNotifiksai(bearerToken: String) {
        viewModelScope.launch {
            try {
                if(checkNetwork()) {
                    val response = notifikasiRepository.getNotifikasi(bearerToken)
                    if(response == null) {
                        error()
                        return@launch
                    }
                    notifikasiApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception saat mengambil data notifikasi")
                Log.e("NotifikasiViewModel", e.message, e)
            }
        }
    }

    fun getCountNotifikasi(bearerToken: String) {
        viewModelScope.launch {
            try {
                if(checkNetwork()) {
                    val response = notifikasiRepository.getCountNotifikasi(bearerToken)
                    if(response == null) {
                        error()
                        return@launch
                    }
                    notifikasiApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception saat mengambil data notifikasi")
                Log.e("NotifikasiViewModel", e.message, e)
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
        notifikasiApiResponse.value = NotifikasiApiResponse(
            400,
            message = message
        )
    }

}