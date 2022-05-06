package id.del.ac.delstat.presentation.di.core

import dagger.Module
import dagger.Provides
import id.del.ac.delstat.data.api.DelStatApiService
import id.del.ac.delstat.data.repository.materi.datasource.MateriRemoteDataSource
import id.del.ac.delstat.data.repository.materi.datasourceimpl.MateriRemoteDataSourceImpl
import id.del.ac.delstat.data.repository.user.datasource.UserRemoteDataSource
import id.del.ac.delstat.data.repository.user.datasourceimpl.UserRemoteDataSourceImpl
import javax.inject.Singleton

@Module
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

}