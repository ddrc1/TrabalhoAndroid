package com.example.recyclerviewapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewapi.R
import com.example.recyclerviewapi.model.RandomUser
import com.squareup.picasso.Picasso

class RandomUserAdapter(var randomUser: RandomUser?): RecyclerView.Adapter<RandomUserAdapter.RandomUserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomUserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_layout, parent, false)
        return RandomUserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return randomUser?.results?.size ?: 0
    }

    override fun onBindViewHolder(holder: RandomUserViewHolder, position: Int) {
        val user = randomUser?.results?.get(position)

        holder.userName.text = "${user?.name?.title} ${user?.name?.first} ${user?.name?.last}"
        holder.userEmail.text = user?.email
        holder.userPhone.text = user?.phone
        Picasso.get().load(user?.picture?.large).into(holder.userAvatar);
    }


    class RandomUserViewHolder(view: View): RecyclerView.ViewHolder(view){
        val userAvatar: ImageView = view.findViewById(R.id.item_imageView_avatar)
        val userName: TextView = view.findViewById(R.id.item_textView_nome)
        val userPhone: TextView = view.findViewById(R.id.item_textView_tel)
        val userEmail: TextView = view.findViewById(R.id.item_textView_email)
    }
}