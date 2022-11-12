package io.pemassi.bearroboticsassignment.domain.service

import io.pemassi.bearroboticsassignment.domain.dto.command.DebitCardGetAccountCommand
import io.pemassi.bearroboticsassignment.domain.dto.command.DebitCardGetCommand
import io.pemassi.bearroboticsassignment.domain.dto.info.AccountInfo
import io.pemassi.bearroboticsassignment.domain.dto.info.DebitCardInfo
import io.pemassi.bearroboticsassignment.domain.dto.mapper.AccountMapper
import io.pemassi.bearroboticsassignment.domain.dto.mapper.DebitCardMapper
import io.pemassi.bearroboticsassignment.domain.repository.DebitCardRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DebitCardService(
    private val debitCardRepository: DebitCardRepository,
    private val accountMapper: AccountMapper,
    private val debitCardMapper: DebitCardMapper,
    private val accountService: AccountService,
)
{
    fun get(command: DebitCardGetCommand): DebitCardInfo
    {
        val debitCard = debitCardRepository.findByCardNumber(command.cardNumber).orElseThrow()

        return debitCardMapper.of(debitCard)
    }

    fun getAccount(command: DebitCardGetAccountCommand): List<AccountInfo>
    {
        val debitCard = debitCardRepository.findByCardNumber(command.cardNumber).orElseThrow()

        // validation password
        if(!debitCard.confirmPinNumber(command.pinNumber))
            throw IllegalArgumentException("Password is not correct.")

        return debitCard.accounts.map {
            accountMapper.of(it)
        }
    }
}
