package id.del.ac.delstat.domain.repository

import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.model.user.UserApiResponse

interface UserRepository {

    suspend fun register(nama: String, email: String, noHp: String, password: String): UserApiResponse?
    suspend fun login(email: String, password: String): UserApiResponse?
    suspend fun logout(bearToken: String): UserApiResponse?
    suspend fun currentUser(): UserApiResponse?
    suspend fun editProfile(user: User): UserApiResponse?
    suspend fun editPassword(user: User): UserApiResponse?

}
