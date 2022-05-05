package com.example.lesson23.network

data class ReqresUserListApiResponse(
    val page: Int,
    val per_page: Int,
    val total: Int,
    val total_pages: Int,
    val data: List<ReqresUser>
)