package id.del.ac.delstat.data.model.notifikasi


import com.google.gson.annotations.SerializedName

data class NotifikasiApiResponse(
    @SerializedName("code")
    val code: Int = 500,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("notifikasi")
    val notifikasi: List<Notifikasi>? = null,

    @SerializedName("count_notifikasi")
    val countNotifikasi: Int? = null
)