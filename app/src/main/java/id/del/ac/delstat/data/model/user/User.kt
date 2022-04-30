package id.del.ac.delstat.data.model.user

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("email_verified_at")
    val emailVerifiedAt: String? = null,

    @SerializedName("foto_profil")
    val fotoProfil: String? = null,

    @SerializedName("id")
    val id: Int,

    @SerializedName("jenjang")
    val jenjang: String,

    @SerializedName("nama")
    val nama: String,

    @SerializedName("no_hp")
    val noHp: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("updated_at")
    val updatedAt: String
)