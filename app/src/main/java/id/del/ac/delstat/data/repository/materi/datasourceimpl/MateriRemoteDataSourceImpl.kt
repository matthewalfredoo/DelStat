package id.del.ac.delstat.data.repository.materi.datasourceimpl

import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.model.materi.MateriApiResponse
import id.del.ac.delstat.data.repository.materi.datasource.MateriRemoteDataSource
import retrofit2.Response

class MateriRemoteDataSourceImpl(
    private val delStatApiService: DelStatApiService
) : MateriRemoteDataSource {

    override suspend fun getMateri(id: Int): Response<MateriApiResponse> {
        return delStatApiService.getMateri(id)
    }

    override suspend fun updateMateri(
        bearerToken: String,
        id: Int,
        linkVideo1: String,
        linkVideo2: String?,
        linkVideo3: String?,
        linkVideo4: String?,
    ): Response<MateriApiResponse> {
        return delStatApiService.updateMateri(bearerToken, id, linkVideo1, linkVideo2, linkVideo3, linkVideo4)
    }
}