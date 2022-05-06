package id.del.ac.delstat.presentation.di.materi

import dagger.Subcomponent
import id.del.ac.delstat.presentation.testactivity.MateriActivity

@MateriScope
@Subcomponent(modules = [MateriModule::class])
interface MateriSubComponent {
    fun inject(materiActivity: MateriActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): MateriSubComponent
    }
}