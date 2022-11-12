package io.pemassi.bearroboticsassignment.domain.dto.mapper

import io.pemassi.bearroboticsassignment.domain.dto.info.DebitCardInfo
import io.pemassi.bearroboticsassignment.domain.entity.DebitCard
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface DebitCardMapper {

    // entity -> info
    fun of(entity: DebitCard): DebitCardInfo

}
