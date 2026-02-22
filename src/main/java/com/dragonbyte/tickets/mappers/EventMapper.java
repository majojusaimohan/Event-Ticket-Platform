package com.dragonbyte.tickets.mappers;

import com.dragonbyte.tickets.domain.CreateEventRequest;
import com.dragonbyte.tickets.domain.CreateTicketTypeRequest;
import com.dragonbyte.tickets.domain.dtos.CreateEventRequestDto;
import com.dragonbyte.tickets.domain.dtos.CreateEventResponseDto;
import com.dragonbyte.tickets.domain.dtos.CreateTicketTypeRequestDto;
import com.dragonbyte.tickets.domain.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

     CreateEventRequest fromDto(CreateEventRequestDto dto);

     CreateEventResponseDto toDto(Event event);
}
