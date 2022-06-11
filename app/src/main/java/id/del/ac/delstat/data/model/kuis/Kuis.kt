package id.del.ac.delstat.data.model.kuis

class Kuis(
    var id: Int? = null,
    var nama: String? = null,
    var deskripsi: String? = null,
    var listSoal: List<SoalKuis>? = null
) {

}