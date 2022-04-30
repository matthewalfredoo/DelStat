package id.del.ac.delstat.data.repository.user.datasource

import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.model.user.UserApiResponse
import retrofit2.Response

interface UserRemoteDataSource {

    suspend fun register(user: User, password: String, passwordConfirmation: String): Response<UserApiResponse>
    suspend fun login(email: String, password: String): Response<UserApiResponse>

}