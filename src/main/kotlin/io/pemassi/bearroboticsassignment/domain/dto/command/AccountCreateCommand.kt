package io.pemassi.bearroboticsassignment.domain.dto.command

data class AccountCreateCommand(
    val userId: Long,
    val password: String,
)
