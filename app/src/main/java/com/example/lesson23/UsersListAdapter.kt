package com.example.lesson23

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.lesson23.databinding.ListItemBinding


class UsersListAdapter(
    private val layoutInflater: LayoutInflater,
    private val users: List<User>
    ): BaseAdapter() {

    override fun getCount() = users.size

    override fun getItem(position: Int) = users[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //Log.d
        val itemView = when (convertView) {
            null -> layoutInflater.inflate(R.layout.list_item, parent, false)
            else -> convertView
        }

//        val itemView = layoutInflater.inflate(R.layout.list_item, parent, false)

        val item = getItem(position)

        ListItemBinding.bind(itemView).apply {
            userFullNameTextView.text = item.firstName + " " + item.lastName
            userAddressTextView.text = item.address
        }

        return itemView
    }

}