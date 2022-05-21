package com.example.uzbengtranslator.networking

import com.example.uzbengtranslator.networking.services.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    var BASE_URl = "https://google-translate1.p.rapidapi.com/language/translate/"

    private val client = getClient()
    private val retrofit = getRetrofit(client)


    private fun getRetrofit(client: OkHttpClient): Retrofit {

        val okHttpClient = OkHttpClient
            .Builder()
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(BASE_URl)
            .client(client)
            .build()
    }

    private fun getClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor(Interceptor { chain ->
            val builder = chain.request().newBuilder()
            chain.proceed(builder.build())
        })
        .build()

    private fun <T> createServiceWithAuth(service: Class<T>?): T {
        val newClient =
            client.newBuilder().addInterceptor(Interceptor { chain ->
                var request = chain.request()
                val builder = request.newBuilder()
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded")
                builder.addHeader("Accept-Encoding", "application/gzip")
                builder.addHeader("X-RapidApi-Host", "google-translate1.p.rapidapi.com")
                builder.addHeader(
                    "X-RapidAPI-Key",
                    "a14f28c32amsh4de33ecf03398d1p172262jsnb3ef54be00e4"
                )
                request = builder.build()
                chain.proceed(request)
            }).build()
        val newRetrofit = retrofit.newBuilder().client(newClient).build()
        return newRetrofit.create(service!!)
    }

    val translateService = createServiceWithAuth(ApiService::class.java)
}
//monthly-expired keys
//m1 a13d4a1df5mshabfff98a5286afep1d4fbajsnf99012f5069e
//f1 a659dd93c9mshaac7183703db858p175b73jsn7ed7826565b3
//f2   730158c851msh3ce875139e8bf95p1d1292jsnf1d7f51b0b7e
//f3   9be309c338msh52e38355b1e272dp180cd4jsn7db2a798a94c

//working keys

//m2   a14f28c32amsh4de33ecf03398d1p172262jsnb3ef54be00e4
//m3   1d80bf1ff9msh0d31db79d33ada7p19c1a4jsncd446a9b3482
//m4   2884463498mshc47a34bd2a99c87p19e93cjsnae5958b64657