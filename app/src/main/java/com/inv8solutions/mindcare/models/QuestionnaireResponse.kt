package com.inv8solutions.mindcare.models

data class QuestionnaireResponse(
    val uid: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val responses: Map<String, String> = emptyMap(),
    val submittedDate: Long = System.currentTimeMillis(),
    val riskLevel: String = "Low" // Low, Medium, High
)

