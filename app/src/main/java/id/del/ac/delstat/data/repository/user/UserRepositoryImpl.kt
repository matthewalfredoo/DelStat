package id.del.ac.delstat.data.repository.user

import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.model.user.UserApiResponse
import id.del.ac.delstat.data.repository.user.datasource.UserRemoteDataSource
import id.del.ac.delstat.domain.repository.UserRepository
import java.io.File

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource
): UserRepository {
    override suspend fun register(nama: String, email: String, noHp: String, password: String): UserApiResponse? {
        try {
            val response = userRemoteDataSource.register(nama, email, noHp, password)
            return response.body()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun login(email: String, password: String): UserApiResponse? {
        try {
            val response = userRemoteDataSource.login(email, password)
            return response.body()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun getUser(bearerToken: String): UserApiResponse? {
        try {
            val response = userRemoteDataSource.getUser(bearerToken)
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun findUsersByRole(bearerToken: String): UserApiResponse? {
        try {
            val response = userRemoteDataSource.findUsersByRole(bearerToken)
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun logout(bearToken: String): UserApiResponse? {
        try {
            val response = userRemoteDataSource.logout(bearToken)
            return response.body()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun updateProfile(
        bearerToken: String,
        nama: String,
        email: String,
        noHp: String,
        jenjang: String,
        fotoProfil: File?
    ): UserApiResponse? {
        try {
            val response = userRemoteDataSource.updateProfile(bearerToken, nama, email, noHp, jenjang, fotoProfil)
            return response.body()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun updatePassword(
        bearerToken: String,
        password: String,
        newPassword: String,
        newPasswordConfirmation: String
    ): UserApiResponse? {
        try {
            val response = userRemoteDataSource.updatePassword(bearerToken, password, newPassword, newPasswordConfirmation)
            return response.body()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun forgotPassword(email: String): UserApiResponse? {
        try {
            val response = userRemoteDataSource.forgotPassword(email)
            return response.body()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun changePassword(
        token: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ): UserApiResponse? {
        try {
            val response = userRemoteDataSource.changePassword(token, email, password, passwordConfirmation)
            return response.body()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}