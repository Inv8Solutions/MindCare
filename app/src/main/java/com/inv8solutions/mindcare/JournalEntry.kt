package com.inv8solutions.mindcare

data class JournalEntry(
    val title: String = "",
    val thoughts: String = "",
    val category: String = "",
    val timestamp: Long = 0
)
