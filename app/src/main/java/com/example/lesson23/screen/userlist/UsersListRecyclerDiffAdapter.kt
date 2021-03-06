package com.example.lesson23.screen.userlist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson23.databinding.ListItemBinding
import com.example.lesson23.db.User

class UsersListRecyclerDiffAdapter(
    private val layoutInflater: LayoutInflater,
    private val clickListener: UserClickListener
) : ListAdapter<User, UsersListRecyclerDiffAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private val checkedIds = mutableSetOf<Long>()

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
            checkbox.isChecked = checkedIds.contains(item.id)
            userFullNameTextView.text = "${item.id}. ${item.firstName} ${item.lastName}"
            userAddressTextView.text = item.address
        }
    }

    inner class MyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val user = getItem(adapterPosition)
                    clickListener.onUserClicked(user)
                }
            }

            binding.checkbox.setOnCheckedChangeListener(object :
                CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    if (adapterPosition == RecyclerView.NO_POSITION) {
                        return
                    }
                    val user = getItem(adapterPosition)

                    if (isChecked) {
                        checkedIds.add(user.id)
                    } else {
                        checkedIds.remove(user.id)
                    }

                    Log.i("UsersListRecyclerDiffAdapter", "checkedIds=$checkedIds")
                }
            })
        }
    }

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