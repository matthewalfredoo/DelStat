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

    override suspend fun logout(bearerToken: String): Response<UserApiResponse> {
        return delStatApiService.logout(bearerToken)
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