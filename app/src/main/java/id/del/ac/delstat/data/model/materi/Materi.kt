package id.del.ac.delstat.data.model.materi


import com.google.gson.annotations.SerializedName

data class Materi(
    @SerializedName("id")
    val id: Int,

    @SerializedName("id_user")
    val idUser: Int? = null,

    @SerializedName("link_video_1")
    val linkVideo1: String? = null,

    @SerializedName("link_video_2")
    val linkVideo2: String? = null,

    @SerializedName("link_video_3")
    val linkVideo3: String? = null,

    @SerializedName("link_video_4")
    val linkVideo4: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
)