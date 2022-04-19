package com.example.lesson23

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson23.databinding.ListItemBinding

class UsersListRecyclerDiffAdapter(
    private val layoutInflater: LayoutInflater,
    private val clickListener: UserClickListener
) : ListAdapter<User, UsersListRecyclerDiffAdapter.MyViewHolder>(DIFF_CALLBACK) {

    fun setData(users: List<User>) {
        submitList(users.toMutableList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        Log.d("UsersListRecyclerAdapter", "onCreateViewHolder")
        val binding = ListItemBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)

        Log.d("UsersListRecyclerAdapter", "onBindViewHolder: $item")

        holder.binding.apply {
            userFullNameTextView.text = item.firstName + " " + item.lastName
            userAddressTextView.text = item.address
        }

        holder.binding.root.setOnClickListener {
            clickListener.onUserClicked(item)
        }
    }

    class MyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    interface UserClickListener {
        fun onUserClicked(user: User)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<User> = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }
}