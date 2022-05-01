package id.del.ac.delstat.data.repository.user.datasourceimpl

import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.model.user.UserApiResponse
import id.del.ac.delstat.data.repository.user.datasource.UserRemoteDataSource
import retrofit2.Response

class UserRemoteDataSourceImpl(private val delStatApiService: DelStatApiService): UserRemoteDataSource {

    override suspend fun register(
        user: User, password: String, passwordConfirmation: String
    ): Response<UserApiResponse> {
        return delStatApiService.register(
            user.nama!!,
            user.email!!,
            user.noHp!!,
            password,
            passwordConfirmation,
            user.jenjang!!,
        )
    }

    override suspend fun login(email: String, password: String): Response<UserApiResponse> {
        return delStatApiService.login(email, password)
    }

    override suspend fun logout(bearerToken: String): Response<UserApiResponse> {
        return delStatApiService.logout(bearerToken)
    }

}