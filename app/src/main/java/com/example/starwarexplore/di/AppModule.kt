package com.example.starwarexplore.di

import android.content.Context
import androidx.room.Room
import com.example.starwarexplore.data.remote.StarWarAPI
import com.example.starwarexplore.other.Constants.STAR_WAR_API_URL
import com.example.starwarexplore.util.hasNetwork
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideStarWarAPI(@ApplicationContext context:Context): StarWarAPI{
        val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(context.cacheDir, cacheSize)


        val okHttpClient = OkHttpClient.Builder()
            // Specify the cache we created earlier.
            .cache(myCache)
            // Add an Interceptor to the OkHttpClient.
            .addInterceptor { chain ->

                // Get the request from the chain.
                var request = chain.request()

                /*
                *  Leveraging the advantage of using Kotlin,
                *  we initialize the request and change its header depending on whether
                *  the device is connected to Internet or not.
                */
                request = if (hasNetwork(context)!!)
                /*
                *  If there is Internet, get the cache that was stored 5 seconds ago.
                *  If the cache is older than 5 minutes, then discard it,
                *  and indicate an error in fetching the response.
                *  The 'max-age' attribute is responsible for this behavior.
                */
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 300).build()
                else
                /*
                *  If there is no Internet, get the cache that was stored 7 days ago.
                *  If the cache is older than 7 days, then discard it,
                *  and indicate an error in fetching the response.
                *  The 'max-stale' attribute is responsible for this behavior.
                *  The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
                */
                    request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
                // End of if-else statement

                // Add the modified request to the chain.
                chain.proceed(request)
            }
            .build()


        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(STAR_WAR_API_URL)
            .client(okHttpClient)
            .build()
            .create(StarWarAPI::class.java)
    }
}


