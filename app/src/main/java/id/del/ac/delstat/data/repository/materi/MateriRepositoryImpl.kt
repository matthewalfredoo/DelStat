package id.del.ac.delstat.data.repository.materi

import android.util.Log
import id.del.ac.delstat.data.model.materi.MateriApiResponse
import id.del.ac.delstat.data.repository.materi.datasource.MateriRemoteDataSource
import id.del.ac.delstat.domain.repository.MateriRepository

class MateriRepositoryImpl(private val materiRemoteDataSource: MateriRemoteDataSource) :
    MateriRepository {

    override suspend fun getMateri(id: Int): MateriApiResponse? {
        try {
            val response = materiRemoteDataSource.getMateri(id)
            return response.body()
        } catch (e: Exception) {
            Log.e("MateriRepositoryImpl", e.message.toString(), e)
        }
        return null
    }

    override suspend fun updateMateri(
        bearerToken: String,
        id: Int,
        linkVideo1: String,
        linkVideo2: String?,
        linkVideo3: String?,
        linkVideo4: String?,
    ): MateriApiResponse? {
        try {
            val response = materiRemoteDataSource.updateMateri(bearerToken, id, linkVideo1, linkVideo2, linkVideo3, linkVideo4)
            return response.body()
        } catch (e: Exception) {
            Log.e("MateriRepositoryImpl", e.message.toString(), e)
        }
        return null
    }
}