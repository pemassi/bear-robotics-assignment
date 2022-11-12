package io.pemassi.bearroboticsassignment.domain.dto.command

data class DebitCardGetAccountCommand(
    val cardNumber: String,
    val pinNumber: String,
)
