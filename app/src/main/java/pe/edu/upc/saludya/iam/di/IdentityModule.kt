package pe.edu.upc.saludya.iam.di

import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import pe.edu.upc.saludya.BuildConfig
import pe.edu.upc.saludya.iam.data.remote.IdentityApi
import pe.edu.upc.saludya.iam.data.repository.RemoteIdentityRepository
import pe.edu.upc.saludya.iam.domain.repository.IdentityRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Clock
import java.time.ZoneId
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class IdentityBindings {
    @Binds @Singleton
    abstract fun repository(implementation: RemoteIdentityRepository): IdentityRepository
}

@Module
@InstallIn(SingletonComponent::class)
object IdentityModule {
    @Provides @Singleton
    fun gson(): Gson = Gson()

    @Provides @Singleton
    fun clock(): Clock = Clock.system(ZoneId.of("America/Lima"))

    @Provides @Singleton
    fun identityApi(gson: Gson): IdentityApi {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .callTimeout(25, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(IdentityApi::class.java)
    }
}
