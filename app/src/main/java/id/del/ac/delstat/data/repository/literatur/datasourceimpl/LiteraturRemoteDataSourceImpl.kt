package id.del.ac.delstat.data.repository.literatur.datasourceimpl

import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.model.literatur.LiteraturApiResponse
import id.del.ac.delstat.data.repository.literatur.datasource.LiteraturRemoteDataSource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class LiteraturRemoteDataSourceImpl(
    private val delStatApiService: DelStatApiService
) : LiteraturRemoteDataSource {

    override suspend fun getLiteratur(
        judul: String?,
        tag: String?
    ): Response<LiteraturApiResponse> {
        return delStatApiService.getLiteratur(judul, tag)
    }

    override suspend fun getDetailLiteratur(id: Int): Response<LiteraturApiResponse> {
        return delStatApiService.getDetailLiteratur(id)
    }

    override suspend fun storeLiteratur(
        bearerToken: String,
        judul: String,
        penulis: String,
        tahunTerbit: Int,
        tag: String,
        file: File?
    ): Response<LiteraturApiResponse> {
        return delStatApiService.storeLiteratur(
            bearerToken,
            requestBody(judul),
            requestBody(penulis),
            requestBody(tahunTerbit),
            requestBody(tag),
            requestBody(file)
        )
    }

    override suspend fun updateLiteratur(
        bearerToken: String,
        id: Int,
        judul: String,
        penulis: String,
        tahunTerbit: Int,
        tag: String,
        file: File?
    ): Response<LiteraturApiResponse> {
        return delStatApiService.updateLiteratur(
            bearerToken,
            id,
            requestBody(judul),
            requestBody(penulis),
            requestBody(tahunTerbit),
            requestBody(tag),
            requestBody(file)
        )
    }

    override suspend fun deleteLiteratur(
        bearerToken: String,
        id: Int
    ): Response<LiteraturApiResponse> {
        return delStatApiService.deleteLiteratur(bearerToken, id)
    }

    private fun requestBody(file: File?): MultipartBody.Part? {
        if (file == null) {
            return null
        }
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }

    private fun requestBody(field: String): RequestBody {
        return field.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun requestBody(field: Int): RequestBody {
        return field.toString().toRequestBody("text/plain".toMediaTypeOrNull())
    }

}