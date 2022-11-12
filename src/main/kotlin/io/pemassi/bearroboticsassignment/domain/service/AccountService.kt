package io.pemassi.bearroboticsassignment.domain.service

import io.pemassi.bearroboticsassignment.domain.dto.command.*
import io.pemassi.bearroboticsassignment.domain.dto.info.AccountInfo
import io.pemassi.bearroboticsassignment.domain.dto.info.AccountTradeHistoryInfo
import io.pemassi.bearroboticsassignment.domain.dto.mapper.AccountMapper
import io.pemassi.bearroboticsassignment.domain.entity.Account
import io.pemassi.bearroboticsassignment.domain.entity.AccountTradeHistory
import io.pemassi.bearroboticsassignment.domain.model.TradeType
import io.pemassi.bearroboticsassignment.domain.repository.AccountRepository
import io.pemassi.bearroboticsassignment.domain.repository.AccountTradeHistoryRepository
import io.pemassi.kotlin.extensions.common.hashWithSHA512
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AccountService(
    private val accountRepository: AccountRepository,
    private val accountTradeHistoryRepository: AccountTradeHistoryRepository,
    private val accountMapper: AccountMapper,
)
{
    fun get(command: AccountGetCommand): AccountInfo
    {
        val account = accountRepository.findByAccountNumber(command.accountNumber).orElseThrow()

        return accountMapper.of(account)
    }

    fun create(command: AccountCreateCommand): AccountInfo
    {
        // generate random account number
        val accountNumber = (1000000000..9999999999).random().toString()

        // generate random salt
        val salt = (1000000000..9999999999).random().toString()
        val hashedPassword = (command.password + salt).hashWithSHA512()

        val account = accountRepository.save(
            Account(
                accountNumber = accountNumber,
                hashedPassword = hashedPassword,
                salt = salt,
            )
        )

        return accountMapper.of(account)
    }

    fun withdraw(command: AccountWithdrawCommand): AccountTradeHistoryInfo
    {
        val account = accountRepository.findByAccountNumber(command.accountNumber).orElseThrow()

        // validation balance
        val balance = getBalance(
            AccountGetBalanceCommand(
                accountNumber = command.accountNumber
            )
        )
        if(balance < command.withdrawAmount)
            throw Exception("Not enough balance")

        val accountTradeHistory = accountTradeHistoryRepository.save(
            AccountTradeHistory(
                account = account,
                tradeType = TradeType.WITHDRAW,
                amount = command.withdrawAmount * -1,
                note = command.note,
            )
        )

        return accountMapper.of(accountTradeHistory)
    }

    fun deposit(command: AccountDepositCommand): AccountTradeHistoryInfo
    {
        val account = accountRepository.findByAccountNumber(command.accountNumber).orElseThrow()

        val accountTradeHistory = accountTradeHistoryRepository.save(
            AccountTradeHistory(
                account = account,
                tradeType = TradeType.DEPOSIT,
                amount = command.depositAmount,
                note = command.note,
            )
        )

        return accountMapper.of(accountTradeHistory)
    }

    fun getBalance(command: AccountGetBalanceCommand): Long
    {
        val account = accountRepository.findByAccountNumber(command.accountNumber).orElseThrow()

        return accountTradeHistoryRepository.getBalance(account)
    }
}
