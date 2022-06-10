package id.del.ac.delstat.data.model.kuis


import com.google.gson.annotations.SerializedName

data class HasilKuisApiResponse(
    @SerializedName("code")
    val code: Int = 500,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("hasilKuis")
    val hasilKuis: List<HasilKuis>? = null
)