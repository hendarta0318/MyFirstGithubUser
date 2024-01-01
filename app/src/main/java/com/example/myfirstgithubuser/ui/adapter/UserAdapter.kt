package com.example.myfirstgithubuser.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.example.myfirstgithubuser.data.response.ItemsItem
import com.example.myfirstgithubuser.databinding.ItemUserBinding
import com.example.myfirstgithubuser.ui.activity.MainActivity.Companion.EXTRA_PROFILE
import com.example.myfirstgithubuser.ui.activity.DetailUserActivity

class UserAdapter : ListAdapter<ItemsItem, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            Toast.makeText(context, "Kamu memilih ${user.login}", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, DetailUserActivity::class.java)
            intent.putExtra(EXTRA_PROFILE, user.login)
            context.startActivity(intent)
        }
    }

    class MyViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemsItem) {
            Glide.with(itemView)
                .load(item.avatarUrl)
                .transition(DrawableTransitionOptions
                    .withCrossFade(DrawableCrossFadeFactory.Builder(1500)))
                .centerCrop()
                .circleCrop()
                .into(binding.ivProfile)
            binding.idUser.text = " ${item.nodeId}"
            binding.tvUser.text = item.login
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(
                oldItem: ItemsItem,
                newItem: ItemsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ItemsItem,
                newItem: ItemsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}