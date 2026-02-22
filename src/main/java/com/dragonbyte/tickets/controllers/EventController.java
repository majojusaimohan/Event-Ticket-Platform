package com.dragonbyte.tickets.controllers;

import com.dragonbyte.tickets.domain.CreateEventRequest;
import com.dragonbyte.tickets.domain.dtos.CreateEventRequestDto;
import com.dragonbyte.tickets.domain.dtos.CreateEventResponseDto;
import com.dragonbyte.tickets.domain.entities.Event;
import com.dragonbyte.tickets.mappers.EventMapper;
import com.dragonbyte.tickets.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventMapper eventMapper;
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<CreateEventResponseDto> creteEvent(
            @AuthenticationPrincipal Jwt jwt,
           @Valid @RequestBody CreateEventRequestDto createEventRequestDto
            ){
        CreateEventRequest createEventRequest = eventMapper.fromDto(createEventRequestDto);
        UUID userID = UUID.fromString(jwt.getSubject());
        Event createdEvent = eventService.createEvent(userID, createEventRequest);
        CreateEventResponseDto createEventResponseDto = eventMapper.toDto(createdEvent);
        return  new ResponseEntity<>(createEventResponseDto, HttpStatus.CREATED);


    }

}
