package com.example.lesson23.mappers

import com.example.lesson23.db.User
import com.example.lesson23.network.ReqresUser
import com.example.lesson23.network.ReqresUserApiResponse
import com.example.lesson23.network.ReqresUserListApiResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class ReqresUserToUserMapperTest {
    val sut = ReqresUserToUserMapper()

    @Test
    fun testMappingReqresUserToUser() {
        // GIVEN
        val expected = User(
            0,
            "first",
            "last",
            "email"
        )

        // WHEN
        val actual = sut.toUser(DEMO_REQRES_USER_0)

        // THEN
        assertEquals(expected, actual)
    }

    @Test
    fun testMappingUserToReqresUser() {
        // GIVEN
        val user = DEMO_USER_0

        val expected = ReqresUser(
            0,
            "email",
            "first",
            "last",
            ""
        )

        // WHEN
        val actual = sut.toReqresUser(user)

        // THEN
        assertEquals(expected, actual)
    }

    @Test
    fun testMappingReqresUserApiResponseToUser() {
        // GIVEN
        val reqresUserApiResponse = ReqresUserApiResponse(
            DEMO_REQRES_USER_0
        )

        val expected = User(
            0,
            "first",
            "last",
            "email"
        )

        // WHEN
        val actual = sut.toUser(reqresUserApiResponse)

        // THEN
        assertEquals(expected, actual)
    }

    @Test
    fun testMappingReqresUserListApiResponseToUser() {
        // GIVEN
        val apiResponse = ReqresUserListApiResponse(
            page = 1,
            per_page = 8,
            total = 3,
            total_pages = 3,
            data = listOf(
                DEMO_REQRES_USER_0,
                DEMO_REQRES_USER_1
            )
        )

        val expected = listOf(
            User(
                0,
                "first",
                "last",
                "email"
            ),
            User(
                1,
                "first1",
                "last1",
                "email1"
            ),
        )

        // WHEN
        val actual = sut.toUserList(apiResponse)

        // THEN
        assertEquals(expected, actual)
    }

    companion object {
        private val DEMO_USER_0 = User(
            0,
            "first",
            "last",
            "email"
        )

        private val DEMO_REQRES_USER_0 = ReqresUser(
            0,
            "email",
            "first",
            "last",
            "avatar"
        )

        private val DEMO_REQRES_USER_1 = ReqresUser(
            1,
            "email1",
            "first1",
            "last1",
            "avatar1"
        )
    }
}