package io.pemassi.bearroboticsassignment.domain.entity

import io.pemassi.bearroboticsassignment.domain.model.TradeType
import io.pemassi.kotlin.extensions.common.hashWithSHA512
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
class Account(
    accountNumber: String = (1000000000..9999999999).random().toString(),
    pinNumber: String,
    salt: String = (1000000000..9999999999).random().toString()
)
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    var accountNumber: String = accountNumber
        private set

    var hashedPinNumber: String = (pinNumber + salt).hashWithSHA512()
        private set

    var salt: String = salt
        private set

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var tradeHistories: MutableList<AccountTradeHistory> = mutableListOf()

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var debitCards: MutableList<DebitCard> = mutableListOf()

    fun confirmPinNumber(pinNumber: String): Boolean
    {
        val pinNumber = (pinNumber + salt).hashWithSHA512()
        return pinNumber == this.hashedPinNumber
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
