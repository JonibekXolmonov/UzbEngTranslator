package com.example.uzbengtranslator.model

data class LanguageDetection(
    val data: Data
)

data class Data(
    val detections: List<List<Detection>>
)

data class Detection(
    val language: String,
    val confidence: Double,
    val isReliable: Boolean
)