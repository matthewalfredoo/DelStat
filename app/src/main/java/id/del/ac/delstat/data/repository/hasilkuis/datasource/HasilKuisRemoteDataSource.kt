package id.del.ac.delstat.data.repository.hasilkuis.datasource

import id.del.ac.delstat.data.model.kuis.HasilKuisApiResponse
import retrofit2.Response

interface HasilKuisRemoteDataSource {

    suspend fun getHasilKuis(
        bearerToken: String
    ): Response<HasilKuisApiResponse>

    suspend fun storeHasilKuis(
        bearerToken: String,
        idKuis: Int,
        nilaiKuis: Double
    ): Response<HasilKuisApiResponse>

    suspend fun getDetailHasilKuis(
        bearerToken: String,
        idKuis: Int
    ): Response<HasilKuisApiResponse>

}