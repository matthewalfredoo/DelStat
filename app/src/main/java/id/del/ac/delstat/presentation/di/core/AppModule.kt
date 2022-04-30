package id.del.ac.delstat.presentation.di.core

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import id.del.ac.delstat.presentation.di.user.UserSubComponent
import javax.inject.Singleton

@Module(subcomponents = [
    UserSubComponent::class
])
class AppModule(
    private val application: Application,
    private val context: Context
) {

    @Singleton
    @Provides
    fun provideApplication(): Application {
        return application
    }

    @Singleton
    @Provides
    fun provideContext(): Context {
        return context.applicationContext
    }

}