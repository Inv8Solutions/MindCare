package com.inv8solutions.mindcare.models

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val isApproved: Boolean = false,
    val isLSN: Boolean = false,
    val registrationDate: Long = System.currentTimeMillis(),
    val approvedBy: String? = null,
    val approvedDate: Long? = null
)

