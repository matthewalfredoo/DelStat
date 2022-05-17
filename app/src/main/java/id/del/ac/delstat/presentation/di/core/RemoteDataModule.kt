package id.del.ac.delstat.presentation.di.core

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.repository.analisisdata.datasource.AnalisisDataRemoteDataSource
import id.del.ac.delstat.data.repository.analisisdata.datasourceimpl.AnalisisDataRemoteDataSourceImpl
import id.del.ac.delstat.data.repository.literatur.datasource.LiteraturRemoteDataSource
import id.del.ac.delstat.data.repository.literatur.datasourceimpl.LiteraturRemoteDataSourceImpl
import id.del.ac.delstat.data.repository.materi.datasource.MateriRemoteDataSource
import id.del.ac.delstat.data.repository.materi.datasourceimpl.MateriRemoteDataSourceImpl
import id.del.ac.delstat.data.repository.user.datasource.UserRemoteDataSource
import id.del.ac.delstat.data.repository.user.datasourceimpl.UserRemoteDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataModule {

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(delStatApiService: DelStatApiService): UserRemoteDataSource {
        return UserRemoteDataSourceImpl(delStatApiService)
    }

    @Singleton
    @Provides
    fun provideMateriRemoteDataSource(delStatApiService: DelStatApiService): MateriRemoteDataSource {
        return MateriRemoteDataSourceImpl(delStatApiService)
    }

    @Singleton
    @Provides
    fun provideLiteraturRemoteDataSource(delStatApiService: DelStatApiService): LiteraturRemoteDataSource {
        return LiteraturRemoteDataSourceImpl(delStatApiService)
    }

    @Singleton
    @Provides
    fun provideAnalisisDataRemoteDataSource(delStatApiService: DelStatApiService): AnalisisDataRemoteDataSource {
        return AnalisisDataRemoteDataSourceImpl(delStatApiService)
    }

}