package id.del.ac.delstat.data.model.materi


import com.google.gson.annotations.SerializedName

class Materi(
    @SerializedName("id")
    val id: Int,

    @SerializedName("id_user")
    val idUser: Int? = null,

    @SerializedName("link_video_1")
    val linkVideo1: String? = null,

    @SerializedName("link_video_2")
    val linkVideo2: String? = null,

    @SerializedName("link_video_3")
    val linkVideo3: String? = null,

    @SerializedName("link_video_4")
    val linkVideo4: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null
) {
    companion object{
        const val ID_MATERI_1_KONSEP_PELUANG = 1
        const val ID_MATERI_2_VARIABEL_ACAK = 2
        const val ID_MATERI_3_DISTRIBUSI_PROBABILITAS_DISKRIT = 3
        const val ID_MATERI_4_DISTRIBUSI_PROBABILITAS_KONTINU = 4
        const val ID_MATERI_5_PENGANTAR_STATISTIK_ANALISIS_DATA = 5
        const val ID_MATERI_6_TEKNIK_SAMPLING = 6
        const val ID_MATERI_7_ANOVA = 7
        const val ID_MATERI_8_KONSEP_ESTIMASI = 8
        const val ID_MATERI_9_PENGUJIAN_HIPOTESIS = 9
        const val ID_MATERI_10_REGRESI_KORELASI = 10
    }
}