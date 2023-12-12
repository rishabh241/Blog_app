package com.example.blog_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.blog_app.Adapter.Blog_Adapter
import com.example.blog_app.Model.Blog_item_model
import com.example.blog_app.databinding.ActivityMainBinding
import com.example.blog_app.register.welcome
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private  lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private val blogitems = mutableListOf<Blog_item_model>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance("https://food-app-62353-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("Blogs")

        val userId = auth.currentUser?.uid

        if(userId!=null){
            //set user Profile
            loadUserProfile(userId)
        }
        // recycler View manager, set adapter
        val blogView = binding.homeRec
        val blog_adapter = Blog_Adapter(blogitems)
        blogView.adapter = blog_adapter
        blogView.layoutManager = LinearLayoutManager(this)

        //fetch data from firebase
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                blogitems.clear()
                for(snap in snapshot.children){
                    val blogItem = snap.getValue(Blog_item_model::class.java)
                    if(blogItem!=null){
                        blogitems.add(blogItem)
                    }
                }
                blog_adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.addBlogbtn.setOnClickListener{
            startActivity(Intent(this,addBlog::class.java))
        }
    }

    private fun loadUserProfile(userId: String) {
        val userReference = FirebaseDatabase.getInstance("https://food-app-62353-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("users").child(userId)

        userReference.child("imageUrl").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val profileImage = snapshot.getValue(String::class.java)

                if(profileImage!=null){
                    Glide.with(this@MainActivity)
                        .load(profileImage)
                        .into(binding.homeProfile)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}