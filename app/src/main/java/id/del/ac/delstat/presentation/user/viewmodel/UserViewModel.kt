package id.del.ac.delstat.presentation.user.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.model.user.UserApiResponse
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.domain.repository.UserRepository
import id.del.ac.delstat.util.Helper
import kotlinx.coroutines.launch
import java.io.File

class UserViewModel(
    private val app: Application,
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences
) : AndroidViewModel(app) {
    /* Properties Declaration */
    val userApiResponse: MutableLiveData<UserApiResponse> = MutableLiveData()
    val loadingProgressBar: MutableLiveData<Boolean> = MutableLiveData()
    /* End of Properties Declaration */

    fun register(nama: String, email: String, noHp: String, password: String) {
        viewModelScope.launch {
            loadingProgressBar.value = true
            try {
                if (checkNetwork()) {
                    val response = userRepository.register(nama, email, noHp, password)
                    if (response == null) {
                        error()
                        return@launch
                    }
                    userApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                e.printStackTrace()
            }
            loadingProgressBar.value = false
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loadingProgressBar.value = true
            try {
                if (checkNetwork()) {
                    val response = userRepository.login(email, password)
                    if (response == null) {
                        error()
                        return@launch
                    }
                    userApiResponse.value = response!!
                    // Log.d("MyTag", response.toString())
                    if (response.code == 200) {
                        userPreferences.setUserId(response.user!!.id!!)
                        userPreferences.setUserNama(response.user.nama!!)
                        userPreferences.setUserEmail(response.user.email!!)
                        userPreferences.setUserNoHp(response.user.noHp!!)
                        userPreferences.setUserFotoProfil(response.user.fotoProfil ?: "")
                        userPreferences.setUserJenjang(response.user.jenjang!!)
                        userPreferences.setUserToken(response.token!!)
                        userPreferences.setUserRole(response.user.role!!)
                    }
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("MyTag", "exception", e)
            }
            loadingProgressBar.value = false
        }
    }

    fun getUser(bearerToken: String) {
        viewModelScope.launch {
            try {
                if (checkNetwork()) {
                    val response = userRepository.getUser(bearerToken)
                    // if response is null, it means the user is unauthorized anymore and will be logged out automatically
                    if (response == null) {
                        error("Session Anda sudah habis, silahkan login kembali")
                        userPreferences.clear()
                        return@launch
                    }
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("MyTag", "exception", e)
            }
        }
    }

    fun updateProfile(
        bearerToken: String,
        nama: String,
        email: String,
        noHp: String,
        jenjang: String,
        fotoProfil: File? = null
    ) {
        viewModelScope.launch {
            loadingProgressBar.value = true
            try {
                if (checkNetwork()) {
                    val response = userRepository.updateProfile(
                        bearerToken,
                        nama,
                        email,
                        noHp,
                        jenjang,
                        fotoProfil
                    )
                    if (response == null) {
                        error("Terjadi kesalahan")
                        return@launch
                    }
                    userApiResponse.value = response!!
                    Log.d("MyTag", response.toString())
                    if (response.code == 200) {
                        userPreferences.setUserNama(response.user!!.nama!!)
                        userPreferences.setUserEmail(response.user.email!!)
                        userPreferences.setUserNoHp(response.user.noHp!!)
                        userPreferences.setUserJenjang(response.user.jenjang!!)
                        userPreferences.setUserFotoProfil(response.user.fotoProfil)
                        // Log.d("MyTag", "update profile success")
                    }
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("MyTag", "exception", e)
            }
            loadingProgressBar.value = false
        }
    }

    fun updatePassword(
        bearerToken: String,
        password: String,
        newPassword: String,
        newPasswordConfirmation: String
    ) {
        viewModelScope.launch {
            loadingProgressBar.value = true
            try {
                if (checkNetwork()) {
                    val response = userRepository.updatePassword(
                        bearerToken,
                        password,
                        newPassword,
                        newPasswordConfirmation
                    )
                    if (response == null) {
                        error("Terjadi kesalahan")
                        return@launch
                    }
                    userApiResponse.value = response!!
                    Log.d("MyTag", response.toString())
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("MyTag", "exception", e)
            }
            loadingProgressBar.value = false
        }
    }

    fun findUsersByRole(bearerToken: String) {
        viewModelScope.launch {
            loadingProgressBar.value = true
            try {
                if (checkNetwork()) {
                    val response = userRepository.findUsersByRole(bearerToken)
                    if (response == null) {
                        error("Terjadi kesalahan")
                        return@launch
                    }
                    userApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception saat mengambil data user")
                Log.e("MyTag", "exception", e)
            }
            loadingProgressBar.value = false
        }
    }

    fun logout(bearerToken: String) {
        viewModelScope.launch {
            loadingProgressBar.value = true
            try {
                if (checkNetwork()) {
                    val response = userRepository.logout(bearerToken)
                    if (response == null) {
                        error("Terjadi null")
                        return@launch
                    }
                    userApiResponse.value = response!!
                    // Log.d("MyTag", response.toString())

                    userPreferences.clear()
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("MyTag", "exception", e)
            }
            loadingProgressBar.value = false
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            loadingProgressBar.value = true
            try {
                if (checkNetwork()) {
                    val response = userRepository.forgotPassword(email)
                    if (response == null) {
                        error("Terjadi kesalahan")
                        return@launch
                    }
                    userApiResponse.value = response!!
                    Log.d("MyTag", response.toString())
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("MyTag", "exception", e)
            }
            loadingProgressBar.value = false
        }
    }

    fun changePassword(
        token: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ) {
        viewModelScope.launch {
            loadingProgressBar.value = true
            try {
                if (checkNetwork()) {
                    val response = userRepository.changePassword(
                        token,
                        email,
                        password,
                        passwordConfirmation
                    )
                    if (response == null) {
                        error("Terjadi kesalahan")
                        return@launch
                    }
                    userApiResponse.value = response!!
                    Log.d("MyTag", response.toString())
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("MyTag", "exception", e)
            }
            loadingProgressBar.value = false
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
        userApiResponse.value = UserApiResponse(
            400,
            message = message
        )
    }

}