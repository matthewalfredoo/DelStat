package id.del.ac.delstat.data.repository.user.datasourceimpl

import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.model.user.User
import id.del.ac.delstat.data.model.user.UserApiResponse
import id.del.ac.delstat.data.repository.user.datasource.UserRemoteDataSource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class UserRemoteDataSourceImpl(private val delStatApiService: DelStatApiService) :
    UserRemoteDataSource {

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

    override suspend fun getUser(bearerToken: String): Response<UserApiResponse> {
        return delStatApiService.getUser(bearerToken)
    }

    override suspend fun updateProfile(
        bearerToken: String,
        nama: String,
        email: String,
        noHp: String,
        jenjang: String,
        fotoProfil: File?
    ): Response<UserApiResponse> {
        return delStatApiService.updateProfile(
            bearerToken,
            requestBody(nama),
            requestBody(email),
            requestBody(noHp),
            requestBody(jenjang),
            requestBody(fotoProfil)
        )
    }

    override suspend fun updatePassword(
        bearerToken: String,
        password: String,
        newPassword: String,
        newPasswordConfirmation: String
    ): Response<UserApiResponse> {
        return delStatApiService.updatePassword(bearerToken, password, newPassword, newPasswordConfirmation)
    }

    override suspend fun findUsersByRole(bearerToken: String): Response<UserApiResponse> {
        return delStatApiService.findUsersByRole(bearerToken)
    }

    override suspend fun logout(bearerToken: String): Response<UserApiResponse> {
        return delStatApiService.logout(bearerToken)
    }

    override suspend fun forgotPassword(email: String): Response<UserApiResponse> {
        return delStatApiService.forgotPassword(email)
    }

    override suspend fun changePassword(
        token: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ): Response<UserApiResponse> {
        return delStatApiService.changePassword(token, email, password, passwordConfirmation)
    }

    private fun requestBody(field: String): RequestBody {
        return field.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun requestBody(file: File?): MultipartBody.Part? {
        if(file == null) {
            return null // empty body
        }
        // else
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("foto_profil", file.name, requestFile)
    }

}