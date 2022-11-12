package io.pemassi.bearroboticsassignment.`interface`

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.pemassi.bearroboticsassignment.domain.dto.command.AccountGetBalanceCommand
import io.pemassi.bearroboticsassignment.domain.dto.command.AccountGetCommand
import io.pemassi.bearroboticsassignment.domain.dto.command.DebitCardGetAccountCommand
import io.pemassi.bearroboticsassignment.domain.dto.info.AccountInfo
import io.pemassi.bearroboticsassignment.domain.dto.info.AccountTradeHistoryInfo
import io.pemassi.bearroboticsassignment.domain.dto.info.DebitCardInfo
import io.pemassi.bearroboticsassignment.domain.model.TradeType
import io.pemassi.bearroboticsassignment.domain.service.AccountService
import io.pemassi.bearroboticsassignment.domain.service.DebitCardService

class AtmControllerTest : BehaviorSpec()
{
    init
    {
        val fixture = kotlinFixture()

        val accountService = mockk<AccountService>()
        val debitCardService = mockk<DebitCardService>()

        val atmController = AtmController(accountService, debitCardService)

        Given("there is no debit card.") {
            val cardNumber: String = fixture()
            every { debitCardService.get(any()) } throws NoSuchElementException()

            When("insert card") {
                Then("throws exception") {
                    shouldThrow<NoSuchElementException> {
                        atmController.insertCard(cardNumber)
                    }
                }
            }
        }

        Given("there is debit card.") {
            val cardNumber: String = fixture()
            val pinNumber: String = fixture()
            val wrongPinNumber: String = fixture()
            val accountOne = AccountInfo(id = fixture(), accountNumber = fixture())
            val accountTwo = AccountInfo(id = fixture(), accountNumber = fixture())
            val accountOneBalance: Long = fixture()

            every { debitCardService.get(any()) } returns DebitCardInfo(
                id = fixture(),
                cardNumber = cardNumber,
            )
            every { debitCardService.getAccount(DebitCardGetAccountCommand(cardNumber, pinNumber)) }.returnsMany(
                listOf(accountOne, accountTwo)
            )
            every { debitCardService.getAccount(DebitCardGetAccountCommand(cardNumber, wrongPinNumber)) } throws IllegalArgumentException("Password is not correct.")
            every { accountService.get(AccountGetCommand(accountOne.accountNumber)) } returns accountOne
            every { accountService.get(AccountGetCommand(accountTwo.accountNumber)) } returns accountTwo
            every { accountService.getBalance(AccountGetBalanceCommand(accountOne.accountNumber)) } returns accountOneBalance



            When("insert card") {
                Then("success") {
                    val info = atmController.insertCard(cardNumber)
                    info.cardNumber shouldBe cardNumber
                }
            }

            When("enter pin number") {
                Then("success") {
                    val info = atmController.enterPinNumber(cardNumber, pinNumber)
                    info.size shouldBe 2
                    info[0].accountNumber shouldBe accountOne.accountNumber
                    info[1].accountNumber shouldBe accountTwo.accountNumber
                }
            }

            When("enter wrong pin number") {
                Then("throws exception") {
                    shouldThrow<IllegalArgumentException> {
                        atmController.enterPinNumber(cardNumber, wrongPinNumber)
                    }
                }
            }

            When("select account") {
                Then("success") {
                    val info = atmController.selectAccount(accountOne.accountNumber)
                    info.accountNumber shouldBe accountOne.accountNumber
                }
            }

            When("get account balance") {
                Then("success") {
                    val info = atmController.getAccountBalance(accountOne.accountNumber)
                    info shouldBe accountOneBalance
                }
            }

            When("withdraw") {
                val withdrawAmount: Long = fixture()
                val note: String = fixture()

                every { accountService.withdraw(any()) } returns AccountTradeHistoryInfo(
                    id = fixture(),
                    account = accountOne,
                    tradeType = TradeType.WITHDRAW,
                    amount = withdrawAmount * -1,
                    note = note,
                )

                Then("success") {
                    val info = atmController.withdraw(accountOne.accountNumber, withdrawAmount, note)
                    info.account.accountNumber shouldBe accountOne.accountNumber
                    info.amount shouldBe withdrawAmount * -1
                    info.note shouldBe note
                }
            }

            When("deposit") {
                val depositAmount: Long = fixture()
                val note: String = fixture()

                every { accountService.deposit(any()) } returns AccountTradeHistoryInfo(
                    id = fixture(),
                    account = accountOne,
                    tradeType = TradeType.DEPOSIT,
                    amount = depositAmount,
                    note = note,
                )

                Then("success") {
                    val info = atmController.deposit(accountOne.accountNumber, depositAmount, note)
                    info.account.accountNumber shouldBe accountOne.accountNumber
                    info.amount shouldBe depositAmount
                    info.note shouldBe note
                }
            }
        }
    }
}
