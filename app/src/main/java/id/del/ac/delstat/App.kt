package id.del.ac.delstat

import android.app.Application
import id.del.ac.delstat.presentation.di.Injector
import id.del.ac.delstat.presentation.di.core.AppComponent
import id.del.ac.delstat.presentation.di.core.AppModule
import id.del.ac.delstat.presentation.di.core.DaggerAppComponent
import id.del.ac.delstat.presentation.di.core.NetModule
import id.del.ac.delstat.presentation.di.literatur.LiteraturSubComponent
import id.del.ac.delstat.presentation.di.materi.MateriSubComponent
import id.del.ac.delstat.presentation.di.user.UserSubComponent

class App: Application(), Injector{
    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this, applicationContext))
            .netModule(NetModule(BuildConfig.BASE_URL))
            .build()
    }

    override fun createUserSubComponent(): UserSubComponent {
        return appComponent.userSubComponent().create()
    }

    override fun createMateriSubComponent(): MateriSubComponent {
        return appComponent.materiSubComponent().create()
    }

    override fun createLiteraturSubComponent(): LiteraturSubComponent {
        return appComponent.literaturSubComponent().create()
    }

}