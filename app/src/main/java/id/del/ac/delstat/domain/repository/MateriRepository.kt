package id.del.ac.delstat.domain.repository

import id.del.ac.delstat.data.model.materi.MateriApiResponse

interface MateriRepository {

    suspend fun getMateri(id: Int): MateriApiResponse?
    suspend fun updateMateri(bearerToken: String, id: Int, linkVideo: String): MateriApiResponse?

}