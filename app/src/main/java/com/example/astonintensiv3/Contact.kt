package com.example.astonintensiv3

data class Contact(
    val id: Int,
    var firstName: String,
    var lastName: String,
    var number: String,
    var isSelected: Boolean = false
)
