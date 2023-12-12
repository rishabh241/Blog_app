package com.example.blog_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.blog_app.Model.Blog_item_model
import com.example.blog_app.Model.UserData
import com.example.blog_app.databinding.ActivityAddBlogBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date

class addBlog : AppCompatActivity() {
    private lateinit var binding: ActivityAddBlogBinding

    private val databaseReference: DatabaseReference =FirebaseDatabase.getInstance("https://food-app-62353-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Blogs")
    private val userReference: DatabaseReference= FirebaseDatabase.getInstance("https://food-app-62353-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users")
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBlogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addBack.setOnClickListener{

        }

        binding.addBlogBtn.setOnClickListener{
            val title: String = binding.addBlogTitleEditText?.text.toString().trim()
            val description: String = binding.addBlogDescEditText?.text.toString().trim()

            val user : FirebaseUser?=auth.currentUser
//            val username = user?.displayName
            if(title.isEmpty() || description.isEmpty()){
                Toast.makeText(this, "Fill all the details ",Toast.LENGTH_LONG).show()
            }else{
                if(user!=null){
                    val userId = user.uid

                    val profilePic = user.photoUrl?:""

                    userReference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val userData = snapshot.getValue(UserData::class.java)
                            if(userData!=null){
                                val userName = userData.name
                                val userProfileUrl = userData.imageUrl
                                val currentData = SimpleDateFormat("yyyy-MM-dd").format(Date())

                                // Blog Item Model
                                val blog_item = Blog_item_model(
                                    userId,
                                    title,
                                    userName,
                                    currentData,
                                    description,
                                    0,
                                    userProfileUrl
                                )
                                //generate key for blog
                                val key = databaseReference.push().key

                                if(key!=null){
                                    val blogRefrence = databaseReference.child(key)
                                    blogRefrence.setValue(blog_item).addOnCompleteListener{task->
                                        if(task.isSuccessful){
                                            finish()
                                        }else{
                                            Toast.makeText(this@addBlog,"Failed to upload",Toast.LENGTH_LONG).show()
                                        }

                                    }
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }


        }
    }
}