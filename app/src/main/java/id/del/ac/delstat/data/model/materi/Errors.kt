package id.del.ac.delstat.data.model.materi


import com.google.gson.annotations.SerializedName

data class Errors(
    @SerializedName("link_video_1")
    val linkVideo1: List<String?>? = null,

    @SerializedName("link_video_2")
    val linkVideo2: List<String?>? = null,

    @SerializedName("link_video_3")
    val linkVideo3: List<String?>? = null,

    @SerializedName("link_video_4")
    val linkVideo4: List<String?>? = null
)