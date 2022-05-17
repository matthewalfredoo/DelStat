package id.del.ac.delstat.data.repository.analisisdata

import id.del.ac.delstat.data.model.analisisdata.AnalisisDataApiResponse
import id.del.ac.delstat.data.repository.analisisdata.datasource.AnalisisDataRemoteDataSource
import id.del.ac.delstat.domain.repository.AnalisisDataRepository
import java.io.File

class AnalisisDataRepositoryImpl(
    private val analisisDataRemoteDataSource: AnalisisDataRemoteDataSource
): AnalisisDataRepository {
    override suspend fun getAnalisisData(bearerToken: String): AnalisisDataApiResponse? {
        try {
            val response = analisisDataRemoteDataSource.getAnalisisData(bearerToken)
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun getDetailAnalisisData(
        bearerToken: String,
        id: Int
    ): AnalisisDataApiResponse? {
        try {
            val response = analisisDataRemoteDataSource.getDetailAnalisisData(bearerToken, id)
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun storeAnalisisData(
        bearerToken: String,
        judul: String,
        deskripsi: String,
        file: File?
    ): AnalisisDataApiResponse? {
        try {
            val response = analisisDataRemoteDataSource.storeAnalisisData(bearerToken, judul, deskripsi, file)
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun updateAnalisisData(
        bearerToken: String,
        id: Int,
        judul: String,
        deskripsi: String,
        file: File?
    ): AnalisisDataApiResponse? {
        try {
            val response = analisisDataRemoteDataSource.updateAnalisisData(bearerToken, id, judul, deskripsi, file)
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override suspend fun cancelAnalisisData(bearerToken: String, id: Int): AnalisisDataApiResponse? {
        try {
            val response = analisisDataRemoteDataSource.cancelAnalisisData(bearerToken, id)
            return response.body()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}