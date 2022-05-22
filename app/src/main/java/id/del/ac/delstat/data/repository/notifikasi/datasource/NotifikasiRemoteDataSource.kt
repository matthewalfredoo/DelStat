package id.del.ac.delstat.data.repository.notifikasi.datasource

import id.del.ac.delstat.data.model.notifikasi.NotifikasiApiResponse
import retrofit2.Response

interface NotifikasiRemoteDataSource {

    suspend fun getNotifikasi(
        bearerToken: String
    ): Response<NotifikasiApiResponse>

    suspend fun getCountNotifikasi(
        bearerToken: String
    ): Response<NotifikasiApiResponse>

}