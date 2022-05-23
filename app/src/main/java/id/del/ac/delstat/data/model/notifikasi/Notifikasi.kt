package id.del.ac.delstat.data.model.notifikasi


import com.google.gson.annotations.SerializedName

class Notifikasi(
    @SerializedName("id")
    val id: Int,

    @SerializedName("id_analisis_data")
    val idAnalisisData: Int? = null,

    @SerializedName("id_literatur")
    val idLiteratur: Int? = null,

    @SerializedName("id_user")
    val idUser: Int,

    @SerializedName("jenis_notifikasi")
    val jenisNotifikasi: String? = null,

    @SerializedName("deskripsi")
    val deskripsi: String? = null,

    @SerializedName("read_at")
    val readAt: String? = null,

    @SerializedName("sudah_dibaca")
    val sudahDibaca: Boolean,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
) {

    companion object {
        const val NOTIFIKASI_LITERATUR_BARU = "Literatur Baru"
        const val NOTIFIKASI_ANALISIS_DATA_BARU = "Request Analisis Data Baru"
    }

}