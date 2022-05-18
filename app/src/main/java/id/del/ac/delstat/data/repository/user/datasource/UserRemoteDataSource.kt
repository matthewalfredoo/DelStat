package id.del.ac.delstat.data.repository.user.datasource

import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.model.user.UserApiResponse
import retrofit2.Response
import java.io.File

interface UserRemoteDataSource {

    suspend fun register(
        nama: String,
        email: String,
        noHp: String,
        password: String
    ): Response<UserApiResponse>

    suspend fun login(email: String, password: String): Response<UserApiResponse>

    suspend fun getUser(bearerToken: String): Response<UserApiResponse>

    suspend fun updateProfile(
        bearerToken: String,
        nama: String,
        email: String,
        noHp: String,
        jenjang: String,
        fotoProfil: File? = null
    ): Response<UserApiResponse>

    suspend fun updatePassword(
        bearerToken: String,
        password: String,
        newPassword: String,
        newPasswordConfirmation: String
    ): Response<UserApiResponse>

    suspend fun logout(bearerToken: String): Response<UserApiResponse>

}