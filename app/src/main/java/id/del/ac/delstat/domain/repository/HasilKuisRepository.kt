package id.del.ac.delstat.domain.repository

import id.del.ac.delstat.data.model.kuis.HasilKuisApiResponse
import retrofit2.Response

interface HasilKuisRepository {

    suspend fun getHasilKuis(
        bearerToken: String
    ): HasilKuisApiResponse?

    suspend fun storeHasilKuis(
        bearerToken: String,
        idKuis: Int,
        nilaiKuis: Double
    ): HasilKuisApiResponse?

    suspend fun getDetailHasilKuis(
        bearerToken: String,
        idKuis: Int
    ): HasilKuisApiResponse?

}