package com.example.myapplication.restapi.models

data class ManagerSpecialsResponse<T>(
    val canvasUnit: String,
    val managerSpecials: List<T>
)