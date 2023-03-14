package com.example.linkedin_clone.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.linkedin_clone.DataClasses.imageUsers
import com.example.linkedin_clone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ImageAdapter()
    : ListAdapter<imageUsers, ImageAdapter.ImageViewHolder>(
ImagesDiffUtilCallback()
) {
    private lateinit var dbref: DatabaseReference
    private lateinit var dbref1: DatabaseReference
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_post, parent, false)
        context = parent.context
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindView(item, context)
        val currentUid = FirebaseAuth.getInstance().currentUser!!.uid

        checkLikeStatus(item, holder.itemView)

        holder.itemView.findViewById<LinearLayout>(R.id.btn_like)?.setOnClickListener {
            val getButtonText =
                holder.itemView.findViewById<TextView>(R.id.like_clr).text.toString()
            dbref1 = FirebaseDatabase.getInstance().getReference("Images").child(item.id).child("likes")
            if (getButtonText == "Liked") {
                dbref1.child(currentUid).removeValue()
                holder.itemView.findViewById<ImageView>(R.id.ic_like)
                    .setImageResource(R.drawable.ic_like)
                holder.itemView.findViewById<TextView>(R.id.like_clr).text = "Like"
            } else {
                dbref1.child(currentUid).setValue("")
                holder.itemView.findViewById<ImageView>(R.id.ic_like)
                    .setImageResource(R.drawable.ic_liked)
                holder.itemView.findViewById<TextView>(R.id.like_clr).text = "Liked"
            }
//            Toast.makeText(context,item.id,Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkLikeStatus(item: imageUsers, itemView: View) {
        val currentUid = FirebaseAuth.getInstance().currentUser!!.uid
        dbref = FirebaseDatabase.getInstance().getReference("Images").child(item.id).child("likes")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    itemView.findViewById<RelativeLayout>(R.id.ll2).visibility = View.VISIBLE
                    itemView.findViewById<TextView>(R.id.likesTxt).text = snapshot.childrenCount.toString()
                }
                if (snapshot.child(currentUid).exists()) {
                    itemView.findViewById<ImageView>(R.id.ic_like)
                        .setImageResource(R.drawable.ic_liked)
                    itemView.findViewById<TextView>(R.id.like_clr).text = "Liked"
                } else {
                    itemView.findViewById<ImageView>(R.id.ic_like)
                        .setImageResource(R.drawable.ic_like)
                    itemView.findViewById<TextView>(R.id.like_clr).text = "Like"
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = itemView.findViewById(R.id.post_img)
        val caption: TextView = itemView.findViewById(R.id.caption)
        val username: TextView = itemView.findViewById(R.id.username)
        val time: TextView = itemView.findViewById(R.id.time_stamp)
        val headLine: TextView = itemView.findViewById(R.id.user_headline)
        fun bindView(item: imageUsers, context: Context) {
            Glide.with(context).load(item.imageURL).into(image)
            caption.text = item.caption
            username.text = item.name
            time.text = item.timeStamp
            headLine.text = item.headline
        }
    }
}
class ImagesDiffUtilCallback() : DiffUtil.ItemCallback<imageUsers>() {
    override fun areItemsTheSame(oldItem: imageUsers, newItem: imageUsers): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: imageUsers, newItem: imageUsers): Boolean {
        return oldItem == newItem
    }

}