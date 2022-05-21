package com.example.uzbengtranslator.networking.services

import com.example.uzbengtranslator.model.LanguageDetection
import com.example.uzbengtranslator.model.TranslationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

@JvmSuppressWildcards
interface ApiService {

    @FormUrlEncoded
    @POST("v2/detect")
    suspend fun detectLanguage(@Field("q") q: String): LanguageDetection

    @FormUrlEncoded
    @POST("v2/")
    suspend fun translateToEng(
        @Field("q") q: String,
        @Field("source") source: String,
        @Field("target") target: String
    ): TranslationResponse
}