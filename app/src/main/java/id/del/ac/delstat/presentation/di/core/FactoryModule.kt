package id.del.ac.delstat.presentation.di.core

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.domain.repository.LiteraturRepository
import id.del.ac.delstat.domain.repository.MateriRepository
import id.del.ac.delstat.domain.repository.UserRepository
import id.del.ac.delstat.presentation.literatur.viewmodel.LiteraturViewModelFactory
import id.del.ac.delstat.presentation.materi.viewmodel.MateriViewModelFactory
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModelFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {

    @Singleton
    @Provides
    fun provideUserViewModelFactory(
        app: Application,
        userRepository: UserRepository,
        userPreferences: UserPreferences
    ): UserViewModelFactory {
        return UserViewModelFactory(app, userRepository, userPreferences)
    }

    @Singleton
    @Provides
    fun provideMateriViewModelFactory(
        app: Application,
        materiRepository: MateriRepository
    ): MateriViewModelFactory {
        return MateriViewModelFactory(app, materiRepository)
    }

    @Singleton
    @Provides
    fun provideLiteraturViewModelFactory(
        app: Application,
        literaturRepository: LiteraturRepository
    ): LiteraturViewModelFactory {
        return LiteraturViewModelFactory(app, literaturRepository)
    }

}