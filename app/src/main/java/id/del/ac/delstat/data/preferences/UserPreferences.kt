package id.del.ac.delstat.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {

    private val dataStore = context.dataStoreFile(
        fileName = "user_preferences"
    )

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        private val USER_ID = intPreferencesKey("user_id")
        private val USER_NAMA = stringPreferencesKey("user_nama")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_NO_HP = stringPreferencesKey("user_no_hp")
        private val USER_FOTO_PROFIL = stringPreferencesKey("user_foto_profil")
        private val USER_JENJANG = stringPreferencesKey("user_jenjang")
        private val USER_TOKEN = stringPreferencesKey("user_token")
    }

    /* USER_ID Preferences */
    val getUserId: Flow<Int?>
        get() = context.dataStore.data.map {
            it[USER_ID] ?: 0
        }

    suspend fun setUserId(id: Int?) {
        context.dataStore.edit {
            it[USER_ID] = id ?: 0
        }
    }
    /* END USER_ID Preferences */

    /* USER_NAMA Preferences */
    val getUserNama: Flow<String?>
        get() = context.dataStore.data.map {
            it[USER_NAMA] ?: ""
        }

    suspend fun setUserNama(nama: String?) {
        context.dataStore.edit {
            it[USER_NAMA] = nama ?: ""
        }
    }
    /* END USER_NAMA Preferences */

    /* USER_EMAIL Preferences */
    val getUserEmail: Flow<String?>
        get() = context.dataStore.data.map {
            it[USER_EMAIL] ?: ""
        }

    suspend fun setUserEmail(email: String?) {
        context.dataStore.edit {
            it[USER_EMAIL] = email ?: ""
        }
    }
    /* END USER_EMAIL Preferences */

    /* USER_NO_HP Preferences */
    val getUserNoHp: Flow<String?>
        get() = context.dataStore.data.map {
            it[USER_NO_HP] ?: ""
        }

    suspend fun setUserNoHp(noHp: String?) {
        context.dataStore.edit {
            it[USER_NO_HP] = noHp ?: ""
        }
    }
    /* END USER_NO_HP Preferences */

    /* USER_FOTO_PROFIL Preferences */
    val getUserFotoProfil: Flow<String?>
        get() = context.dataStore.data.map {
            it[USER_FOTO_PROFIL] ?: ""
        }

    suspend fun setUserFotoProfil(fotoProfil: String?) {
        context.dataStore.edit {
            it[USER_FOTO_PROFIL] = fotoProfil ?: ""
        }
    }
    /* END USER_FOTO_PROFIL Preferences */

    /* USER_JENJANG Preferences */
    val getUserJenjang: Flow<String?>
        get() = context.dataStore.data.map {
            it[USER_JENJANG] ?: ""
        }

    suspend fun setUserJenjang(jenjang: String?) {
        context.dataStore.edit {
            it[USER_JENJANG] = jenjang ?: ""
        }
    }
    /* END USER_JENJANG Preferences */

    /* USER_TOKEN Preferences */
    val getUserToken: Flow<String?>
        get() = context.dataStore.data.map {
            it[USER_TOKEN] ?: ""
        }

    suspend fun setUserToken(token: String?) {
        context.dataStore.edit {
            it[USER_TOKEN] = token ?: ""
        }
    }
    /* END USER_TOKEN Preferences */

}