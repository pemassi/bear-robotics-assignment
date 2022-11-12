package io.pemassi.bearroboticsassignment.domain.entity

import io.pemassi.kotlin.extensions.common.hashWithSHA512
import javax.persistence.*

@Entity
class DebitCard(
    cardNumber: String,
    password: String,
    salt: String,
)
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    val cardNumber: String = cardNumber

    var hashedPinNumber: String = password
        private set

    var salt: String = salt
        private set

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val accounts: MutableList<Account> = mutableListOf()

    fun confirmPinNumber(pinNumber: String): Boolean
    {
        val hashedPinNumber = (pinNumber + salt).hashWithSHA512()
        return hashedPinNumber == this.hashedPinNumber
    }

    fun connectAccount(account: Account)
    {
        accounts.add(account)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as DebitCard

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
