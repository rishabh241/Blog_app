package com.example.blog_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.blog_app.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Profile : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val userId = auth.currentUser?.uid

        if(userId!=null){
            loadUserProfile(userId)
        }

        binding.profileAddArt.setOnClickListener {
            startActivity(Intent(this,addBlog::class.java))
            finish()
        }

        binding.profileLogOut.setOnClickListener {
            auth.signOut()
            Toast.makeText(this,"Successfully Logged Out",Toast.LENGTH_SHORT).show()
//            val broadcastIntent = Intent()
//            broadcastIntent.setAction("com.package.ACTION_LOGOUT")
//            sendBroadcast(broadcastIntent)
//            finish()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("finish", true)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // To clean up all activities

            startActivity(intent)
            finish()
        }


    }

    private fun loadUserProfile(userId: String) {
        val userReference = FirebaseDatabase.getInstance("https://food-app-62353-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("users").child(userId)

        userReference.child("name").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.getValue(String::class.java)
                if(name!=null){
                    binding.profileName.setText(name)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        userReference.child("imageUrl").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profileImage = snapshot.getValue(String::class.java)


                if(profileImage!=null){
                    Glide.with(this@Profile)
                        .load(profileImage)
                        .into(binding.proImg)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}