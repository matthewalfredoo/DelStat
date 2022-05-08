package id.del.ac.delstat.presentation.di.literatur

import android.app.Application
import dagger.Module
import dagger.Provides
import id.del.ac.delstat.data.preferences.UserPreferences
import id.del.ac.delstat.domain.repository.LiteraturRepository
import id.del.ac.delstat.presentation.literatur.viewmodel.LiteraturViewModelFactory

@Module
class LiteraturModule {

    @LiteraturScope
    @Provides
    fun provideLiteraturViewModelFactory(
        app: Application,
        literaturRepository: LiteraturRepository,
        userPreferences: UserPreferences
    ): LiteraturViewModelFactory {
        return LiteraturViewModelFactory(app, literaturRepository)
    }

}