package com.example.lesson23

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson23.UsersListRecyclerAdapter.MyViewHolder
import com.example.lesson23.databinding.ListItemBinding

class UsersListRecyclerAdapter(
    private val layoutInflater: LayoutInflater,
    private val clickListener: UserClickListener
) : RecyclerView.Adapter<MyViewHolder>() {

    private var users: MutableList<User> = mutableListOf()

    fun setData(users: List<User>) {
        this.users = users.toMutableList()
        notifyDataSetChanged()
    }

    fun addNewUser(user: User) {
        this.users.add(user)
        notifyItemInserted(users.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        Log.d("UsersListRecyclerAdapter", "onCreateViewHolder")
        val binding = ListItemBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = users[position]

        Log.d("UsersListRecyclerAdapter", "onBindViewHolder: $item")

        holder.binding.apply {
            userFullNameTextView.text = item.firstName + " " + item.lastName
            userAddressTextView.text = item.address
        }
    }

    override fun getItemCount(): Int = users.size

    inner class MyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val user = users[adapterPosition]
                    clickListener.onUserClicked(user)
                }
            }
        }
    }

    interface UserClickListener {
        fun onUserClicked(user: User)
    }
}