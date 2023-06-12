package com.dicoding.mygithubapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.mygithubapp.database.FavoriteUser
import com.dicoding.mygithubapp.databinding.ItemUserBinding

class FavoriteUserAdapter : RecyclerView.Adapter<FavoriteUserAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    private val listFavorite = ArrayList<FavoriteUser>()
    fun setListFavorite(listFav: List<FavoriteUser>) {
        listFavorite.clear()
        listFavorite.addAll(listFav)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = listFavorite[position]
        holder.bind(favorite)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listFavorite[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listFavorite.size

    class ViewHolder(private val view: ItemUserBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(user: FavoriteUser) {
            view.username.text = user.login

            Glide.with(itemView)
                .load(user.avatarUrl)
                .skipMemoryCache(true)
                .centerCrop()
                .into(view.profilePic)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(favorite: FavoriteUser)
    }
}