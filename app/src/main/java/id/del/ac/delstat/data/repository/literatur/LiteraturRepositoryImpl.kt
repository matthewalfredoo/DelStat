package id.del.ac.delstat.data.repository.literatur

import id.del.ac.delstat.data.model.literatur.LiteraturApiResponse
import id.del.ac.delstat.data.repository.literatur.datasource.LiteraturRemoteDataSource
import id.del.ac.delstat.domain.repository.LiteraturRepository
import java.io.File

class LiteraturRepositoryImpl(
    private val literaturRemoteDataSource: LiteraturRemoteDataSource
) : LiteraturRepository {

    override suspend fun getLiteratur(
        judul: String?,
        tag: String?
    ): LiteraturApiResponse? {
        try {
            val response = literaturRemoteDataSource.getLiteratur(judul, tag)
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun getDetailLiteratur(id: Int): LiteraturApiResponse? {
        try {
            val response = literaturRemoteDataSource.getDetailLiteratur(id)
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun storeLiteratur(
        bearerToken: String,
        judul: String,
        penulis: String,
        tahunTerbit: Int,
        tag: String,
        file: File?
    ): LiteraturApiResponse? {
        try {
            val response = literaturRemoteDataSource.storeLiteratur(
                bearerToken,
                judul,
                penulis,
                tahunTerbit,
                tag,
                file
            )
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun updateLiteratur(
        bearerToken: String,
        id: Int,
        judul: String,
        penulis: String,
        tahunTerbit: Int,
        tag: String,
        file: File?
    ): LiteraturApiResponse? {
        try {
            val response = literaturRemoteDataSource.updateLiteratur(
                bearerToken,
                id,
                judul,
                penulis,
                tahunTerbit,
                tag,
                file
            )
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun deleteLiteratur(bearerToken: String, id: Int): LiteraturApiResponse? {
        try {
            val response = literaturRemoteDataSource.deleteLiteratur(bearerToken, id)
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}