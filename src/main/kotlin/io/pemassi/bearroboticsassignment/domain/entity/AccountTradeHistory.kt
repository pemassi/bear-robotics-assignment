package io.pemassi.bearroboticsassignment.domain.entity

import io.pemassi.bearroboticsassignment.domain.model.TradeType
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
class AccountTradeHistory(
    account: Account,
    tradeType: TradeType,
    amount: Long,
    note: String,
)
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val account: Account = account

    @Enumerated(EnumType.STRING)
    val tradeType: TradeType = tradeType

    val amount: Long = amount

    val note: String = note

    init
    {
        when(tradeType)
        {
            TradeType.DEPOSIT -> require(amount > 0) { "Amount must be greater than 0" }
            TradeType.WITHDRAW -> require(amount < 0) { "Amount must be less than 0" }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(
                other
            )
        ) return false
        other as AccountTradeHistory

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
