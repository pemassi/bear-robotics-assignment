package io.pemassi.bearroboticsassignment.domain.dto.command

data class AccountWithdrawCommand(
    val accountNumber: String,
    val withdrawAmount: Long,
    val note: String,
)
