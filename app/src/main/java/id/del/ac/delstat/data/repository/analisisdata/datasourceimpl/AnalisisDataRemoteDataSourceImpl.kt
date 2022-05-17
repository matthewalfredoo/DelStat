package id.del.ac.delstat.data.repository.analisisdata.datasourceimpl

import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.model.analisisdata.AnalisisDataApiResponse
import id.del.ac.delstat.data.repository.analisisdata.datasource.AnalisisDataRemoteDataSource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class AnalisisDataRemoteDataSourceImpl(
    private val delStatApiService: DelStatApiService
): AnalisisDataRemoteDataSource {

    override suspend fun getAnalisisData(bearerToken: String): Response<AnalisisDataApiResponse> {
        return delStatApiService.getAnalisisData(bearerToken)
    }

    override suspend fun getDetailAnalisisData(
        bearerToken: String,
        id: Int
    ): Response<AnalisisDataApiResponse> {
        return delStatApiService.getDetailAnalisisData(bearerToken, id)
    }

    override suspend fun storeAnalisisData(
        bearerToken: String,
        judul: String,
        deskripsi: String,
        file: File?
    ): Response<AnalisisDataApiResponse> {
        return delStatApiService.storeAnalisisData(
            bearerToken,
            requestBody(judul),
            requestBody(deskripsi),
            requestBody(file)
        )
    }

    override suspend fun updateAnalisisData(
        bearerToken: String,
        id: Int,
        judul: String,
        deskripsi: String,
        file: File?
    ): Response<AnalisisDataApiResponse> {
        return delStatApiService.updateAnalisisData(
            bearerToken,
            id,
            requestBody(judul),
            requestBody(deskripsi),
            requestBody(file)
        )
    }

    override suspend fun cancelAnalisisData(
        bearerToken: String,
        id: Int
    ): Response<AnalisisDataApiResponse> {
        return delStatApiService.cancelAnalisisData(bearerToken, id)
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

}