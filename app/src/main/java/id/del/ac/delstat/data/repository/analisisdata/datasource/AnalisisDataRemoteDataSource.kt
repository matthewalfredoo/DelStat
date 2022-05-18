package id.del.ac.delstat.data.repository.analisisdata.datasource

import id.del.ac.delstat.data.model.analisisdata.AnalisisDataApiResponse
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

interface AnalisisDataRemoteDataSource {

    suspend fun getAnalisisData(
        bearerToken: String
    ): Response<AnalisisDataApiResponse>

    suspend fun getDetailAnalisisData(
        bearerToken: String,
        id: Int
    ): Response<AnalisisDataApiResponse>

    suspend fun storeAnalisisData(
        bearerToken: String,
        judul: String,
        deskripsi: String,
        file: File? = null
    ): Response<AnalisisDataApiResponse>

    suspend fun updateAnalisisData(
        bearerToken: String,
        id: Int,
        deskripsi: String
    ): Response<AnalisisDataApiResponse>

    suspend fun updateStatusAnalisisData(
        bearerToken: String,
        id: Int,
        status: String
    ): Response<AnalisisDataApiResponse>

    suspend fun cancelAnalisisData(
        bearerToken: String,
        id: Int
    ): Response<AnalisisDataApiResponse>

}