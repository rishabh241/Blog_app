package com.example.blog_app.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blog_app.Model.Blog_item_model
import com.example.blog_app.R
import com.example.blog_app.databinding.RecItemBinding
import com.example.blog_app.read_article
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Blog_Adapter(private val items: List<Blog_item_model>):RecyclerView.Adapter<Blog_Adapter.BlogViewHolder>() {

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance("https://food-app-62353-default-rtdb.asia-southeast1.firebasedatabase.app").reference
    private val currentUser = FirebaseAuth.getInstance().currentUser
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = RecItemBinding.inflate(inflator, parent, false)
        return BlogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val blogItem = items[position]
        holder.bind(blogItem)
    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class BlogViewHolder(private val binding: RecItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(blogItemModel: Blog_item_model) {

            val postIds: String? = blogItemModel.postId
            Log.d("this",postIds.toString())
            val context = binding.root.context
            binding.recHeading.text = blogItemModel.heading
            binding.recDate.text = blogItemModel.date
            binding.recDesc.text = blogItemModel.desc
            binding.recCount.text = blogItemModel.likeCount.toString()

            Glide.with(binding.recProfile.context)
                .load(blogItemModel.imageUrl)
                .into(binding.recProfile)

            binding.recBlogger.text = blogItemModel.userName

            binding.addBlogBtn.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, read_article::class.java)
                intent.putExtra("blogItem", blogItemModel)
                context.startActivity(intent)
            }

//            // check user that he liked the blog or not
//            val postLikeReference = databaseReference.child("Blogs").child(postId.toString()).child("likes")
////            val postLikeReference = postId?.let { databaseReference.child("Blogs").child(it).child("likeCount") }
//            val currentUserLiked = currentUser.uid?.let { uid->
//                postLikeReference?.child(uid)?.addValueEventListener(object :ValueEventListener{
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                       if(snapshot.exists()){
//                            binding.recLike.setImageResource(R.drawable.icon_red_like)
//                       }else{
//                           binding.recLike.setImageResource(R.drawable.icon_like)
//                       }
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        TODO("Not yet implemented")
//                    }
//
//                }) /*?:*/

            //initilize the save button before updating ui
            val userReference = databaseReference.child("users").child(currentUser!!.uid)
            val postSaveReference = userReference.child("savedPost").child(postIds.toString())

            postSaveReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        binding.recBookmark.setImageResource(R.drawable.icon_bookmark_red)
                    }else{
                        binding.recBookmark.setImageResource(R.drawable.icon_bookmark_outline)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            binding.recBookmark.setOnClickListener {
                if(currentUser!=null){

                        handleBookmarkButtonClick(postIds,blogItemModel,binding)

                }else{
                    Toast.makeText(context,"please LogIn",Toast.LENGTH_SHORT).show()
                }
            }

        }
//            binding. .setOnClickListener{
//                if(currentUser!=null){
//                    handleLikeButtonClick(postId,blogItemModel)
//                }else{
//                    Toast.makeText(context,"please LogIn",Toast.LENGTH_LONG).show()
//                }
//            }
//        }


    }

    private fun handleBookmarkButtonClick
                (
        postIds: String?,
        blogItemModel: Blog_item_model,
        binding: RecItemBinding){

             val userReference = databaseReference.child("users").child(currentUser!!.uid)
                userReference.child("savedPost").child(postIds.toString()).addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            userReference.child("savedPost").child(postIds.toString()).removeValue()
                                .addOnSuccessListener {
                                    val clickedBlogItem = items.find { it.postId==postIds }
                                    clickedBlogItem?.isSaved = false
                                    notifyDataSetChanged()
                                    Toast.makeText(binding.root.context,"Blog Unsaved",Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener{
                                    Toast.makeText(binding.root.context,"Proocess Failed",Toast.LENGTH_LONG).show()
                                }
                            binding.recBookmark.setImageResource(R.drawable.icon_bookmark_outline)
                        }else{
                            //save your favourite blog post
                            userReference.child("savedPost").child(postIds.toString()).setValue(true)
                                .addOnSuccessListener {
                                    //update UI
                                    val clickedBlogItem = items.find { it.postId==postIds }
                                    clickedBlogItem?.isSaved = true
                                    notifyDataSetChanged()
                                    Toast.makeText(binding.root.context,"Blog Saved",Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener{
                                    Toast.makeText(binding.root.context,"Proocess Failed",Toast.LENGTH_LONG).show()
                                }
                            binding.recBookmark.setImageResource(R.drawable.icon_bookmark_red)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

    }
}
//    private fun handleLikeButtonClick(postId: String?, blogItemModel: Blog_item_model) {
//            val userReference = databaseReference.child("users").child(currentUser!!.uid.toString())
//            val likeReference = databaseReference.child("Blogs").child(postId.toString()).child("likes")
//
////          likeReference.child(currentUser.uid.toString()).addValueEventListener(object :ValueEventListener{
////              override fun onDataChange(snapshot: DataSnapshot) {
////                  if(snapshot.exists()){
////                      userReference.child("likes").child(postId.toString()).removeValue()
////                          .addOnSuccessListener {
////
////                          }
////                  }else{
////
////                  }
////              }
////
////              override fun onCancelled(error: DatabaseError) {
////
////              }
////
////          })
//    }

//}