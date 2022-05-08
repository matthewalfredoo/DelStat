package id.del.ac.delstat.data.api

import id.del.ac.delstat.data.model.literatur.LiteraturApiResponse
import id.del.ac.delstat.data.model.materi.MateriApiResponse
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

    /* Materi related Services */

    @GET("api/materi/{id}")
    suspend fun getMateri(
        @Path("id")
        id: Int
    ): Response<MateriApiResponse>

    @FormUrlEncoded
    @POST("api/materi/update/{id}")
    suspend fun updateMateri(
        @Header("Authorization")
        bearerToken: String,

        @Path("id")
        id: Int,

        @Field("link_video")
        linkVideo: String
    ): Response<MateriApiResponse>

    /* End of Materi related Services */

    /* Literature related Services */

    @GET("api/literatur")
    suspend fun getLiteratur(): Response<LiteraturApiResponse>

    @GET("api/literatur/{id}")
    suspend fun getDetailLiteratur(
        @Path("id")
        id: Int
    ): Response<LiteraturApiResponse>

    @Multipart
    @POST("api/literatur/store")
    suspend fun storeLiteratur(
        @Header("Authorization")
        bearerToken: String,

        @Part("judul")
        judul: RequestBody,

        @Part("penulis")
        penulis: RequestBody,

        @Part("tahun_terbit")
        tahunTerbit: RequestBody,

        @Part("tag")
        tag: RequestBody,

        @Part
        file: MultipartBody.Part? = null
    ): Response<LiteraturApiResponse>

    @Multipart
    @POST("api/literatur/update/{id}")
    suspend fun updateLiteratur(
        @Header("Authorization")
        bearerToken: String,

        @Path("id")
        id: Int,

        @Part("judul")
        judul: RequestBody,

        @Part("penulis")
        penulis: RequestBody,

        @Part("tahun_terbit")
        tahunTerbit: RequestBody,

        @Part("tag")
        tag: RequestBody,

        @Part
        file: MultipartBody.Part? = null
    ): Response<LiteraturApiResponse>

    @POST("api/literatur/delete/{id}")
    suspend fun deleteLiteratur(
        @Header("Authorization")
        bearerToken: String,

        @Path("id")
        id: Int
    ): Response<LiteraturApiResponse>

    /* End of Literature related Services */
}