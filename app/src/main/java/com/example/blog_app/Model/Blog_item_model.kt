package com.example.blog_app.Model

data class Blog_item_model(
    val heading: String="null",
    val userName: String="null",
    val date: String="null",
    val desc: String="null",
    val likeCount: Int=0,
    val imageUrl: String="null"
)
