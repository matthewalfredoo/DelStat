package id.del.ac.delstat.domain.repository

import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.model.user.UserApiResponse
import java.io.File

interface UserRepository {

    suspend fun register(
        nama: String,
        email: String,
        noHp: String,
        password: String
    ): UserApiResponse?

    suspend fun login(email: String, password: String): UserApiResponse?

    suspend fun logout(bearToken: String): UserApiResponse?

    suspend fun updateProfile(
        bearerToken: String,
        nama: String,
        email: String,
        noHp: String,
        jenjang: String,
        fotoProfil: File? = null
    ): UserApiResponse?

    suspend fun editPassword(user: User): UserApiResponse?

}
