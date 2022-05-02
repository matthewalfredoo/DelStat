package id.del.ac.delstat.presentation.di.user

import dagger.Subcomponent
import id.del.ac.delstat.presentation.activity.HomeActivity
import id.del.ac.delstat.presentation.activity.LoginActivity
import id.del.ac.delstat.presentation.activity.MainActivity
import id.del.ac.delstat.presentation.activity.RegisterActivity

@UserScope
@Subcomponent(modules = [UserModule::class])
interface UserSubComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(homeActivity: HomeActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(registerActivity: RegisterActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): UserSubComponent
    }
}