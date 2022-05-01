package id.del.ac.delstat.presentation.di.core

import android.content.Context
import dagger.Module
import dagger.Provides
import id.del.ac.delstat.data.preferences.UserPreferences
import javax.inject.Singleton

@Module
class LocalDataModule {

    @Singleton
    @Provides
    fun provideUserPreferences(context: Context) : UserPreferences {
        return UserPreferences(context)
    }

}