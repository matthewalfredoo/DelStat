package id.del.ac.delstat.data.model.user


import com.google.gson.annotations.SerializedName

data class Errors(
    @SerializedName("email")
    val email: List<String?>? = null,

    @SerializedName("jenjang")
    val jenjang: List<String?>? = null,

    @SerializedName("nama")
    val nama: List<String?>? = null,

    @SerializedName("no_hp")
    val noHp: List<String?>? = null,

    @SerializedName("password")
    val password: List<String?>? = null,
)