package com.example.lesson23.network

class UserNotFoundException : RuntimeException("User not found")
class InvalidResponseException : RuntimeException("Response body is null")