package id.del.ac.delstat.presentation.di

import id.del.ac.delstat.presentation.di.literatur.LiteraturSubComponent
import id.del.ac.delstat.presentation.di.materi.MateriSubComponent
import id.del.ac.delstat.presentation.di.user.UserSubComponent

interface Injector {
    fun createUserSubComponent(): UserSubComponent
    fun createMateriSubComponent(): MateriSubComponent
    fun createLiteraturSubComponent(): LiteraturSubComponent
}