package id.del.ac.delstat.domain.repository

import id.del.ac.delstat.data.model.analisisdata.AnalisisDataApiResponse
import retrofit2.Response
import java.io.File

interface AnalisisDataRepository {

    suspend fun getAnalisisData(
        bearerToken: String
    ): AnalisisDataApiResponse?

    suspend fun getDetailAnalisisData(
        bearerToken: String,
        id: Int
    ): AnalisisDataApiResponse?

    suspend fun storeAnalisisData(
        bearerToken: String,
        judul: String,
        deskripsi: String,
        file: File? = null
    ): AnalisisDataApiResponse?

    suspend fun updateAnalisisData(
        bearerToken: String,
        id: Int,
        judul: String,
        deskripsi: String,
        file: File? = null
    ): AnalisisDataApiResponse?

    suspend fun cancelAnalisisData(
        bearerToken: String,
        id: Int
    ): AnalisisDataApiResponse?

}