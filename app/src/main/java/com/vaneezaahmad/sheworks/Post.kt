package com.vaneezaahmad.sheworks

data class Post(
    val profileImageResId: Int,
    val username: String,
    val timeAgo: String,
    val postImageResId: Int,
    val postContent: String,
    val likesCount: Int,
    val commentsCount: Int,
    val likedBy: List<String>, // List of usernames who liked the post
    val comments: List<Comment> // List of comments
)
{

}
