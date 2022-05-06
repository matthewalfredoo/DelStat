package id.del.ac.delstat.presentation.di.core

import dagger.Module
import dagger.Provides
import id.del.ac.delstat.data.repository.materi.MateriRepositoryImpl
import id.del.ac.delstat.data.repository.materi.datasource.MateriRemoteDataSource
import id.del.ac.delstat.data.repository.user.UserRepositoryImpl
import id.del.ac.delstat.data.repository.user.datasource.UserRemoteDataSource
import id.del.ac.delstat.domain.repository.MateriRepository
import id.del.ac.delstat.domain.repository.UserRepository
import javax.inject.Singleton

@Module
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

}