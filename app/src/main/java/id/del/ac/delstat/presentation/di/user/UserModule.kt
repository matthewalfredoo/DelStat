package id.del.ac.delstat.presentation.di.user

import android.app.Application
import dagger.Module
import dagger.Provides
import id.del.ac.delstat.domain.repository.UserRepository
import id.del.ac.delstat.presentation.user.viewmodel.UserViewModelFactory

@Module
class UserModule {

    @UserScope
    @Provides
    fun provideUserViewModelFactory(app: Application, userRepository: UserRepository): UserViewModelFactory {
        return UserViewModelFactory(app, userRepository)
    }

}