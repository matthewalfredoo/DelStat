package id.del.ac.delstat.data.api

import id.del.ac.delstat.data.model.user.UserApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface DelStatApiService {
    /* User-related Services */

    @FormUrlEncoded
    @POST("api/register")
    suspend fun register(
        @Field("nama")
        nama: String,

        @Field("email")
        email: String,

        @Field("no_hp")
        noHp: String,

        @Field("password")
        password: String,
    ): Response<UserApiResponse>

    @FormUrlEncoded
    @POST("api/login")
    suspend fun login(
        @Field("email")
        email: String,

        @Field("password")
        password: String
    ): Response<UserApiResponse>

    @Multipart
    @POST("api/user/update")
    suspend fun updateProfile(
        @Header("Authorization")
        berarerToken: String,

        @Part("nama")
        nama: RequestBody,

        @Part("email")
        email: RequestBody,

        @Part("no_hp")
        noHp: RequestBody,

        @Part("jenjang")
        jenjang: RequestBody,

        @Part
        fotoProfil: MultipartBody.Part? = null
    ): Response<UserApiResponse>

    @FormUrlEncoded
    @POST("api/user/password")
    suspend fun updatePassword(
        @Header("Authorization")
        berarerToken: String,

        @Field("password")
        password: String,

        @Field("new_password")
        newPassword: String,

        @Field("new_password_confirmation")
        newPasswordConfirmation: String
    ): Response<UserApiResponse>

    @POST("api/logout")
    suspend fun logout(
        @Header("Authorization")
        bearerToken: String
    ): Response<UserApiResponse>

    /* End of User-related Services */
}