package com.vaneezaahmad.sheworks

data class User(
    val name: String,
    val image: Int
)
{
    constructor() : this("", 0)
}
