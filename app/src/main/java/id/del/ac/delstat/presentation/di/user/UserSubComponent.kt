package id.del.ac.delstat.presentation.di.user

import dagger.Subcomponent
import id.del.ac.delstat.MainActivity

@UserScope
@Subcomponent(modules = [UserModule::class])
interface UserSubComponent {
    fun inject(mainActivity: MainActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): UserSubComponent
    }
}