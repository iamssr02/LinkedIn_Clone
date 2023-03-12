package com.example.linkedin_clone.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.linkedin_clone.DataClasses.ConnectionRequestUser
import com.example.linkedin_clone.DataClasses.imageUsers
import com.example.linkedin_clone.R

class ImageAdapter(
    private val imageList: ArrayList<imageUsers>,
    private val context: Context) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_post, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = imageList[position]
        holder.bindView(item, context)
    }

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = itemView.findViewById(R.id.post_img)
        val caption: TextView = itemView.findViewById(R.id.caption)
        fun bindView(item: imageUsers, context: Context) {
            Glide.with(context).load(item.imageURL).into(image)
            caption.text = item.caption
        }
    }
}