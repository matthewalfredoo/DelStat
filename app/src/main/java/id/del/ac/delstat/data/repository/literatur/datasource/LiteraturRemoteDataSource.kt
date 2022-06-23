package id.del.ac.delstat.data.repository.literatur.datasource

import id.del.ac.delstat.data.model.literatur.LiteraturApiResponse
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

interface LiteraturRemoteDataSource {

    suspend fun getLiteratur(
        judul: String? = null,
        tag: String? = null
    ): Response<LiteraturApiResponse>

    suspend fun getDetailLiteratur(id: Int): Response<LiteraturApiResponse>

    suspend fun storeLiteratur(
        bearerToken: String,
        judul: String,
        penulis: String,
        tahunTerbit: Int,
        tag: String,
        file: File? = null
    ): Response<LiteraturApiResponse>

    suspend fun updateLiteratur(
        bearerToken: String,
        id: Int,
        judul: String,
        penulis: String,
        tahunTerbit: Int,
        tag: String,
        file: File? = null,
    ): Response<LiteraturApiResponse>

    suspend fun deleteLiteratur(bearerToken: String, id: Int): Response<LiteraturApiResponse>

}