package io.pemassi.bearroboticsassignment.domain.repository

import io.pemassi.bearroboticsassignment.domain.entity.Account
import io.pemassi.bearroboticsassignment.domain.entity.DebitCard
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DebitCardRepository: JpaRepository<DebitCard, Long>
{
    fun findByCardNumber(cardNumber: String): Optional<DebitCard>
}
