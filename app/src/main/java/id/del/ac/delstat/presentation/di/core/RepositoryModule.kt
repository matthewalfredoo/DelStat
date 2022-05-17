package id.del.ac.delstat.presentation.di.core

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.del.ac.delstat.data.repository.analisisdata.AnalisisDataRepositoryImpl
import id.del.ac.delstat.data.repository.analisisdata.datasource.AnalisisDataRemoteDataSource
import id.del.ac.delstat.data.repository.literatur.LiteraturRepositoryImpl
import id.del.ac.delstat.data.repository.literatur.datasource.LiteraturRemoteDataSource
import id.del.ac.delstat.data.repository.materi.MateriRepositoryImpl
import id.del.ac.delstat.data.repository.materi.datasource.MateriRemoteDataSource
import id.del.ac.delstat.data.repository.user.UserRepositoryImpl
import id.del.ac.delstat.data.repository.user.datasource.UserRemoteDataSource
import id.del.ac.delstat.domain.repository.AnalisisDataRepository
import id.del.ac.delstat.domain.repository.LiteraturRepository
import id.del.ac.delstat.domain.repository.MateriRepository
import id.del.ac.delstat.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideUserRepository(userRemoteDataSource: UserRemoteDataSource): UserRepository {
        return UserRepositoryImpl(userRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideMateriRepository(materiRemoteDataSource: MateriRemoteDataSource): MateriRepository {
        return MateriRepositoryImpl(materiRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideLiteraturRepository(literaturRemoteDataSource: LiteraturRemoteDataSource): LiteraturRepository {
        return LiteraturRepositoryImpl(literaturRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideAnalisisDataRepository(analisisDataRemoteDataSource: AnalisisDataRemoteDataSource): AnalisisDataRepository {
        return AnalisisDataRepositoryImpl(analisisDataRemoteDataSource)
    }

}