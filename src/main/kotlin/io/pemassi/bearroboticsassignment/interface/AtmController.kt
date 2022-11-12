package io.pemassi.bearroboticsassignment.`interface`

import io.pemassi.bearroboticsassignment.domain.dto.command.*
import io.pemassi.bearroboticsassignment.domain.dto.info.AccountInfo
import io.pemassi.bearroboticsassignment.domain.dto.info.AccountTradeHistoryInfo
import io.pemassi.bearroboticsassignment.domain.dto.info.DebitCardInfo
import io.pemassi.bearroboticsassignment.domain.service.AccountService
import io.pemassi.bearroboticsassignment.domain.service.DebitCardService
import org.springframework.stereotype.Component

@Component
class AtmController(
    private val accountService: AccountService,
    private val debitCardService: DebitCardService,
)
{

    fun insertCard(
        cardNumber: String,
    ): DebitCardInfo
    {
        val command = DebitCardGetCommand(cardNumber)
        val info = debitCardService.getDebitCard(command)

        return info
    }

    fun enterPinNumber(
        cardNumber: String,
        pinNumber: String,
    ): List<AccountInfo>
    {
        val command = DebitCardGetAccountCommand(cardNumber, pinNumber)
        val info = debitCardService.getAccount(command)

        return info
    }

    fun selectAccount(
        accountNumber: String,
    ): AccountInfo
    {
        val command = AccountGetCommand(accountNumber)
        val info = accountService.get(command)

        return info
    }

    fun getAccountBalance(
        accountNumber: String,
    ): Long
    {
        val command = AccountGetBalanceCommand(accountNumber)
        val info = accountService.getBalance(command)

        return info
    }

    fun withdraw(
        accountNumber: String,
        withdrawAmount: Long,
        node: String,
    ): AccountTradeHistoryInfo
    {
        val command = AccountWithdrawCommand(accountNumber, withdrawAmount, node)
        val info = accountService.withdraw(command)

        return info
    }

    fun deposit(
        accountNumber: String,
        depositAmount: Long,
        node: String,
    ): AccountTradeHistoryInfo
    {
        val command = AccountDepositCommand(accountNumber, depositAmount, node)
        val info = accountService.deposit(command)

        return info
    }
}
