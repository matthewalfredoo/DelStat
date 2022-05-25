package id.del.ac.delstat.presentation.di.core

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.domain.repository.*
import id.del.ac.delstat.presentation.analisisdata.viewmodel.AnalisisDataViewModelFactory
import id.del.ac.delstat.presentation.chat.viewmodel.ChatViewModelFactory
import id.del.ac.delstat.presentation.literatur.viewmodel.LiteraturViewModelFactory
import id.del.ac.delstat.presentation.materi.viewmodel.MateriViewModelFactory
import id.del.ac.delstat.presentation.notifikasi.viewmodel.NotifikasiViewModelFactory
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

    @Singleton
    @Provides
    fun provideAnalisisDataViewModelFactory(
        app: Application,
        analisisDataRepository: AnalisisDataRepository
    ): AnalisisDataViewModelFactory {
        return AnalisisDataViewModelFactory(app, analisisDataRepository)
    }

    @Singleton
    @Provides
    fun provideNotifikasiViewModelFactory(
        app: Application,
        notifikasiRepository: NotifikasiRepository
    ): NotifikasiViewModelFactory {
        return NotifikasiViewModelFactory(app, notifikasiRepository)
    }

    @Singleton
    @Provides
    fun provideChatViewModelFactory(
        app: Application,
        chatRepository: ChatRepository
    ): ChatViewModelFactory {
        return ChatViewModelFactory(app, chatRepository)
    }

}