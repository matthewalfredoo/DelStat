package id.del.ac.delstat.presentation.di

import id.del.ac.delstat.presentation.di.user.UserSubComponent

interface Injector {
    fun createUserSubComponent(): UserSubComponent
}