package com.example.blog_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.blog_app.Model.Blog_item_model
import com.example.blog_app.databinding.ActivityAddBlogBinding
import com.example.blog_app.databinding.ActivityReadArticleBinding

class read_article : AppCompatActivity() {
    private lateinit var binding: ActivityReadArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.readBack.setOnClickListener{
            finish()
        }

        val blogs = intent.getParcelableExtra<Blog_item_model>("blogItem")

        if(blogs!=null){
            binding.readTitle.setText(blogs.heading).toString()
            binding.readAuthor.setText(blogs.userName).toString()
            binding.readDate.setText(blogs.date).toString()
            binding.readDesc.setText(blogs.desc).toString()

            Glide.with(this)
                .load(blogs.imageUrl)
                .into(binding.readAuthorImg)
        }
    }
}