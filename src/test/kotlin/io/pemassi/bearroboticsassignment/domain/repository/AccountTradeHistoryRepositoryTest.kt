package io.pemassi.bearroboticsassignment.domain.repository

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.matchers.shouldBe
import io.pemassi.bearroboticsassignment.domain.entity.Account
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional

@ExtendWith(SpringExtension::class)
@DataJpaTest
@Transactional
class AccountTradeHistoryRepositoryTest
{
    @Autowired
    private lateinit var accountRepository: AccountRepository

    @Autowired
    private lateinit var accountTradeHistoryRepository: AccountTradeHistoryRepository

    private val fixture = kotlinFixture()

    @Test
    fun getBalance() {
        //given
        val account = Account(fixture(), fixture(), fixture())
        accountRepository.save(account)

        account.deposit(10000, fixture())
        account.withdraw(-5000, fixture())
        accountRepository.save(account)

        //when
        val balance = accountTradeHistoryRepository.getBalance(account)

        //then
        balance shouldBe 5000L
    }

}
