package io.pemassi.bearroboticsassignment.domain.service

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.pemassi.bearroboticsassignment.domain.dto.command.DebitCardGetAccountCommand
import io.pemassi.bearroboticsassignment.domain.dto.command.DebitCardGetCommand
import io.pemassi.bearroboticsassignment.domain.dto.mapper.AccountMapper
import io.pemassi.bearroboticsassignment.domain.dto.mapper.DebitCardMapper
import io.pemassi.bearroboticsassignment.domain.entity.Account
import io.pemassi.bearroboticsassignment.domain.entity.DebitCard
import io.pemassi.bearroboticsassignment.domain.repository.DebitCardRepository
import org.mapstruct.factory.Mappers
import java.util.*

class DebitCardServiceTest: BehaviorSpec()
{
    init
    {
        val fixture = kotlinFixture()

        val debitCardRepository = mockk<DebitCardRepository>()
        val accountService = mockk<AccountService>()
        val accountMapper = Mappers.getMapper(AccountMapper::class.java)
        val debitCardMapper = Mappers.getMapper(DebitCardMapper::class.java)

        val debitCardService = DebitCardService(debitCardRepository, accountMapper, debitCardMapper, accountService)

        Given("there is no debit card.") {
            val cardNumber: String = fixture()
            every { debitCardRepository.findByCardNumber(any()) } returns Optional.empty()

            When("get debit card") {
                Then("throws exception") {
                    shouldThrow<NoSuchElementException> {
                        debitCardService.get(DebitCardGetCommand(cardNumber))
                    }
                }
            }

            When("get account") {
                Then("throws exception") {
                    shouldThrow<NoSuchElementException> {
                        debitCardService.getAccount(DebitCardGetAccountCommand(cardNumber, fixture()))
                    }
                }
            }
        }

        Given("there is debit card.") {
            val pinNumber = fixture<String>()
            val debitCard = DebitCard(
                fixture(),
                pinNumber,
                fixture(),
            )

            val accountNumberOne = fixture<String>()
            val accountNumberTwo = fixture<String>()

            val accountOne = Account(
                accountNumberOne,
                fixture(),
                fixture(),
            )

            val accountTwo = Account(
                accountNumberTwo,
                fixture(),
                fixture(),
            )

            debitCard.connectAccount(accountOne)
            debitCard.connectAccount(accountTwo)

            every { debitCardRepository.findByCardNumber(debitCard.cardNumber) } returns Optional.of(debitCard)

            When("get debit card") {
                Then("returns debit card info") {
                    val info = debitCardService.get(DebitCardGetCommand(debitCard.cardNumber))

                    info.id shouldBe debitCard.id
                    info.cardNumber shouldBe debitCard.cardNumber
                }
            }

            When("get account with correct pin number") {
                Then("returns account info") {
                    val accountInfo = debitCardService.getAccount(DebitCardGetAccountCommand(debitCard.cardNumber, pinNumber))

                    accountInfo.size shouldBe debitCard.accounts.size
                    accountInfo[0].accountNumber shouldBe accountNumberOne
                    accountInfo[1].accountNumber shouldBe accountNumberTwo
                }
            }

            When("get account with wrong correct pin number") {
                Then("throws exception") {
                    shouldThrow<IllegalArgumentException> {
                        debitCardService.getAccount(DebitCardGetAccountCommand(debitCard.cardNumber, fixture()))
                    }
                }
            }
        }
    }
}
