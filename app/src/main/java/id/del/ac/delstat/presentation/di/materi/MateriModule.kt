package id.del.ac.delstat.presentation.di.materi

import android.app.Application
import dagger.Module
import dagger.Provides
import id.del.ac.delstat.domain.repository.MateriRepository
import id.del.ac.delstat.presentation.materi.viewmodel.MateriViewModelFactory

@Module
class MateriModule {

    @MateriScope
    @Provides
    fun provideMateriViewModelFactory(
        app: Application,
        materiRepository: MateriRepository
    ): MateriViewModelFactory {
        return MateriViewModelFactory(app, materiRepository)
    }

}