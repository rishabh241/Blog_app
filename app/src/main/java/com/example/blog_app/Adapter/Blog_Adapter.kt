package com.example.blog_app.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.blog_app.Model.Blog_item_model
import com.example.blog_app.databinding.RecItemBinding

class Blog_Adapter(private val items: List<Blog_item_model>):RecyclerView.Adapter<Blog_Adapter.BlogViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val inflator = LayoutInflater.from(parent.context)
        val binding = RecItemBinding.inflate(inflator,parent, false)
        return BlogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


    inner class BlogViewHolder(private val binding: RecItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(blogItemModel: Blog_item_model) {
            binding.recHeading.text = blogItemModel.heading
            binding.recDate.text = blogItemModel.date
            binding.recDesc.text = blogItemModel.desc
            binding.recCount.text = blogItemModel.likeCount.toString()

            Glide.with(binding.recProfile.context)
                .load(blogItemModel.imageUrl)
                .into(binding.recProfile)

            binding.recBlogger.text = blogItemModel.userName
        }

    }

}