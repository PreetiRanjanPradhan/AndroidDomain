package com.example.androiddomain.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androiddomain.activity.ChatActivity
import com.example.androiddomain.databinding.ItemUserLayoutBinding
import com.example.androiddomain.model.UserModel

class DatingAdapter(val context: Context, val list : ArrayList<UserModel>) : RecyclerView.Adapter<DatingAdapter.DatingViewHolder>() {
    inner class DatingViewHolder(val binding : ItemUserLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatingViewHolder {

        return DatingViewHolder(ItemUserLayoutBinding.inflate(LayoutInflater.from(context)
        ,parent,false))

    }

    override fun onBindViewHolder(holder: DatingViewHolder, position: Int) {

        holder.binding.textView5.text = list[position].name
        holder.binding.textView4.text = list[position].email

        Glide.with(context).load(list[position].image).into(holder.binding.userImage)


        holder.binding.chat.setOnClickListener {
            val inte = Intent(context, ChatActivity::class.java)
            inte.putExtra("userId",list[position].number)
            context.startActivity(inte)

        }

    }
    override fun getItemCount(): Int {
       return  list.size
    }

}