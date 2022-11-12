package io.pemassi.bearroboticsassignment.domain.dto.mapper

import io.pemassi.bearroboticsassignment.domain.dto.info.AccountInfo
import io.pemassi.bearroboticsassignment.domain.dto.info.AccountTradeHistoryInfo
import io.pemassi.bearroboticsassignment.domain.entity.Account
import io.pemassi.bearroboticsassignment.domain.entity.AccountTradeHistory
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface AccountMapper {

    // entity -> info
    fun of(entity: Account): AccountInfo
    fun of(entity: AccountTradeHistory): AccountTradeHistoryInfo
}
