package com.example.lesson23.repository

import org.junit.Assert.*

import org.junit.Test

class UserQueryTest {
    @Test
    fun when_Constructed_Then_NoSearchQuery() {
        // GIVEN-WHEN
        val sut = UserQuery()

        // THEN
        assertFalse(sut.hasSearchQuery())
    }

    @Test
    fun when_NullQuery_Then_NoSearchQuery() {
        // GIVEN-WHEN
        val sut = UserQuery(searchQuery = null)

        // THEN
        assertFalse(sut.hasSearchQuery())
    }

    @Test
    fun when_EmptyQuery_Then_NoSearchQuery() {
        // GIVEN-WHEN
        val sut = UserQuery(searchQuery = "")

        // THEN
        assertFalse(sut.hasSearchQuery())
    }

    @Test
    fun when_BlankQuery_Then_NoSearchQuery() {
        // GIVEN-WHEN
        val sut = UserQuery(searchQuery = "   ")

        // THEN
        assertFalse(sut.hasSearchQuery())
    }

    @Test
    fun when_QueryIsPresent_Then_HasSearchQuery() {
        // GIVEN-WHEN
        val sut = UserQuery(searchQuery = "john")

        // THEN
        assertTrue(sut.hasSearchQuery())
    }
}