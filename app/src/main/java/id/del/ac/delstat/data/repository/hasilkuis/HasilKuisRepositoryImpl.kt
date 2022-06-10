package id.del.ac.delstat.data.repository.hasilkuis

import id.del.ac.delstat.data.model.kuis.HasilKuisApiResponse
import id.del.ac.delstat.data.repository.hasilkuis.datasource.HasilKuisRemoteDataSource
import id.del.ac.delstat.domain.repository.HasilKuisRepository

class HasilKuisRepositoryImpl(
    private val hasilKuisRemoteDataSource: HasilKuisRemoteDataSource
): HasilKuisRepository {

    override suspend fun getHasilKuis(bearerToken: String): HasilKuisApiResponse? {
        try {
            val response = hasilKuisRemoteDataSource.getHasilKuis(bearerToken)
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun storeHasilKuis(
        bearerToken: String,
        idKuis: Int,
        nilaiKuis: Double
    ): HasilKuisApiResponse? {
        try {
            val response = hasilKuisRemoteDataSource.storeHasilKuis(bearerToken, idKuis, nilaiKuis)
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun getDetailHasilKuis(
        bearerToken: String,
        idKuis: Int
    ): HasilKuisApiResponse? {
        try {
            val response = hasilKuisRemoteDataSource.getDetailHasilKuis(bearerToken, idKuis)
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}