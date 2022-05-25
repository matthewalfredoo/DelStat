package id.del.ac.delstat.data.api

import id.del.ac.delstat.data.model.analisisdata.AnalisisDataApiResponse
import id.del.ac.delstat.data.model.chat.ChatApiResponse
import id.del.ac.delstat.data.model.literatur.LiteraturApiResponse
import id.del.ac.delstat.data.model.materi.MateriApiResponse
import id.del.ac.delstat.data.model.notifikasi.NotifikasiApiResponse
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

    @GET("api/user")
    suspend fun getUser(
        @Header("Authorization")
        bearerToken: String,

        @Header("Accept")
        accept: String = "application/json"
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

    @GET("api/user/findbyrole")
    suspend fun findUsersByRole(
        @Header("Authorization")
        bearerToken: String
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

        @Field("link_video_1")
        linkVideo1: String,

        @Field("link_video_2")
        linkVideo2: String? = null,

        @Field("link_video_3")
        linkVideo3: String? = null,

        @Field("link_video_4")
        linkVideo4: String? = null
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

    /* Analisis Data related Services */

    @GET("api/analisisdata")
    suspend fun getAnalisisData(
        @Header("Authorization")
        bearerToken: String
    ): Response<AnalisisDataApiResponse>

    @GET("api/analisisdata/{id}")
    suspend fun getDetailAnalisisData(
        @Header("Authorization")
        bearerToken: String,

        @Path("id")
        id: Int
    ): Response<AnalisisDataApiResponse>

    @Multipart
    @POST("api/analisisdata/store")
    suspend fun storeAnalisisData(
        @Header("Authorization")
        bearerToken: String,

        @Part("judul")
        judul: RequestBody,

        @Part("deskripsi")
        deskripsi: RequestBody,

        @Part
        file: MultipartBody.Part? = null
    ): Response<AnalisisDataApiResponse>

    @Multipart
    @POST("api/analisisdata/update/{id}")
    suspend fun updateAnalisisData(
        @Header("Authorization")
        bearerToken: String,

        @Path("id")
        id: Int,

        @Part("deskripsi")
        deskripsi: RequestBody
    ): Response<AnalisisDataApiResponse>

    @FormUrlEncoded
    @PUT("api/analisisdata/status/{id}")
    suspend fun updateStatusAnalisisData(
        @Header("Authorization")
        bearerToken: String,

        @Path("id")
        id: Int,

        @Field("status")
        status: String
    ): Response<AnalisisDataApiResponse>

    @DELETE("api/analisisdata/cancel/{id}")
    suspend fun cancelAnalisisData(
        @Header("Authorization")
        bearerToken: String,

        @Path("id")
        id: Int
    ): Response<AnalisisDataApiResponse>

    /* End of Analisis Data related Services */

    /* Notifikasi related Services */

    @GET("api/notifikasi")
    suspend fun getNotifikasi(
        @Header("Authorization")
        bearerToken: String
    ): Response<NotifikasiApiResponse>

    @GET("api/notifikasi/count")
    suspend fun getCountNotifikasi(
        @Header("Authorization")
        bearerToken: String
    ): Response<NotifikasiApiResponse>

    /* End of Notifikasi related Services */

    /* Chat related Services */

    @GET("api/chatroom")
    suspend fun getChatRooms(
        @Header("Authorization")
        bearerToken: String
    ): Response<ChatApiResponse>

    @GET("api/chatroom/{id}")
    suspend fun getChatRoom(
        @Header("Authorization")
        bearerToken: String,

        @Path("id")
        id: Int
    ): Response<ChatApiResponse>

    @FormUrlEncoded
    @POST("api/chatroom/store")
    suspend fun storeChatRoom(
        @Header("Authorization")
        bearerToken: String,

        @Field("id_user")
        idUser: Int,
    ): Response<ChatApiResponse>

    @FormUrlEncoded
    @POST("api/chatroom/store/{id}")
    suspend fun storeChat(
        @Header("Authorization")
        bearerToken: String,

        @Path("id")
        id: Int,

        @Field("pesan")
        pesan: String
    ): Response<ChatApiResponse>

    @FormUrlEncoded
    @PUT("api/chatroom/update/{id}")
    suspend fun updateChatRoom(
        @Header("Authorization")
        bearerToken: String,

        @Path("id")
        id: Int,

        @Field("is_automatic_deleted")
        isAutomaticDeleted: Int
    ): Response<ChatApiResponse>

    /* End of Chat related Services */
}