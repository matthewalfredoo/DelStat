package id.del.ac.delstat.presentation.di.core

import dagger.Module
import dagger.Provides
import id.del.ac.delstat.BuildConfig
import id.del.ac.delstat.data.api.DelStatApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetModule(private val baseUrl: String) {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }

    @Singleton
    @Provides
    fun provideDelStatApiService(retrofit: Retrofit): DelStatApiService {
        return retrofit.create(DelStatApiService::class.java)
    }

}