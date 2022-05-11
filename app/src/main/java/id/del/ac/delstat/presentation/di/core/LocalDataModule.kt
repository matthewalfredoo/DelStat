package id.del.ac.delstat.presentation.di.core

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.del.ac.delstat.data.preferences.UserPreferences
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {

    @Singleton
    @Provides
    fun provideUserPreferences(
        @ApplicationContext
        context: Context
    ) : UserPreferences {
        return UserPreferences(context)
    }

}