package io.pemassi.bearroboticsassignment.domain.entity

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.pemassi.bearroboticsassignment.domain.model.TradeType
import kotlin.math.absoluteValue

class AccountTradeHistoryTest: BehaviorSpec()
{
    init
    {
        val fixture = kotlinFixture()

        Given("deposit") {
            val account = Account(fixture(), fixture(), fixture())
            val depositAmount = fixture<Long>().absoluteValue
            val note = fixture<String>()

            When("amount is positive") {
                Then("success to create") {
                    val accountTradeHistory = AccountTradeHistory(account, TradeType.DEPOSIT, depositAmount, note)

                    accountTradeHistory.account shouldBe account
                    accountTradeHistory.tradeType shouldBe TradeType.DEPOSIT
                    accountTradeHistory.amount shouldBe depositAmount
                    accountTradeHistory.note shouldBe note
                }
            }

            When("amount is negative") {
                Then("throw exception") {
                    shouldThrow<IllegalArgumentException> {
                        AccountTradeHistory(account, TradeType.DEPOSIT, -depositAmount, note)
                    }
                }
            }
        }


        Given("withdraw") {
            val account = Account(fixture(), fixture(), fixture())
            val withdrawAmount = fixture<Long>().absoluteValue
            val note = fixture<String>()

            When("amount is negative") {
                Then("success to create") {
                    val accountTradeHistory = AccountTradeHistory(account, TradeType.WITHDRAW, -withdrawAmount, note)

                    accountTradeHistory.account shouldBe account
                    accountTradeHistory.tradeType shouldBe TradeType.WITHDRAW
                    accountTradeHistory.amount shouldBe -withdrawAmount
                    accountTradeHistory.note shouldBe note
                }
            }

            When("amount is positive") {
                Then("throw exception") {
                    shouldThrow<IllegalArgumentException> {
                        AccountTradeHistory(account, TradeType.WITHDRAW, withdrawAmount, note)
                    }
                }
            }
        }

    }
}
