package id.del.ac.delstat.presentation.literatur.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.del.ac.delstat.data.model.literatur.LiteraturApiResponse
import id.del.ac.delstat.data.model.materi.MateriApiResponse
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.domain.repository.LiteraturRepository
import id.del.ac.delstat.util.Helper
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception

class LiteraturViewModel(
    private val app: Application,
    private val literaturRepository: LiteraturRepository
) : AndroidViewModel(app) {
    /* Properties declaration */
    val literaturApiResponse: MutableLiveData<LiteraturApiResponse> = MutableLiveData()
    val loadingProgressBar: MutableLiveData<Boolean> = MutableLiveData()
    /* End of properties declaration */

    fun getLiteratur(
        judul: String? = null,
        tag: String? = null
    ) {
        viewModelScope.launch {
            loadingProgressBar.value = true
            try {
                if (checkNetwork()) {
                    val response = literaturRepository.getLiteratur(judul, tag)
                    if (response == null) {
                        error()
                        return@launch
                    }
                    literaturApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("LiteraturViewModel", e.message, e)
            }
            loadingProgressBar.value = false
        }
    }

    fun getDetailLieratur(id: Int) {
        viewModelScope.launch {
            try {
                if (checkNetwork()) {
                    val response = literaturRepository.getDetailLiteratur(id)
                    if (response == null) {
                        error()
                        return@launch
                    }
                    literaturApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("LiteraturViewModel", e.message, e)
            }
        }
    }

    fun storeLiteratur(
        bearerToken: String,
        judul: String,
        penulis: String,
        tahunTerbit: Int,
        tag: String,
        file: File?
    ) {
        viewModelScope.launch {
            try {
                if (checkNetwork()) {
                    val response = literaturRepository.storeLiteratur(
                        bearerToken,
                        judul,
                        penulis,
                        tahunTerbit,
                        tag,
                        file
                    )
                    if (response == null) {
                        error()
                        return@launch
                    }
                    literaturApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("LiteraturViewModel", e.message, e)
            }
        }
    }

    fun updateLiteratur(
        bearerToken: String,
        id: Int,
        judul: String,
        penulis: String,
        tahunTerbit: Int,
        tag: String,
        file: File?
    ) {
        viewModelScope.launch {
            try {
                if (checkNetwork()) {
                    val response = literaturRepository.updateLiteratur(
                        bearerToken, id, judul, penulis, tahunTerbit, tag, file
                    )
                    if (response == null) {
                        error()
                        return@launch
                    }
                    literaturApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("LiteraturViewModel", e.message, e)
            }
        }
    }

    fun deleteLiteratur(bearerToken: String, id: Int) {
        viewModelScope.launch {
            try {
                if (checkNetwork()) {
                    val response = literaturRepository.deleteLiteratur(bearerToken, id)
                    if (response == null) {
                        error()
                        return@launch
                    }
                    literaturApiResponse.value = response!!
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
        literaturApiResponse.value = LiteraturApiResponse(
            400,
            message = message
        )
    }
}