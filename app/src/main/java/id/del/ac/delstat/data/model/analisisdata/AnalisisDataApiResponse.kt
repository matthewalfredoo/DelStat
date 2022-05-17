package id.del.ac.delstat.data.model.analisisdata


import com.google.gson.annotations.SerializedName

data class AnalisisDataApiResponse(
    @SerializedName("code")
    val code: Int = 500,

    @SerializedName("errors")
    val errors: Errors? = null,

    @SerializedName("analisisData")
    val analisisData: AnalisisData? = null,

    @SerializedName("listAnalisisData")
    val listAnalisisData: List<AnalisisData>? = null,

    @SerializedName("message")
    val message: String? = null
)