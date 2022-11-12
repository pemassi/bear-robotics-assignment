package io.pemassi.bearroboticsassignment.domain.repository

import io.pemassi.bearroboticsassignment.domain.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountRepository: JpaRepository<Account, Long>
{
    fun findByAccountNumber(accountNumber: String): Optional<Account>
}
