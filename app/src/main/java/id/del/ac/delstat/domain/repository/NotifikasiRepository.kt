package id.del.ac.delstat.domain.repository

import id.del.ac.delstat.data.model.notifikasi.NotifikasiApiResponse

interface NotifikasiRepository {

    suspend fun getNotifikasi(
        bearerToken: String
    ): NotifikasiApiResponse?

    suspend fun getCountNotifikasi(
        bearerToken: String
    ): NotifikasiApiResponse?

}