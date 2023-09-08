package com.example.customerslistrating

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.customerslistrating.databinding.EmployerSampleBinding
import com.example.customerslistrating.databinding.MoreCommentsSampleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class MoreCommentsAdapter  : RecyclerView.Adapter<MoreCommentsAdapter.MoreCommentsHolder>() {
    private lateinit var context: Context
    private var commentsList = ArrayList<Comment>()

    class MoreCommentsHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = MoreCommentsSampleBinding.bind(view)
        private lateinit var databaseReference : DatabaseReference
        fun bind(comment: Comment) = with(binding){
            authorName.text = comment.authorName
            commentText.text = comment.text
            ratingBarEmployer.rating = comment.stars
            databaseReference = FirebaseDatabase.getInstance().getReference("UserApp")
            Glide.with(ivUserAvatar.context).load("https://media.threatpost.com/wp-content/uploads/sites/103/2019/09/26105755/fish-1.jpg").into(ivUserAvatar)
            val valueListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(ds : DataSnapshot in snapshot.children) {
                        var userApp = ds.getValue(UserApp::class.java) as UserApp
                        if (userApp != null) {
                            if(authorName.text == userApp.userName) {
                                site.text = userApp.userEmail
                                Glide.with(ivUserAvatar.context).load(userApp.imageUrl).into(ivUserAvatar)


                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
            databaseReference.addValueEventListener(valueListener)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreCommentsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.more_comments_sample, parent, false)
        context = parent.context
        return MoreCommentsHolder(view)
    }

    override fun getItemCount(): Int {
        return commentsList.size
    }

    override fun onBindViewHolder(holder: MoreCommentsHolder, position: Int) {
        holder.bind(commentsList[position])
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addComment(comment: Comment){
        commentsList.add(comment)
        notifyDataSetChanged()
    }
    fun getList() : ArrayList<Comment> {
        return commentsList
    }

}