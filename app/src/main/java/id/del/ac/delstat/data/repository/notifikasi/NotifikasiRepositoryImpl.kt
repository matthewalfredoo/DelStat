package id.del.ac.delstat.data.repository.notifikasi

import android.util.Log
import id.del.ac.delstat.data.model.notifikasi.NotifikasiApiResponse
import id.del.ac.delstat.data.repository.notifikasi.datasource.NotifikasiRemoteDataSource
import id.del.ac.delstat.domain.repository.NotifikasiRepository

class NotifikasiRepositoryImpl(
    private val notifikasiRemoteDataSource: NotifikasiRemoteDataSource
): NotifikasiRepository {

    override suspend fun getNotifikasi(bearerToken: String): NotifikasiApiResponse? {
        try {
            val response = notifikasiRemoteDataSource.getNotifikasi(bearerToken)
            return response.body()
        } catch (e: Exception) {
            Log.e("NotifikasiRepository", e.message.toString(), e)
        }
        return null
    }

    override suspend fun getCountNotifikasi(bearerToken: String): NotifikasiApiResponse? {
        try {
            val response = notifikasiRemoteDataSource.getCountNotifikasi(bearerToken)
            return response.body()
        } catch (e: Exception) {
            Log.e("NotifikasiRepository", e.message.toString(), e)
        }
        return null
    }
}