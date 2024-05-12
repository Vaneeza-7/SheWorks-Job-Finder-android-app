package com.vaneezaahmad.sheworks

data class User(
    val uid: String,
    val username: String,
    val email: String,
    val contactNumber: String,
    val country: String,
    val city: String,
    val useCase: String,
    val password: String,
    val profileImage: String
)
{
    constructor() : this("", "", "", "", "", "", "", "", "")
}
