package io.pemassi.bearroboticsassignment.domain.entity

import com.appmattus.kotlinfixture.kotlinFixture
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class DebitCardTest: BehaviorSpec()
{
    init
    {
        val fixture = kotlinFixture()

        Given("debit card") {
            val cardNumber: String = fixture()
            val pinNumber: String = fixture()
            val debitCard = DebitCard(cardNumber, pinNumber)

            Then("confirm with correct pin number") {
                debitCard.confirmPinNumber(pinNumber) shouldBe true
            }

            Then("confirm with wrong pin number") {
                debitCard.confirmPinNumber(fixture()) shouldBe false
            }
        }
    }
}
