package id.del.ac.delstat.data.repository.notifikasi.datasourceimpl

import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.model.notifikasi.NotifikasiApiResponse
import id.del.ac.delstat.data.repository.notifikasi.datasource.NotifikasiRemoteDataSource
import retrofit2.Response

class NotifikasiRemoteDataSourceImpl(
    private val delStatApiService: DelStatApiService
): NotifikasiRemoteDataSource {

    override suspend fun getNotifikasi(bearerToken: String): Response<NotifikasiApiResponse> {
        return delStatApiService.getNotifikasi(bearerToken)
    }

    override suspend fun getCountNotifikasi(bearerToken: String): Response<NotifikasiApiResponse> {
        return delStatApiService.getCountNotifikasi(bearerToken)
    }
}