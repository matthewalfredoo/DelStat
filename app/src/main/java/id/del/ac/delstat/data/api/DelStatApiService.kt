package id.del.ac.delstat.data.api

import id.del.ac.delstat.data.model.user.UserApiResponse
import retrofit2.Response
import retrofit2.http.*

interface DelStatApiService {
    /* User-related Services */

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("nama")
        nama: String,

        @Field("email")
        email: String,

        @Field("no_hp")
        noHp: String,

        @Field("password")
        password: String,

        @Field("password_confirmation")
        passwordConfirmation: String,

        @Field("jenjang")
        jenjang: String
    ): Response<UserApiResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email")
        email: String,

        @Field("password")
        password: String
    ): Response<UserApiResponse>

    @FormUrlEncoded
    @POST("logout")
    suspend fun logout(
        @Header("Authorization")
        bearerToken: String
    ): Response<UserApiResponse>

    /* End of User-related Services */
}