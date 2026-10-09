package pe.edu.upc.saludya.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import pe.edu.upc.saludya.BuildConfig
import pe.edu.upc.saludya.core.session.SessionProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(sessionProvider: SessionProvider): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = sessionProvider.accessToken
                val request = chain.request().newBuilder().apply {
                    if (!token.isNullOrBlank()) header("Authorization", "Bearer $token")
                }.build()
                chain.proceed(request)
            }
            .build()

        val baseUrl = BuildConfig.API_BASE_URL.trimEnd('/') + "/api/v1/"

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
