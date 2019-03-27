package gitlab.therealdroid.com.addidaschallenge.domain.datasource.api

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import gitlab.therealdroid.com.addidaschallenge.configuration.ProjectConfiguration
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiManager {

    private fun okkHttpClient(): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        return builder.build()
    }

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(ProjectConfiguration.API_URL)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .client(okkHttpClient())
        .build()

    private fun <T> provideApi(apiClass: Class<T>): T = retrofit.create(apiClass)

    internal val goalsApi: GoalsApi = provideApi(GoalsApi::class.java)


}