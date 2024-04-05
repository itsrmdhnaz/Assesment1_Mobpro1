package org.d3if3026.assesment1.model

import java.time.LocalDate
import java.util.UUID

data class User(
    val id: UUID,
    val username: String,
    val password: String,
    val transaction: List<Transaction> = listOf(),
    val currency: String
)

data class Transaction(
    val id: UUID,
    val amount: Float,
    val date: LocalDate,
    val transferTo: String? = null,
)

data class Auth(
    val user: User
)