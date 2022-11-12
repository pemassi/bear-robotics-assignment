package io.pemassi.bearroboticsassignment.domain.dto.command

data class AccountDepositCommand(
    val accountNumber: String,
    val depositAmount: Long,
    val note: String,
)
