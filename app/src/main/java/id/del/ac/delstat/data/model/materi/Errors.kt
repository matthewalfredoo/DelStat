package id.del.ac.delstat.data.model.materi


import com.google.gson.annotations.SerializedName

data class Errors(
    @SerializedName("link_video")
    val linkVideo: List<String?>? = null
)