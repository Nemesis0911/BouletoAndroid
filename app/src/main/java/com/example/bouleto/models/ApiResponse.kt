package com.example.bouleto.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val results: List<Result>,
    val status: String
)

@Serializable
data class Result(
    val city: String,
    val classification: Int,
    val country: String,
    val fulltext: String,
    val kind: String,
    val metropole: Boolean,
    val oldcity: String,
    val street: String,
    val x: Double,
    val y: Double,
    val zipcode: String
)