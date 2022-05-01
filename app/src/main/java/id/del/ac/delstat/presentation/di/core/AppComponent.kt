package id.del.ac.delstat.presentation.di.core

import dagger.Component
import id.del.ac.delstat.presentation.di.user.UserSubComponent
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NetModule::class,
    RemoteDataModule::class,
    LocalDataModule::class,
    RepositoryModule::class,
    AppModule::class
])
interface AppComponent {

    fun userSubComponent(): UserSubComponent.Factory

}