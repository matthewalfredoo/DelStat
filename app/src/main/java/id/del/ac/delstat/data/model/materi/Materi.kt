package id.del.ac.delstat.data.model.materi


import com.google.gson.annotations.SerializedName

data class Materi(
    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("id_user")
    val idUser: Int,

    @SerializedName("link_video")
    val linkVideo: String,

    @SerializedName("updated_at")
    val updatedAt: String
)