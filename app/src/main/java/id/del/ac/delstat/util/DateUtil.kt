package id.del.ac.delstat.util

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {

    companion object {
        val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale("in", "ID"))

        fun getCurrentDateTime(): String {
            return simpleDateFormat.format(Date())
        }

        fun getDateTime(s: String): String? {
            return try {
                val netDate = Date(s.toLong() * 1000)
                simpleDateFormat.format(netDate)
            } catch (e: Exception) {
                null
            }
        }
    }

}