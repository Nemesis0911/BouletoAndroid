package com.example.bouleto.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    @SerialName("results") val results: List<Result> = emptyList(),
    @SerialName("status") val status: String? = null
)

@Serializable
data class Result(
    @SerialName("city") val city: String? = null,
    @SerialName("classification") val classification: Int? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("fulltext") val fulltext: String? = null,
    @SerialName("kind") val kind: String? = null,
    @SerialName("metropole") val metropole: Boolean? = null,
    @SerialName("oldcity") val oldcity: String? = null,
    @SerialName("street") val street: String? = null,
    @SerialName("x") val x: Double? = null,
    @SerialName("y") val y: Double? = null,
    @SerialName("zipcode") val zipcode: String? = null
)