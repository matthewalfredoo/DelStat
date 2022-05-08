package id.del.ac.delstat.data.model.literatur


import com.google.gson.annotations.SerializedName

data class Literatur(
    @SerializedName("id")
    val id: Int,

    @SerializedName("id_user")
    val idUser: Int,

    @SerializedName("file")
    val file: String? = null,

    @SerializedName("judul")
    val judul: String? = null,

    @SerializedName("penulis")
    val penulis: String? = null,

    @SerializedName("tag")
    val tag: String? = null,

    @SerializedName("tahun_terbit")
    val tahunTerbit: Int = 0,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)