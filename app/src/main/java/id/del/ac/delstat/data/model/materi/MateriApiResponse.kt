package id.del.ac.delstat.data.model.materi


import com.google.gson.annotations.SerializedName

data class MateriApiResponse(
    @SerializedName("code")
    val code: Int,

    @SerializedName("errors")
    val errors: Errors? = null,

    @SerializedName("materi")
    val materi: Materi? = null,

    @SerializedName("message")
    val message: String? = null
)