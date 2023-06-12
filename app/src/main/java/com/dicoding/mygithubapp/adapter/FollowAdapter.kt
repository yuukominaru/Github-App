package com.dicoding.mygithubapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mygithubapp.databinding.ItemUserBinding
import com.dicoding.mygithubapp.response.FollowResponseItem

class FollowAdapter(private val listFollow: List<FollowResponseItem>) : RecyclerView.Adapter<FollowAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listFollow[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = listFollow.size

    class ViewHolder(private val view: ItemUserBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(user: FollowResponseItem) {
            view.username.text = user.login

            Glide.with(itemView)
                .load(user.avatarUrl)
                .skipMemoryCache(true)
                .centerCrop()
                .into(view.profilePic)
        }
    }
}