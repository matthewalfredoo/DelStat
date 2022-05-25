package id.del.ac.delstat.util

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {

    companion object {
        val simpleDateFormatWithSecond = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale("in", "ID"))
        val simpleDateFormatWithoutSecond = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("in", "ID"))

        fun getCurrentDateTime(): String {
            return simpleDateFormatWithSecond.format(Date())
        }

        fun getDateTime(s: String): String? {
            return try {
                val netDate = Date(s.toLong() * 1000)
                simpleDateFormatWithSecond.format(netDate)
            } catch (e: Exception) {
                null
            }
        }

        fun getDateTimeWithoutSecond(s: String): String? {
            return try {
                val netDate = Date(s.toLong() * 1000)
                simpleDateFormatWithoutSecond.format(netDate)
            } catch (e: Exception) {
                null
            }
        }
    }

}