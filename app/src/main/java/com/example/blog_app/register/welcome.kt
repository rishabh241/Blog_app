package com.example.blog_app.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.blog_app.MainActivity
import com.example.blog_app.R
import com.example.blog_app.SignIn_Register
import com.example.blog_app.databinding.ActivityWelcomeBinding
import com.google.firebase.auth.FirebaseAuth

class welcome : AppCompatActivity() {
//    private val binding: ActivityWelcomeBinding by lazy {
//        ActivityWelcomeBinding.inflate(layoutInflater)
//    }
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        auth=FirebaseAuth.getInstance()
        setContentView(binding.root)

        binding.logInBtn.setOnClickListener{
            val intent = Intent(this,SignIn_Register::class.java)
            intent.putExtra("action","logIn")
            startActivity(intent)
            finish()
        }
//
        binding.regBtn.setOnClickListener{
            val intent = Intent(this,SignIn_Register::class.java)
            intent.putExtra("action","register")
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUSer = auth.currentUser
        if(currentUSer!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}