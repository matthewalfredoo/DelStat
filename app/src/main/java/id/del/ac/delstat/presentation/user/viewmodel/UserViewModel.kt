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
import id.del.ac.delstat.domain.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(
    private val app: Application,
    private val userRepository: UserRepository
) : AndroidViewModel(app) {
    /* Properties Declaration */
    val userApiResponse: MutableLiveData<UserApiResponse> = MutableLiveData()
    /* End of Properties Declaration */

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

    fun register(user: User, password: String, passwordConfirmation: String) {
        viewModelScope.launch {
            try {
                if (checkNetwork()) {
                    val response = userRepository.register(user, password, passwordConfirmation)
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
                    if(response == null){
                        error("Terjadi kesalahan")
                        return@launch
                    }
                    userApiResponse.value = response!!
                }
            } catch (e: Exception) {
                error("Terjadi exception")
                Log.e("MyTag", "exception", e)
            }
        }
    }

    private fun checkNetwork(): Boolean {
        if (!isNetworkAvailable(app)) {
            error("Tidak dapat mengakses jaringan")
            return false
        }
        return true
    }

    private fun error(message: String = "Kesalahan terjadi, silahkan coba lagi nanti") {
        userApiResponse.value = UserApiResponse(
            400,
            message = message
        )
    }

}