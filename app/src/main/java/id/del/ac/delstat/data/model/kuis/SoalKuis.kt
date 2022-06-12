package id.del.ac.delstat.data.model.kuis

import android.content.res.Resources

class SoalKuis(
    var id: Int? = null,
    var soal: String? = null,
    var keterangan: String? = null,
    var gambar: Int? = null,
    var video: String? = null,
    var audio: String? = null,

    var jawabanBenar: String? = null,
    var jawabanBenarResources: Int? = null,
    var listPilihanJawaban: List<String>? = null,
    var listPilihanJawabanResources: List<Int>? = null,

    var jawabanDipilih: String? = null
) {

    companion object{
        const val PILIHAN_A = 0
        const val PILIHAN_B = 1
        const val PILIHAN_C = 2
        const val PILIHAN_D = 3
    }

}