package id.del.ac.delstat.data.model.literatur


import com.google.gson.annotations.SerializedName

data class Errors(
    @SerializedName("file")
    val file: List<String?>? = null,

    @SerializedName("judul")
    val judul: List<String?>? = null,

    @SerializedName("penulis")
    val penulis: List<String?>? = null,

    @SerializedName("tag")
    val tag: List<String?>? = null,

    @SerializedName("tahun_terbit")
    val tahunTerbit: List<String?>? = null
)