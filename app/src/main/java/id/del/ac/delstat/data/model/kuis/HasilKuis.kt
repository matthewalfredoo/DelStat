package id.del.ac.delstat.data.model.kuis


import com.google.gson.annotations.SerializedName

data class HasilKuis(
    @SerializedName("id")
    val id: Int,

    @SerializedName("id_kuis")
    val idKuis: Int,

    @SerializedName("id_user")
    val idUser: Int,

    @SerializedName("nama_user")
    val namaUser: String,

    @SerializedName("nilai_kuis")
    val nilaiKuis: Int,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)