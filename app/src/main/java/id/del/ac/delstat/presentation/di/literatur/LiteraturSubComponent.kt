package id.del.ac.delstat.presentation.di.literatur

import dagger.Subcomponent
import id.del.ac.delstat.presentation.testactivity.LiteraturActivity

@LiteraturScope
@Subcomponent(modules = [LiteraturModule::class])
interface LiteraturSubComponent {
    fun inject(literaturActivity: LiteraturActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(): LiteraturSubComponent
    }
}