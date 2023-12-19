package com.example.blog_app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.blog_app.Model.UserData
import com.example.blog_app.databinding.ActivitySigninRegisterBinding
import com.example.blog_app.register.welcome
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class SignIn_Register : AppCompatActivity() {
//    private val binding: ActivitySigninRegisterBinding by lazy {
//        ActivitySigninRegisterBinding.inflate(layoutInflater)
//    }
    private lateinit var binding: ActivitySigninRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage

    private val PICK_IMAGE_REQUEST = 1
    private var ImageUri : Uri? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySigninRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
//
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://food-app-62353-default-rtdb.asia-southeast1.firebasedatabase.app")
        storage = FirebaseStorage.getInstance()



        val action: String? = intent.getStringExtra("action")

        if(action=="logIn"){

            binding.logInBtn.visibility = View.VISIBLE
            binding.logInEmail.visibility = View.VISIBLE
            binding.logInPassword.visibility = View.VISIBLE
            binding.regTxt.visibility = View.INVISIBLE
            binding.regBtn.visibility = View.INVISIBLE

            binding.logInBtn.setOnClickListener{

                val email: String = binding.logInEmail.text.toString()
                val password: String = binding.logInPassword.text.toString()
                if(email.isBlank() || password.isBlank()){
                    Toast.makeText(this, "Fill all fields",Toast.LENGTH_LONG).show()
                }else{
                    auth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener {task->
                            if(task.isSuccessful){
                                Toast.makeText(this,"Log In succesful",Toast.LENGTH_LONG).show()
                                startActivity(Intent(this,MainActivity::class.java))
                                finish()
                            }else{
                                Toast.makeText(this,"Invalid ${task.exception?.message.toString()}",Toast.LENGTH_LONG).show()
                            }
                        }

                }


            }
        }else if(action=="register"){
            binding.regName.visibility = View.VISIBLE
            binding.regEmail.visibility = View.VISIBLE
            binding.regPass.visibility = View.VISIBLE
            binding.regBtn.visibility = View.VISIBLE
            binding.cardView.visibility = View.VISIBLE

            binding.regTxt.visibility = View.INVISIBLE
            binding.logInBtn.visibility = View.INVISIBLE


            binding.regBtn.setOnClickListener {
                val name: String = binding.regName.text.toString()
                val email: String = binding.regEmail.text.toString()
                val password: String = binding.regPass.text.toString()

                if(name.isBlank() || email.isBlank() || password.isBlank()){
                    Toast.makeText(this, "Fill all fields",Toast.LENGTH_LONG).show()
                }else{
                    auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener{task->
                            if(task.isSuccessful){
                                val user = auth.currentUser
                                auth.signOut()
                                //data store in database
                                user?.let {
                                    val userReference = database.getReference("users")
                                    val userId = user.uid
                                    val userData = UserData(name,email,password)
                                    userReference.child(userId).setValue(userData)

                                    // save user Profile or upload image of a user
                                    val storageReference = storage.reference.child("profile_image/$userId.jpg")
                                    storageReference.putFile(ImageUri!!)
                                        .addOnCompleteListener{task->
                                            if(task.isSuccessful){
                                            storageReference.downloadUrl.addOnCompleteListener{imageUri->
                                                if(imageUri.isSuccessful){
                                                    val imgUrl:String= imageUri.result.toString()
                                                    userReference.child(userId).child("imageUrl").setValue(imgUrl)
                                                }
                                           }
                                        }
                                    }
                                    startActivity(Intent(this,welcome::class.java))
                                    finish()
                                }
                            }else{
                                Toast.makeText(this,"Invalid ${task.exception?.message.toString()}",Toast.LENGTH_LONG).show()
                            }

                        }
                }
            }
        }
        binding.cardView.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"select Image"), PICK_IMAGE_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.data!=null)
            ImageUri = data.data
            Glide.with(this)
                .load(ImageUri)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.imageView2)


    }



}