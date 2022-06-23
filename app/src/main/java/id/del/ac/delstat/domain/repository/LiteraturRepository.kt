package id.del.ac.delstat.domain.repository

import id.del.ac.delstat.data.model.literatur.LiteraturApiResponse
import retrofit2.Response
import java.io.File

interface LiteraturRepository {

    suspend fun getLiteratur(
        judul: String? = null,
        tag: String? = null
    ): LiteraturApiResponse?

    suspend fun getDetailLiteratur(id: Int): LiteraturApiResponse?

    suspend fun storeLiteratur(
        bearerToken: String,
        judul: String,
        penulis: String,
        tahunTerbit: Int,
        tag: String,
        file: File? = null
    ): LiteraturApiResponse?

    suspend fun updateLiteratur(
        bearerToken: String,
        id: Int,
        judul: String,
        penulis: String,
        tahunTerbit: Int,
        tag: String,
        file: File? = null,
    ): LiteraturApiResponse?

    suspend fun deleteLiteratur(bearerToken: String, id: Int): LiteraturApiResponse?

}