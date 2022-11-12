package io.pemassi.bearroboticsassignment.domain.entity

import io.pemassi.bearroboticsassignment.domain.model.TradeType
import io.pemassi.kotlin.extensions.common.hashWithSHA512
import org.hibernate.Hibernate
import org.springframework.boot.autoconfigure.security.SecurityProperties
import javax.persistence.*

@Entity
class Account(
    accountNumber: String,
    hashedPassword: String,
    salt: String,
)
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    var accountNumber: String = accountNumber
        private set

    var hashedPassword: String = hashedPassword
        private set

    var salt: String = salt
        private set

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var tradeHistories: MutableList<AccountTradeHistory> = mutableListOf()

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var debitCards: MutableList<DebitCard> = mutableListOf()

    fun confirmPassword(password: String): Boolean
    {
        val hashedPassword = (password + salt).hashWithSHA512()
        return hashedPassword == this.hashedPassword
    }

    fun withdraw(amount: Long, note: String): AccountTradeHistory
    {
        val history = AccountTradeHistory(
            this,
            TradeType.WITHDRAW,
            amount,
            note
        )

        tradeHistories.add(history)
        return history
    }

    fun deposit(amount: Long, note: String): AccountTradeHistory
    {
        val history = AccountTradeHistory(
            this,
            TradeType.DEPOSIT,
            amount,
            note
        )

        tradeHistories.add(history)
        return history
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(
                other
            )
        ) return false
        other as Account

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
