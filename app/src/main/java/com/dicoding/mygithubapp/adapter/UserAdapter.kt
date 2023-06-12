package com.dicoding.mygithubapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mygithubapp.databinding.ItemUserBinding
import com.dicoding.mygithubapp.response.ItemsItem

class UserAdapter(private val listUser: List<ItemsItem>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUser[position]
        holder.bind(user)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUser[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listUser.size

    class ViewHolder(private val view: ItemUserBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(user: ItemsItem) {
            view.username.text = user.login

            Glide.with(itemView)
                .load(user.avatarUrl)
                .skipMemoryCache(true)
                .centerCrop()
                .into(view.profilePic)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: ItemsItem)
    }
}