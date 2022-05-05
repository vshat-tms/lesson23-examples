package com.example.lesson23.mappers

import com.example.lesson23.db.User
import com.example.lesson23.network.ReqresUser
import com.example.lesson23.network.ReqresUserApiResponse
import com.example.lesson23.network.ReqresUserListApiResponse

class ReqresUserToUserMapper {
    fun toUser(reqresUser: ReqresUser) = User(
        id = reqresUser.id,
        firstName = reqresUser.first_name,
        lastName = reqresUser.last_name,
        address = reqresUser.email
    )

    fun toReqresUser(user: User) = ReqresUser(
        id = user.id,
        email = user.address,
        first_name = user.firstName,
        last_name = user.lastName,
        avatar = ""
    )

    fun toUser(apiResponse: ReqresUserApiResponse) = toUser(apiResponse.data)

    fun toUserList(apiResponse: ReqresUserListApiResponse) = apiResponse.data.map {
        toUser(it)
    }
}