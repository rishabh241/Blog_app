package com.example.blog_app.Model

data class UserData(
    val name: String="null",
    val email: String="null",
    val pass: String="null",
    val imageUrl: String="null"
){
    constructor(): this(" ","","","")
}
