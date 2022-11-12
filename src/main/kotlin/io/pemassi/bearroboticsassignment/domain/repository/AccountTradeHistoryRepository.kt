package io.pemassi.bearroboticsassignment.domain.repository

import io.pemassi.bearroboticsassignment.domain.entity.Account
import io.pemassi.bearroboticsassignment.domain.entity.AccountTradeHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AccountTradeHistoryRepository: JpaRepository<AccountTradeHistory, Long>
{
    @Query("""
        SELECT SUM(ath.amount) FROM AccountTradeHistory ath WHERE ath.account = :account
    """)
    fun getBalance(account: Account): Long
}
