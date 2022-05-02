package id.del.ac.delstat.data.repository.user.datasourceimpl

import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.model.user.UserApiResponse
import id.del.ac.delstat.data.repository.user.datasource.UserRemoteDataSource
import retrofit2.Response

class UserRemoteDataSourceImpl(private val delStatApiService: DelStatApiService): UserRemoteDataSource {

    override suspend fun register(
        nama: String, email: String, noHp: String, password: String
    ): Response<UserApiResponse> {
        return delStatApiService.register(
            nama, email, noHp, password
        )
    }

    override suspend fun login(email: String, password: String): Response<UserApiResponse> {
        return delStatApiService.login(email, password)
    }

    override suspend fun logout(bearerToken: String): Response<UserApiResponse> {
        return delStatApiService.logout(bearerToken)
    }

}