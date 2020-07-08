package com.bouzo.specials.restapi.models

data class ManagerSpecialsResponse<T>(
    val canvasUnit: Int,
    val managerSpecials: List<T>
)