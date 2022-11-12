package io.pemassi.bearroboticsassignment.domain.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.pemassi.bearroboticsassignment.domain.dto.command.*
import io.pemassi.bearroboticsassignment.domain.dto.mapper.AccountMapper
import io.pemassi.bearroboticsassignment.domain.entity.Account
import io.pemassi.bearroboticsassignment.domain.entity.AccountTradeHistory
import io.pemassi.bearroboticsassignment.domain.model.TradeType
import io.pemassi.bearroboticsassignment.domain.repository.AccountRepository
import io.pemassi.bearroboticsassignment.domain.repository.AccountTradeHistoryRepository
import org.mapstruct.factory.Mappers

internal class AccountServiceTest: BehaviorSpec()
{
    init
    {
        val fixture = kotlinFixture()

        val accountRepository = mockk<AccountRepository>()
        val accountTradeHistoryRepository = mockk<AccountTradeHistoryRepository>()
        val accountMapper = Mappers.getMapper(AccountMapper::class.java)

        val accountService = AccountService(accountRepository, accountTradeHistoryRepository, accountMapper)

        Given("there is no account.") {
            val newAccount = Account(fixture(), fixture(), fixture())

            every { accountRepository.findByAccountNumber(any()) } returns null
            every { accountRepository.save(any()) } returns newAccount

            When("get account") {
                Then("throws exception") {
                    shouldThrow<NoSuchElementException> {
                        val accountNumber: String = fixture()
                        accountService.get(AccountGetCommand(accountNumber))
                    }
                }
            }

            When("get balance") {
                Then("throws exception") {
                    shouldThrow<NoSuchElementException> {
                        val accountNumber: String = fixture()
                        accountService.getBalance(AccountGetBalanceCommand(accountNumber))
                    }
                }
            }

            When("create account") {
                val command = fixture<AccountCreateCommand>()

                Then("account is created") {
                    val info = accountService.create(command)

                    info.accountNumber shouldBe newAccount.accountNumber
                }
            }
        }

        Given("there is account") {
            val account = Account(fixture(), fixture(), fixture())
            val balance = 10000L
            every { accountRepository.findByAccountNumber(account.accountNumber) } returns account
            every { accountTradeHistoryRepository.getBalance(account) } returns balance

            When("get account") {
                Then("return account") {
                    val info = accountService.get(AccountGetCommand(account.accountNumber))

                    info.id shouldBe account.id
                    info.accountNumber shouldBe account.accountNumber
                }
            }

            When("get balance") {
                Then("return balance") {
                    val info = accountService.getBalance(AccountGetBalanceCommand(account.accountNumber))

                    info shouldBe balance
                }
            }

            When("withdarw over balance") {
                val command = AccountWithdrawCommand(
                    accountNumber = account.accountNumber,
                    withdrawAmount = 10001L,
                    note = fixture(),
                )

                Then("throw exception") {
                    shouldThrow<IllegalArgumentException> {
                        accountService.withdraw(command)
                    }
                }
            }

            When("withdraw") {
                val command = AccountWithdrawCommand(
                    accountNumber = account.accountNumber,
                    withdrawAmount = 1000L,
                    note = fixture(),
                )
                val accountTradeHistory = AccountTradeHistory(
                    account = account,
                    tradeType = TradeType.WITHDRAW,
                    amount = command.withdrawAmount * -1,
                    note = command.note,
                )
                every { accountTradeHistoryRepository.save(any()) } returns accountTradeHistory

                Then("success") {
                    val info = accountService.withdraw(command)

                    info.account.accountNumber shouldBe account.accountNumber
                    info.account.id shouldBe account.id
                    info.amount shouldBe command.withdrawAmount * -1
                    info.note shouldBe command.note
                }
            }

            When("withdraw with wrong account number") {
                val command = AccountWithdrawCommand(
                    accountNumber = fixture(),
                    withdrawAmount = 1000L,
                    note = fixture(),
                )

                Then("throw exception") {
                    shouldThrow<NoSuchElementException> {
                        accountService.withdraw(command)
                    }
                }
            }

            When("deposit") {
                val command = AccountDepositCommand(
                    accountNumber = account.accountNumber,
                    depositAmount = 1000L,
                    note = fixture(),
                )
                val accountTradeHistory = AccountTradeHistory(
                    account = account,
                    tradeType = TradeType.DEPOSIT,
                    amount = command.depositAmount,
                    note = command.note,
                )
                every { accountTradeHistoryRepository.save(any()) } returns accountTradeHistory

                Then("success") {
                    val info = accountService.deposit(command)

                    info.account.accountNumber shouldBe account.accountNumber
                    info.account.id shouldBe account.id
                    info.amount shouldBe command.depositAmount
                    info.note shouldBe command.note
                }
            }

            When("deposit with wrong account number") {
                val command = AccountDepositCommand(
                    accountNumber = fixture(),
                    depositAmount = 1000L,
                    note = fixture(),
                )

                Then("throw exception") {
                    shouldThrow<NoSuchElementException> {
                        accountService.deposit(command)
                    }
                }
            }
        }
    }
}
