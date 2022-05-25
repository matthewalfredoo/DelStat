package id.del.ac.delstat.data.model.user


import com.google.gson.annotations.SerializedName

data class UserApiResponse(
    @SerializedName("code")
    val code: Int = 500,

    @SerializedName("errors")
    val errors: Errors? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("token")
    val token: String? = null,

    @SerializedName("user")
    val user: User? = null,

    @SerializedName("list_user")
    val listUser: List<User>? = null
)