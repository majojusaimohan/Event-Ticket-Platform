package com.dragonbyte.tickets.mappers;

import com.dragonbyte.tickets.domain.dtos.TicketValidationResponseDto;
import com.dragonbyte.tickets.domain.entities.TicketValidation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketValidationMapper {

    @Mapping(target="ticketId", source="ticket.id")
    TicketValidationResponseDto  toTicketValidationResponseDto(TicketValidation ticketValidation);
}
