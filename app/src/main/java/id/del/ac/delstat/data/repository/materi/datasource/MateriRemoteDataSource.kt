package id.del.ac.delstat.data.repository.materi.datasource

import id.del.ac.delstat.data.model.materi.MateriApiResponse
import retrofit2.Response

interface MateriRemoteDataSource {

    suspend fun getMateri(id: Int): Response<MateriApiResponse>
    suspend fun updateMateri(bearerToken: String, id: Int, linkVideo: String): Response<MateriApiResponse>

}