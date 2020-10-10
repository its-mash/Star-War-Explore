package com.example.starwarexplore.ui.dashboard

import android.Manifest
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.rule.GrantPermissionRule
import com.example.starwarexplore.StarWarExploreApplication
import com.example.starwarexplore.data.remote.StarWarAPI
import com.example.starwarexplore.repositories.StarWarRepository
import com.example.starwarexplore.util.Constants
import com.example.starwarexplore.util.hasNetwork
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class DashboardViewModelTest {
    @get:Rule
    var instTaskExecutorRule= InstantTaskExecutorRule()

    @get:Rule
    var mRuntimePermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.INTERNET)

    private lateinit var api: StarWarAPI
    private lateinit var repository:StarWarRepository
    private lateinit var viewModel:DashboardViewModel

    @Before
    fun setup() {
        val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(
            ApplicationProvider.getApplicationContext<StarWarExploreApplication>().cacheDir,
            cacheSize
        )


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
                request = if (hasNetwork(ApplicationProvider.getApplicationContext())!!)
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
                    request.newBuilder().header(
                        "Cache-Control",
                        "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                    ).build()
                // End of if-else statement

                // Add the modified request to the chain.
                chain.proceed(request)
            }
            .build()
        api= Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.STAR_WAR_API_URL)
            .client(okHttpClient)
            .build()
            .create(StarWarAPI::class.java)
        repository= StarWarRepository(api)
        viewModel=DashboardViewModel(repository)
    }


    @Test
    fun getEndPoints() {
        val entryPoints= runBlocking {  repository.getEndPoints()}
        Truth.assertThat(entryPoints.data?.films).isEqualTo("http://swapi.dev/api/films/")
    }
}