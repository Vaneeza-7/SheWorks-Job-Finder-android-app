package com.vaneezaahmad.sheworks

data class Post(
    val profileImage: String,
    val username: String,
    val timeAgo: String,
    val postImage: String,
    val postContent: String,
    val likesCount: Int,
    val commentsCount: Int
)
{
    constructor() : this("", "", "", "", "", 0, 0)

}
