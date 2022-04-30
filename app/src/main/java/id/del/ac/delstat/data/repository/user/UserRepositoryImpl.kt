package id.del.ac.delstat.data.repository.user

import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.model.user.UserApiResponse
import id.del.ac.delstat.data.repository.user.datasource.UserRemoteDataSource
import id.del.ac.delstat.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource
): UserRepository {
    override suspend fun register(user: User, password: String, passwordConfirmation: String): UserApiResponse? {
        try {
            val response = userRemoteDataSource.register(user, password, passwordConfirmation)
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

    override suspend fun currentUser(): UserApiResponse? {
        TODO("Not yet implemented")
    }

    override suspend fun editProfile(user: User): UserApiResponse? {
        TODO("Not yet implemented")
    }

    override suspend fun editPassword(user: User): UserApiResponse? {
        TODO("Not yet implemented")
    }
}