package id.del.ac.delstat.presentation.kuis.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.del.ac.delstat.data.model.kuis.HasilKuisApiResponse
import id.del.ac.delstat.domain.repository.HasilKuisRepository
import id.del.ac.delstat.util.Helper
import kotlinx.coroutines.launch

class HasilKuisViewModel(
    private val app: Application,
    private val hasilKuisRepository: HasilKuisRepository
) : AndroidViewModel(app) {
    /* Properties Declaration */
    val hasilKuisApiResponse: MutableLiveData<HasilKuisApiResponse> = MutableLiveData()
    /* End of Properties Declaration */

    fun getHasilKuis(bearerToken: String) {
        viewModelScope.launch {
            try {
                if(checkNetwork()) {
                    val response = hasilKuisRepository.getHasilKuis(bearerToken)
                    if(response == null) {
                        error()
                        return@launch
                    }
                    hasilKuisApiResponse.value = response
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("HasilKuisViewModel", e.toString(), e)
            }
        }
    }

    fun storeHasilKuis(
        bearerToken: String,
        idKuis: Int,
        nilaiKuis: Double
    ) {
        viewModelScope.launch {
            try {
                if(checkNetwork()) {
                    val response = hasilKuisRepository.storeHasilKuis(
                        bearerToken,
                        idKuis,
                        nilaiKuis
                    )
                    if(response == null) {
                        error()
                        return@launch
                    }
                    hasilKuisApiResponse.value = response
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("HasilKuisViewModel", e.toString(), e)
            }
        }
    }

    fun getDetailHasilKuis(
        bearerToken: String,
        idKuis: Int
    ) {
        viewModelScope.launch {
            try {
                if(checkNetwork()) {
                    val response = hasilKuisRepository.getDetailHasilKuis(
                        bearerToken,
                        idKuis
                    )
                    if(response == null) {
                        error()
                        return@launch
                    }
                    hasilKuisApiResponse.value = response
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("HasilKuisViewModel", e.toString(), e)
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
        hasilKuisApiResponse.value = HasilKuisApiResponse(
            message = message
        )
    }

}