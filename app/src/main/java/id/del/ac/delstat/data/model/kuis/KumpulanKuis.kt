package id.del.ac.delstat.data.model.kuis

import id.del.ac.delstat.R
import id.del.ac.delstat.data.model.materi.Materi

object KumpulanKuis {

    /* Kuis 1 */
    val soalKuis1_1 = SoalKuis(
        id = 1,
        soal = "Suatu percobaan terdiri atas lantunan suatu mata uang logam. " +
                "Jika pada lantunan tersebut muncul angka, maka uang logam tersebut dilontarkan kembali. " +
                "Namun jika muncul gambar, maka sebuah dadu akan digulirkan sekali. Tentukan ruang sampelnya.",
        jawabanBenar = "8",
        listPilihanJawaban = listOf(
            "16",
            "8",
            "6",
            "12"
        )
    )
    val soalKuis1_2 = SoalKuis(
        id = 2,
        soal = "Misalkan tiga barang dipilih secara acak dari hasil suatu pabrik. " +
                "Tiga barang diperiksa dan digolongkan sebagai cacat (C) atau tidak cacat (B). " +
                "Tentukan ruang sampelnya",
        jawabanBenar = "8",
        listPilihanJawaban = listOf(
            "8",
            "16",
            "24",
            "36"
        )
    )
    val soalKuis1_3 = SoalKuis(
        id = 3,
        soal = "Berapa macam hidangan dapat disajikan bila masing-masing hidangan dapat terdiri atas sop, nasi goring, bakmi dan soto. " +
                "Bila tersedia 4 macam  sop, 3 macam nasi goreng, 5 macam bakmi dan 4 macam soto?",
        jawabanBenar = "240",
        listPilihanJawaban = listOf(
            "112",
            "200",
            "240",
            "360"
        )
    )
    val soalKuis1_4 = SoalKuis(
        id = 4,
        soal = "Dari 20 lotere, dua diambil untuk hadiah pertama dan kedua. Banyaknya titik sampel dalam T adalah",
        jawabanBenar = "380",
        listPilihanJawaban = listOf(
            "200",
            "260",
            "360",
            "380"
        )
    )
    val soalKuis1_5 = SoalKuis(
        id = 5,
        soal = "Terdapat 9 bola lampu yang akan dirangkai seri. " +
                "Ada beberapa cara menyusun 9 bola lampu itu bila 3 diantaranya berwarna merah, 4 kuning, 2 biru?",
        jawabanBenar = "1260",
        listPilihanJawaban = listOf(
            "200",
            "380",
            "1260",
            "2000"
        )
    )

    val listSoalKuis1 = listOf(soalKuis1_1, soalKuis1_2, soalKuis1_3, soalKuis1_4, soalKuis1_5)

    val kuis1 = Kuis(
        id = Materi.ID_MATERI_1_KONSEP_PELUANG,
        nama = "Kuis Konsep Peluang",
        deskripsi = "Kuis ini berisikan soal tentang konsep peluang",
        listSoal = listSoalKuis1
    )
    /* End of Kuis 1 */

    /* Kuis 2 */
    val soalKuis2_1 = SoalKuis(
        id = 1,
        soal = "1.C adalah kejadian sederhana peci Pak Ali, Badu, dan Cokro " +
                "jika diberi bobot yang sama dan nilai c adalah jumlah urutan yang cocok maka peluangnya adalah",
        jawabanBenar = "P(C=0) = 1/3, P(C=1) = ½, P(C=2) = 1/6",
        listPilihanJawaban = listOf(
            "P(C=0) = 1/3, P(C=1) = ½, P(C=2) = 1/6",
            "P(C=0) = 1/4, P(C=1) = ½, P(C=2) = 1/6",
            "P(C=0) = 1/3, P(C=1) = ½, P(C=2) = 1/5",
            "P(C=0) = 1/2, P(C=1) = ½, P(C=2) = 1/6"
        )
    )
    val soalKuis2_2 = SoalKuis(
        id = 2,
        soal = "2. Suatu pengiriman 6 notebook ke IT Del terdapat 2 yang cacat " +
               "Jika Duktek memilih 4 secara acak, maka banyaknya distribusi peluang yang cacat adalah ",
        jawabanBenar = "f(0) = 1/15, f(1) = 8/15, f(2) = 6/15",
        listPilihanJawaban = listOf(
            "f(0) = 1/15, f(1) = 8/15, f(2) = 6/15",
            "f(0) = 1/15, f(1) = 4/15, f(2) = 6/15",
            "f(0) = 1/15, f(1) = 8/15, f(2) = 3/15",
            "f(0) = 1/15, f(1) = 1/15, f(2) = 6/15"
        )
    )
    val soalKuis2_3 = SoalKuis(
        id = 3,
        soal = "3. Jika diketahui X=x{3,4,5,6} " +
               "f(x) = 1/3, k/9, 2k+1/18, 1/6 maka nilai K adalah",
        jawabanBenar = "2",
        listPilihanJawaban = listOf(
            "1",
            "2",
            "3",
            "4"
        )
    )
    val soalKuis2_4 = SoalKuis(
        id = 4,
        soal = "4. Berdasarkan soal nomor 3, nilai P(x ≤ 5) adalah",
        jawabanBenar = "5/6",
        listPilihanJawaban = listOf(
            "1/3",
            "2/9",
            "5/18",
            "5/6"
        )
    )
    val soalKuis2_5 = SoalKuis(
        id = 5,
        soal = "5. Diketahui sebuah variabel acak kontinu X dengan fungsi f(x) = 2/27 (x + 1), " +
               "mengambil nilai x antara 2 dan 4. Nilai probabilitas untuk (X < 2,5) adalah",
        jawabanBenar = "13/108",
        listPilihanJawaban = listOf(
            "300/108",
            "13/108",
            "100/13",
            "108/13"
        )
    )
    val kuis2 = Kuis(
        id = Materi.ID_MATERI_2_VARIABEL_ACAK,
        nama = "Kuis Variabel Acak",
        deskripsi = "Kuis ini berisikan soal variabel acak",
        listSoal = listOf(soalKuis2_1, soalKuis2_2, soalKuis2_3, soalKuis2_4, soalKuis2_5)
    )
    /* End of Kuis 2 */

    /* Kuis 3 - Distribusi Probabilitas Diskrit (Joshua) */
    val soalKuis3_1 = SoalKuis(
        id = 1,
        soal = "1. Jika sebuah dadu dilempar, maka setiap elemen dari ruang sampelnya S = {1,2,3,4,5,6} " +
               "terjadi dengan peluang yang sama 1/6 maka rataan dan variansinya adalah",
        jawabanBenar = "µ = 3,5 dan σ^2 = 35/12",
        listPilihanJawaban = listOf(
            "µ = 3,5 dan σ^2 = 30/12",
            "µ = 4,5 dan σ^2 = 35/12",
            "µ = 3,5 dan σ^2 = 35/12",
            "µ = 3,5 dan σ^2 = 35/6"
        )
    )
    val soalKuis3_2 = SoalKuis(
        id = 2,
        soal = "2. Sebuah mata uang dilambungkan satu kali dicatat bahwa hasilnya yang " +
               "muncul angka “A” dan gambar “G”. Berapa peluang yang muncul angka?",
        jawabanBenar = "0,5",
        listPilihanJawaban = listOf(
            "1",
            "0,5",
            "2",
            "1,5"
        )
    )
    val soalKuis3_3 = SoalKuis(
        id = 3,
        soal = "3. Bila dua dadu dilemparkan 6 kali, berapakah peluang mendapat jumlah 7 atau 11 muncul 2 kali, " +
               "sepasang bilangan yang sama satu kali dan kombinasi lainnya 3 kali?",
        jawabanBenar = "0,1127",
        listPilihanJawaban = listOf(
            "0,1127",
            "0,115",
            "0,1345",
            "0,2762"
        )
    )
    val soalKuis3_4 = SoalKuis(
        id = 4,
        soal = "4. Dari suatu kotak yang berisi 40 buah komponnen, 3 di antaranya rusak. Jika diambil " +
               "secara acak 5 buah komponen, tentukan peluang sampel tersebut berisi 1 komponen rusak",
        jawabanBenar = "0,3011",
        listPilihanJawaban = listOf(
            "0,3011",
            "0,204",
            "0,123",
            "0,112"
        )
    )
    val soalKuis3_5 = SoalKuis(
        id = 5,
        soal = "5. Dalam sebuah penelitian terdapat 10 orang yang dijadikan sampel, " +
               "3 diantara mereka bergolongan darah O, 4 bergolongan darah A dan 3 bergolongan darah B. " +
               "Berapakah peluang suatu sampel acak ukuran 5 beranggotakan 1 orang bergolongan darah O, " +
               "2 orang bergolongan darah A dan 2 orang lagi bergolongan darh B?",
        jawabanBenar = "3/14",
        listPilihanJawaban = listOf(
            "2/5",
            "3/14",
            "2/10",
            "3/11"
        )
    )
    val kuis3 = Kuis(
        id = Materi.ID_MATERI_3_DISTRIBUSI_PROBABILITAS_DISKRIT,
        nama = "Kuis Distribusi Probabilitas Diskrit",
        deskripsi = "Kuis ini berisikan soal distribusi probabilitas diskrit",
        listSoal = listOf(soalKuis3_1, soalKuis3_2, soalKuis3_3, soalKuis3_4, soalKuis3_5)
    )
    /* End of Kuis 3 */


    /* Kuis 4 - Distribusi Probabilitas Kontinu (Joshua) */
    val soalKuis4_1 = SoalKuis(
        id = 1,
        soal = "1. Misalkan X adalah variable acak yang mewakili waktu penerbangan pesawat terbang " +
               "perjalanan dari Jakarta ke Silangit. Dalam kondisi normal, waktu penerbangan antara " +
               "120 sampai 140 menit. Berapa peluang bahwa waktu kedatangan akan antara 120 dan 130 menit?",
        jawabanBenar = "1/2",
        listPilihanJawaban = listOf(
            "1/4",
            "1/3",
            "1/2",
            "1/6"
        )
    )
    val soalKuis4_2 = SoalKuis(
        id = 2,
        soal = "2. Suatu jenis baterai mobil rata-rata 2 tahun dengan simpangan baku 0,5 tahun. Bila dianggap " +
               "umur baterai berdistribusi normal, carilah peluang baterai akan berumur kurang dari 2,3 tahun.",
        jawabanBenar = "0,08808",
        listPilihanJawaban = listOf(
            "0,567",
            "0,765",
            "0,0808",
            "0,211"
        )
    )
    val soalKuis4_3 = SoalKuis(
        id = 3,
        soal = "3. Dalam suatu proses industry, diameter suatu batang merupakan bagian yang penting. " +
        "Pembeli menetapkan ketentuan mengenai diameternya, yakni sebesar 3,0 ± 0,01 cm. " +
        "Maksudnya adalah tidak ada batang yang ukurannya diluar ketentuan akan diterima. " +
        "Diketahui bahwa proses pembuatan diameter tersebut berdistribusi normal dengan " +
        "rataan 3,0 dan simpangan baku 0,005. Berapa banyaknya rata-rata batang yang akan terbuang?",
        jawabanBenar = "0,9544",
        listPilihanJawaban = listOf(
            "0,9544",
            "0,8777",
            "0,1143",
            "0,2762"
        )
    )
    val soalKuis4_4 = SoalKuis(
        id = 4,
        soal = "4. Waktu yang dibutuhkan untuk menyelesaikan kuis adalah 30-40 menit. Semua mahasiswa dianggap " +
               "mempunyai kemampuan yang sama (maka peluang sama). Berapa peluang antara 34-38 menit?",
        jawabanBenar = "2/5",
        listPilihanJawaban = listOf(
            "3/4",
            "5/6",
            "2/5",
            "1/10"
        )
    )
    val soalKuis4_5 = SoalKuis(
        id = 5,
        soal = "5. Distribusi dari pekerja menyelesaikan suatu pekerjaan rata-rata 8 menit dengan standar deviasi 2 menit. " +
               "Tentukan peluang waktu yang dibutuhkan untuk menyelesaikan setiap pekerjaan lebih dari 9 menit?",
        jawabanBenar = "0,3085",
        listPilihanJawaban = listOf(
            "0,5110",
            "0,6778",
            "0,2601",
            "0,3085"
        )
    )
    val kuis4 = Kuis(
        id = Materi.ID_MATERI_4_DISTRIBUSI_PROBABILITAS_KONTINU,
        nama = "Kuis Distribusi Probabilitas Kontinu",
        deskripsi = "Kuis ini berisikan soal distribusi probabilitas kontinu",
        listSoal = listOf(soalKuis4_1, soalKuis4_2, soalKuis4_3, soalKuis4_4, soalKuis4_5)
    )
    /* End of Kuis 4 */

    /* Kuis 5 - Statistika dan Analisis Data (Matthew) */
    val soalKuis5_1 = SoalKuis(
        id = 1,
        soal = "1.\tBerikut adalah data usia sekelompok (dalam tahun) " +
                "25 42 43 37 35 41 35 28 37 36 29 27 36 44 43 38 25 34 31 45 " +
                "maka banyaknya kelas interval dan panjang kelas interval adalah",
        jawabanBenar = "K = 6 dan C = 4",
        listPilihanJawaban = listOf(
            "K = 6 dan C = 4",
            "K = 4 dan C = 2",
            "K = 2 dan C = 4",
            "K = 4 dan C = 3"
        )
    )
    val soalKuis5_2 = SoalKuis(
        id = 2,
        keterangan = "Diberikan data seperti tabel berikut ini",
        gambar = R.drawable.kuis_pengantar_statistika_soal_2,
        soal = "Maka kelas modusnya adalah",
        jawabanBenar = "55.21",
        listPilihanJawaban = listOf(
            "48",
            "55.21",
            "31",
            "19"
        )
    )
    val soalKuis5_3 = SoalKuis(
        id = 3,
        soal = "Diberikan data tunggal 4, 3, 2, 4, 5 maka kuartil dari data tersebut adalah",
        jawabanBenar = "2.5",
        listPilihanJawaban = listOf(
            "2.5",
            "3.0",
            "1.3",
            "1.14"
        )
    )
    val soalKuis5_4 = SoalKuis(
        id = 4,
        keterangan = "Diberikan data seperti pada tabel berikut ini.",
        gambar = R.drawable.kuis_pengantar_statistika_soal_4,
        soal = "Maka varians dari data tersebut adalah",
        jawabanBenar = "38.56",
        listPilihanJawaban = listOf(
            "52.8",
            "22.4",
            "45.23",
            "38.56"
        )
    )
    val soalKuis5_5 = SoalKuis(
        id = 5,
        soal = "Berdasarkan tabel pada soal nomor 4 maka standar deviasinya adalah",
        jawabanBenar = "6.2",
        listPilihanJawaban = listOf(
            "6.2",
            "4.5",
            "6.8",
            "7"
        )
    )
    val kuis5 = Kuis(
        id = Materi.ID_MATERI_5_PENGANTAR_STATISTIK_ANALISIS_DATA,
        nama = "Kuis Pengantar Statistika dan Analisis Data",
        deskripsi = "Kuis ini berisikan soal pengantar statistika dan analisis data",
        listSoal = listOf(soalKuis5_1, soalKuis5_2, soalKuis5_3, soalKuis5_4, soalKuis5_5)
    )
    /* End of Kuis 5 */

    /* Kuis 6 - Teknik Sampling (Joshua) */
    val soalKuis6_1 = SoalKuis(
        id = 1,
        soal = "1. Suatu perusahaan memproduksi bola lampu yang umumnya berdistribusi hampir normal " +
               "dengan rataan 800 jam dan simpangan baku 40 jam. Hitunglah peluangnya bahwa suatu " +
               "sampel acak dengan 16 bola lampu akan mempunyai umur rata-rata kurang dari 775 jam?",
        jawabanBenar = "0,0062",
        listPilihanJawaban = listOf(
            "0,0062",
            "0,004",
            "0,0015",
            "0,012"
        )
    )
    val soalKuis6_2 = SoalKuis(
        id = 2,
        soal = "2. Suatu pabrik baterai mobil menjamin bahwa baterainya akan tahan rata-rata 3 tahun " +
               "dengan simpangan baku 1 tahun. Bila lima baterainya tahan 11,9; 2,4; 3,0; 3,5; 4,2 tahun, " +
               "apakah pembuatanya masih yakin bahwa simpangan baku baterai tersebut adalah 1 tahun?",
        jawabanBenar = "Ya, karena standard deviasinya 1",
        listPilihanJawaban = listOf(
            "Ya, karena standard deviasinya 1",
            "Tidak, karena standar deviasinya 1",
            "Ya, karena standard deviasinya 2",
            "Tidak, karena standard deviasinya 2"
        )
    )
    val soalKuis6_3 = SoalKuis(
        id = 3,
        soal = "3. Populasi penelitian adalah 800 orang, maka jumlah sampel dengan rumus Slovin adalah",
        jawabanBenar = "267",
        listPilihanJawaban = listOf(
            "700",
            "200",
            "267",
            "250"
        )
    )
    val soalKuis6_4 = SoalKuis(
        id = 4,
        soal = "4. Seorang peneliti akan meneliti hubungan antara karakteristik social ekonomi dengan " +
               "perilaku hidup bersih dan sehat. Menurut referensi yang ada didapatkan informasi bahwa " +
               "tingkat pendidikan berpengaruh terhadap kebiasaan perilaku hidup bersih dan sehat. " +
               "Oleh karena itu, peneliti akan mengelompokkan populasi penelitian berdasarkan tingkat " +
               "pendidikan formal, yaitu rendah, menengah, dan tinggi. Metode sampling yang paling tepat digunakan adalah",
        jawabanBenar = "Cluster random sampling",
        listPilihanJawaban = listOf(
            "Simple random sampling",
            "Systematic random sampling",
            "Stratified random sampling",
            "Cluster random sampling"
        )
    )
    val soalKuis6_5 = SoalKuis(
        id = 5,
        soal = "5.	Misalkan hypothesis yang ingin diuji untuk berat mahasiswa di fakultas tertentu H0 : µ = 68 kg, " +
               "H1 µ > 68 kg, Taraf keberartian = 5%, simpangan baku populasi  = 5 kg. Hitunglah ukuran sampel " +
               "yang diperlukan jika kuasa uji 0,95 dan rataan sesungguhnya 69 kg",
        jawabanBenar = "270",
        listPilihanJawaban = listOf(
            "300",
            "270",
            "250",
            "260"
        )
    )
    val kuis6 = Kuis(
        id = Materi.ID_MATERI_6_TEKNIK_SAMPLING,
        nama = "Kuis Teknik Sampling",
        deskripsi = "Kuis ini berisikan soal teknik sampling",
        listSoal = listOf(soalKuis6_1, soalKuis6_2, soalKuis6_3, soalKuis6_4, soalKuis6_5)
    )
    /* End of Kuis 6 */

    /* Kuis 7 - Rancangan Percobaan - ANOVA (Matthew) */
    val soalKuis7_1 = SoalKuis(
        id = 1,
        soal = "Pengujian rata-rata menggunakan satu variable dependen dari satu factor dengan lebih dari atau sama dengan 2 taraf, " +
                "merupakan pengertian dari ",
        jawabanBenar = "One Way ANOVA",
        listPilihanJawaban = listOf(
            "Two Way ANOVA",
            "One Way ANOVA",
            "Simpangan Baku",
            "Taraf Keberartian"
        )
    )
    val soalKuis7_2 = SoalKuis(
        id = 2,
        keterangan = "Dosis pupuk 2kg, 3kg, dan 4kg diberikan kepada 9 petak sawah. " +
                "Tiga petak sawah masing-masing diberikan pupuk 3kg, 3 petak sawah selain 6 sebelumnya masing-masing diberi pupuk 4 kg. " +
                "Dosis pupuk 2 kg disebut perlakuan level 1, dosis pupuk 3 kg disebut perlakuan level 2, " +
                "dan dosis pupuk 4 kg disebut perlakuan level 3. Karena masing-masing level (dosis pupuk) diterapkan pada 3 petak sawah, " +
                "artinya percobaan dilakukan dengan ulangan sebanyak 3 kali. " +
                "Data disajikan dalam tabel berikut ini",
        gambar = R.drawable.kuis_anova_1,
        soal = "Maka JKA dari data tersebut adalah",
        jawabanBenar = "22.32",
        listPilihanJawaban = listOf(
            "22.32",
            "22.01",
            "24.9",
            "2.58"
        )
    )
    val soalKuis7_3 = SoalKuis(
        id = 3,
        soal = "Merujuk pada soal nomor 2 maka nilai JKT adalah",
        jawabanBenar = "24.9",
        listPilihanJawaban = listOf(
            "22.32",
            "22.01",
            "24.9",
            "2.58"
        )
    )
    val soalKuis7_4 = SoalKuis(
        id = 4,
        soal = "Merujuk pada soal nomor 2 maka nilai JKG adalah",
        jawabanBenar = "2.58",
        listPilihanJawaban = listOf(
            "22.32",
            "22.01",
            "24.9",
            "2.58"
        )
    )
    val soalKuis7_5 = SoalKuis(
        id = 5,
        soal = "Jika pada suatu percobaan F hitung > F Alpha maka bisa ditarik kesimpulan bahwa",
        jawabanBenar = "Tolak H0, Terima H1",
        listPilihanJawaban = listOf(
            "Tolak H0, Tolak H1",
            "Tolak H0, Terima H1",
            "Terima H0, Tolak H1",
            "Terima H0, Terima H1"
        )
    )
    val kuis7 = Kuis(
        id = Materi.ID_MATERI_7_ANOVA,
        nama = "Kuis ANOVA",
        deskripsi = "Kuis ini berisikan soal ANOVA",
        listSoal = listOf(soalKuis7_1, soalKuis7_2, soalKuis7_3, soalKuis7_4, soalKuis7_5)
    )
    /* End of Kuis 7 */

    /* Kuis 8 - Estimasi Satu dan Dua Sampel (Joshua) */
    val soalKuis8_1 = SoalKuis(
        id = 1,
        soal = "1. Dari 11 mahasiswa X penghasilan orangtuanya sebagai berikut : 2; 3; 3,2; 2,7; 4; 5; 6; 3; 4; 5; 3 juta perbulan. " +
                "Dari 10 mahasiswa Y penghasilan orangtuanya sebagai berikut : 4; 2; 4; 5; 2,7; 4; 5; 6; 2; 4 juta perbulan. " +
                "Rata-rata X dari data diatas adalah",
        jawabanBenar = "3,72",
        listPilihanJawaban = listOf(
            "3,72",
            "4",
            "3,5",
            "3,2"
        )
    )
    val soalKuis8_2 = SoalKuis(
        id = 2,
        soal = "2. Standar deviasi dari soal nomor 1 di atas adalah",
        jawabanBenar = "1,2",
        listPilihanJawaban = listOf(
            "3,72",
            "3,5",
            "1,2",
            "3,2"
        )
    )
    val soalKuis8_3 = SoalKuis(
        id = 3,
        soal = "3. Berdasarkan soal nomor 1 Proporsi mahasiswa X yang lebih dari 4 juta adalah",
        jawabanBenar = "0,27",
        listPilihanJawaban = listOf(
            "0,27",
            "2",
            "1,6",
            "1"
        )
    )
    val soalKuis8_4 = SoalKuis(
        id = 4,
        soal = "4. Berdasarkan soal nomor 1, maka Selisih rata-rata penghasilan orangtua mahasiswa X dan Y adalah",
        jawabanBenar = "0,25",
        listPilihanJawaban = listOf(
            "0,27",
            "2",
            "1",
            "0,25"
        )
    )
    val soalKuis8_5 = SoalKuis(
        id = 5,
        soal = "5. Berdasarkan soal nomor 1, maka selisih proporsi penghasilan orangtua X dan Y adalah",
        jawabanBenar = "0,03",
        listPilihanJawaban = listOf(
            "0,27",
            "1,6",
            "0,25",
            "0,03"
        )
    )
    val kuis8 = Kuis(
        id = Materi.ID_MATERI_8_KONSEP_ESTIMASI,
        nama = "Kuis Konsep Estimasi",
        deskripsi = "Kuis ini berisikan soal konsep estimasi",
        listSoal = listOf(soalKuis8_1, soalKuis8_2, soalKuis8_3, soalKuis8_4, soalKuis8_5)
    )
    /* End of Kuis 8 */

    /* Kuis 9 - Pengujian Hipotesis (Matthew) */
    val soalKuis9_1 = SoalKuis(
        id = 1,
        soal = "Satu perusahaan alat listrik menghasilkan bola lampu yang umumnya berdistribusi hampir normal " +
                "dengan rataan 800 jam dan simpangan baku 40 jam. " +
                "Ujilah hipotesis bahwa µ = 800 jam lawan tanding µ ≠ 800 jam bila sampel acak 30 bola lampu mempunyai " +
                "rata-rata umur 788 jam. " +
                "Gunakan taraf keberartian 0,04 !",
        jawabanBenar = "Terima H0, Tolak H1",
        listPilihanJawaban = listOf(
            "Tolak H0, Tolak H1",
            "Tolak H0, Terima H1",
            "Terima H0, Tolak H1",
            "Terima H0, Terima H1"
        )
    )
    val soalKuis9_2 = SoalKuis(
        id = 2,
        soal = "Suatu sampel acak 96 cangkir minuman yang diambil dari suatu  mesin minuman berisi rata-rata 21,9 desiliter, " +
                "dengan simpangan baku 1,42 desiliter. " +
                "Ujilah hipotesis bahwa µ = 22,2 desiliter lawan hipotesis tanding bahwa µ < 22,2 pada taraf keberartian 0,05. " +
                "Maka diperoleh nilai statistic uji yaitu",
        jawabanBenar = "-1.26",
        listPilihanJawaban = listOf(
            "-1.26",
            "1.3",
            "1.26",
            "1.3"
        )
    )
    val soalKuis9_3 = SoalKuis(
        id = 3,
        soal = "Berdasarkan soal nomor 2 maka nilai P-value adalah",
        jawabanBenar = "0.1038",
        listPilihanJawaban = listOf(
            "0.1",
            "0.1038",
            "0.2",
            "0.112"
        )
    )
    val soalKuis9_4 = SoalKuis(
        id = 4,
        soal = "4.\tDalam laporan penelitian oleh Richard H. Weindruch dari UCLA Medical School, " +
                "dikemukakan bahwa tikus yang umur rata-ratanya 32 bulan akan hidup mencapai sekitar 40 bulan jika " +
                "40% dari kalori dlam makanannya diganti dengan vitamin dan protein. " +
                "Apakah ada alasan untuk mempercayai bahwa µ < 40 bila 64 tikus yang diberi diet mempunyai " +
                "umur rata-rata 38 bulan dengan simpangan baku 5,8 bulan? Gunakan taraf keberartian 0,025!",
        jawabanBenar = "Terima H0, Tolak H1",
        listPilihanJawaban = listOf(
            "Tolak H0, Tolak H1",
            "Tolak H0, Terima H1",
            "Terima H0, Tolak H1",
            "Terima H0, Terima H1"
        )
    )
    val soalKuis9_5 = SoalKuis(
        id = 5,
        soal = "Berdasarkan soal nomor 4 di atas maka nilai P-Valuenya adalah",
        jawabanBenar = "0.0030",
        listPilihanJawaban = listOf(
            "0.012",
            "0.0030",
            "0.31",
            "0.022"
        )
    )
    val kuis9 = Kuis(
        id = Materi.ID_MATERI_9_PENGUJIAN_HIPOTESIS,
        nama = "Kuis Pengujian Hipotesis",
        deskripsi = "Kuis ini berisikan soal pengujian hipotesis",
        listSoal = listOf(soalKuis9_1, soalKuis9_2, soalKuis9_3, soalKuis9_4, soalKuis9_5)
    )
    /* End of Kuis 9 */

    /* Kuis 10 - Regresi Linier Sederhana dan Korelasi (Matthew) */
    val soalKuis10_1 = SoalKuis(
        id = 1,
        keterangan = "Perhatikan data yang disajikan pada tabel berikut ini.",
        gambar = R.drawable.kuis_regresi_korelasi_1,
        soal = "Berdasarkan tabel diatas maka nilai dari ∑Y^2  adalah",
        jawabanBenar = "35252",
        listPilihanJawaban = listOf(
            "35252",
            "30893",
            "28460",
            "820"
        )
    )
    val soalKuis10_2 = SoalKuis(
        id = 2,
        soal = "Berdasarkan soal nomor 1 maka nilai ∑X^2  adalah",
        jawabanBenar = "28460",
        listPilihanJawaban = listOf(
            "35252",
            "30893",
            "28460",
            "820"
        )
    )
    val soalKuis10_3 = SoalKuis(
        id = 3,
        soal = "Berdasarkan tabel pada soal nomor 1 maka nilai slope (b) adalah",
        jawabanBenar = "0.54",
        listPilihanJawaban = listOf(
            "0.2",
            "0.54",
            "0.34",
            "1"
        )
    )
    val soalKuis10_4 = SoalKuis(
        id = 4,
        soal = "Berdasarkan tabel pada soal nomor 1 maka nilai intersep (a) yang diperoleh adalah",
        jawabanBenar = "21.54",
        listPilihanJawaban = listOf(
            "20",
            "21.3",
            "21.54",
            "22"
        )
    )
    val soalKuis10_5 = SoalKuis(
        id = 5,
        soal = "Berdasarkan nilai yang diperoleh untuk masing-masing a dan b pada soal sebelumnya " +
                "maka diperoleh persamaan regresi adalah",
        jawabanBenar = "Y = 21,54 + 0,54X",
        listPilihanJawaban = listOf(
            "Y = 21,54 + 0,54X",
            "Y = 21,3 + 0,34X",
            "Y = 21,54 + 0,34X",
            "Y = 23,12 + 0,54X"
        )
    )
    val kuis10 = Kuis(
        id = Materi.ID_MATERI_10_REGRESI_KORELASI,
        nama = "Kuis Regresi Linier dan Korelasi",
        deskripsi = "Kuis ini berisikan soal regresi linier dan korelasi",
        listSoal = listOf(soalKuis10_1, soalKuis10_2, soalKuis10_3, soalKuis10_4, soalKuis10_5)
    )
    /* End of Kuis 10 */

    /* Kumpulan Kuis */
    val kumpulanKuis = arrayListOf<Kuis>(
        kuis1,
        kuis2,
        kuis3,
        kuis4,
        kuis5,
        kuis6,
        kuis7,
        kuis8,
        kuis9,
        kuis10
    )
    /* End of Kumpulan Kuis */

}