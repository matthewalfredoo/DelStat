package id.del.ac.delstat.data.model.analisisdata


import com.google.gson.annotations.SerializedName

data class Errors(
    @SerializedName("deskripsi")
    val deskripsi: List<String?>? = null,

    @SerializedName("file")
    val file: List<String?>? = null,

    @SerializedName("judul")
    val judul: List<String?>? = null,
)