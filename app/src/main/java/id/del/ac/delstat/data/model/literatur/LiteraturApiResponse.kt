package id.del.ac.delstat.data.model.literatur


import com.google.gson.annotations.SerializedName

data class LiteraturApiResponse(
    @SerializedName("code")
    val code: Int = 500,

    @SerializedName("errors")
    val errors: Errors? = null,

    @SerializedName("literatur_list")
    val literaturList: List<Literatur>? = null,

    @SerializedName("literatur")
    val literatur: Literatur? = null,

    @SerializedName("message")
    val message: String? = null
)