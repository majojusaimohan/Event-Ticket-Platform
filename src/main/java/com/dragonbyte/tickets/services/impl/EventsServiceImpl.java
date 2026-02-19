package com.dragonbyte.tickets.services.impl;

import com.dragonbyte.tickets.domain.CreateEventRequest;
import com.dragonbyte.tickets.domain.entities.Event;
import com.dragonbyte.tickets.domain.entities.TicketType;
import com.dragonbyte.tickets.domain.entities.User;
import com.dragonbyte.tickets.exceptions.UserNotFoundException;
import com.dragonbyte.tickets.repositories.EventRepository;
import com.dragonbyte.tickets.repositories.UserRepository;
import com.dragonbyte.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventService {
    
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    @Override
    public Event createEvent(UUID organizerId, CreateEventRequest event) {
      User organizer = userRepository.findById(organizerId)
              .orElseThrow(() -> new UserNotFoundException("User with id " + organizerId + " not found"));

        List<TicketType> ticketTypesToCreate = event.getTicketTypes().stream().map(
                ticketType -> {
                    TicketType ticketTypeToCreate = new TicketType();
                    ticketTypeToCreate.setName(ticketType.getName());
                    ticketTypeToCreate.setPrice(ticketType.getPrice());
                    ticketTypeToCreate.setDescription(ticketType.getDescription());
                    ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                    return ticketTypeToCreate;
                }).toList();


      Event eventToCreate = new Event();
      eventToCreate.setName(event.getName());
      eventToCreate.setStart(event.getStart());
      eventToCreate.setEnd(event.getEnd());
      eventToCreate.setVenue(event.getVenue());
      eventToCreate.setSalesStart(event.getSalesStart());
      eventToCreate.setSalesEnd(event.getSalesEnd());
      eventToCreate.setStatus(event.getStatus());
      eventToCreate.setOrganizer(organizer);
      eventToCreate.setTicketTypes(ticketTypesToCreate);

      return eventRepository.save(eventToCreate);
    }
}
