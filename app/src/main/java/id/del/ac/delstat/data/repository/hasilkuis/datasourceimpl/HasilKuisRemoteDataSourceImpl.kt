package id.del.ac.delstat.data.repository.hasilkuis.datasourceimpl

import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.model.kuis.HasilKuisApiResponse
import id.del.ac.delstat.data.repository.hasilkuis.datasource.HasilKuisRemoteDataSource
import retrofit2.Response

class HasilKuisRemoteDataSourceImpl(
    private val delStatApiService: DelStatApiService
): HasilKuisRemoteDataSource {

    override suspend fun getHasilKuis(bearerToken: String): Response<HasilKuisApiResponse> {
        return delStatApiService.getHasilKuis(bearerToken)
    }

    override suspend fun storeHasilKuis(
        bearerToken: String,
        idKuis: Int,
        nilaiKuis: Double
    ): Response<HasilKuisApiResponse> {
        return delStatApiService.storeHasilKuis(bearerToken, idKuis, nilaiKuis)
    }

    override suspend fun getDetailHasilKuis(
        bearerToken: String,
        idKuis: Int
    ): Response<HasilKuisApiResponse> {
        return delStatApiService.getDetailHasilKuis(bearerToken, idKuis)
    }
}