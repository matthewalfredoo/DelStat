package id.del.ac.delstat.data.model.analisisdata

import com.google.gson.annotations.SerializedName
import id.del.ac.delstat.data.model.user.User

class AnalisisData(
    @SerializedName("id")
    val id: Int,

    @SerializedName("deskripsi")
    val deskripsi: String? = null,

    @SerializedName("file")
    val file: String? = null,

    @SerializedName("id_user")
    val idUser: Int,

    @SerializedName("judul")
    val judul: String? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    @SerializedName("user")
    val user: User? = null
) {
    companion object {
        const val STATUS_DIPESAN = "Dipesan"
        const val STATUS_DIBATALKAN = "Dibatalkan"
        const val STATUS_DIPROSES = "Diproses"
        const val STATUS_DITOLAK = "Ditolak"
        const val STATUS_SELESAI = "Selesai"
    }
}