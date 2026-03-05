package com.dragonbyte.tickets.controllers;

import com.dragonbyte.tickets.domain.dtos.TicketValidationRequestDto;
import com.dragonbyte.tickets.domain.dtos.TicketValidationResponseDto;
import com.dragonbyte.tickets.domain.entities.TicketValidation;
import com.dragonbyte.tickets.domain.entities.TicketValidationMethod;
import com.dragonbyte.tickets.mappers.TicketValidationMapper;
import com.dragonbyte.tickets.services.TicketValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/api/v1/ticket-validations")
@RequiredArgsConstructor
public class TicketValidationController {

    private final TicketValidationService ticketValidationService;
    private final TicketValidationMapper ticketValidationMapper;

    @PostMapping
    public ResponseEntity<TicketValidationResponseDto> validateTicket(
            @RequestBody TicketValidationRequestDto ticketValidationRequestDto
            ){
        TicketValidationMethod method = ticketValidationRequestDto.getMethod();
        TicketValidation ticketValidation;

        if(method.equals(TicketValidationMethod.QR_SCAN)){
             ticketValidation = ticketValidationService.validateTicketByQrCode(ticketValidationRequestDto.getId());
        }
        else{
            ticketValidation = ticketValidationService.validateTicketManually(ticketValidationRequestDto.getId());
        }

        return ResponseEntity.ok(ticketValidationMapper.toTicketValidationResponseDto(ticketValidation));


    }
}
