package com.example.lesson23

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


class UsersListAdapter(
    private val layoutInflater: LayoutInflater,
    private val users: List<User>
    ): BaseAdapter() {

    override fun getCount() = users.size

    override fun getItem(position: Int) = users[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView = when(convertView) {
            null -> layoutInflater.inflate(R.layout.list_item, parent, false)
            else -> convertView
        }

        val item = getItem(position)

        val fullNameTextView = itemView.findViewById<TextView>(R.id.userFullNameTextView)
        val userAddressTextView = itemView.findViewById<TextView>(R.id.userAddressTextView)

        fullNameTextView.text = item.firstName + " " + item.lastName
        userAddressTextView.text = item.address

        return itemView
    }

}