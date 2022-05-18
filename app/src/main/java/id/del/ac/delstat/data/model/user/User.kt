package id.del.ac.delstat.data.model.user

import com.google.gson.annotations.SerializedName

class User(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("no_hp")
    val noHp: String? = null,

    @SerializedName("nama")
    val nama: String? = null,

    @SerializedName("foto_profil")
    val fotoProfil: String? = null,

    @SerializedName("role")
    val role: String? = null,

    @SerializedName("jenjang")
    val jenjang: String? = null,

    @SerializedName("email_verified_at")
    val emailVerifiedAt: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    @SerializedName("api_token")
    val apiToken: String? = null
) {
    companion object {
        const val ROLE_ADMIN = "Admin"
        const val ROLE_DOSEN = "Dosen"
        const val ROLE_SISWA = "Siswa"
    }
}