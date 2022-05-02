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
import kotlinx.coroutines.launch

class UserViewModel(
    private val app: Application,
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences
) : AndroidViewModel(app) {
    /* Properties Declaration */
    val userApiResponse: MutableLiveData<UserApiResponse> = MutableLiveData()
    /* End of Properties Declaration */

    /**
     * This method is used to check if the device is connected to the internet
     */
    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false

    }

    fun register(nama: String, email: String, noHp: String, password: String) {
        viewModelScope.launch {
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
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                if (checkNetwork()) {
                    val response = userRepository.login(email, password)
                    if (response == null) {
                        error()
                        return@launch
                    }
                    userApiResponse.value = response!!
                    // Log.d("MyTag", response.toString())
                    if(response.code == 200) {
                        userPreferences.setUserId(response.user!!.id!!)
                        userPreferences.setUserNama(response.user.nama!!)
                        userPreferences.setUserEmail(response.user.email!!)
                        userPreferences.setUserNoHp(response.user.noHp!!)
                        userPreferences.setUserFotoProfil(response.user.fotoProfil ?: "")
                        userPreferences.setUserJenjang(response.user.jenjang!!)
                        userPreferences.setUserToken(response.token!!)
                    }
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("MyTag", "exception", e)
            }
        }
    }

    fun logout(bearerToken: String) {
        viewModelScope.launch {
            try {
                if (checkNetwork()) {
                    val response = userRepository.logout(bearerToken)
                    if (response == null) {
                        error("Terjadi null")
                        return@launch
                    }
                    userApiResponse.value = response!!
                    Log.d("MyTag", response.toString())

                    userPreferences.clear()
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("MyTag", "exception", e)
            }
        }
    }

    /**
     * This method is used to check if the device is connected to the internet
     * This is a shorthand method of isNetworkAvailable(Context)
     */
    private fun checkNetwork(): Boolean {
        if (!isNetworkAvailable(app)) {
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