package io.pemassi.bearroboticsassignment.domain.dto.info

import io.pemassi.bearroboticsassignment.domain.model.TradeType

data class AccountTradeHistoryInfo(
    val id: Long,
    val account: AccountInfo,
    val tradeType: TradeType,
    val amount: Long,
    val note: String,
)
