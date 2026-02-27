package com.dragonbyte.tickets.services.impl;

import com.dragonbyte.tickets.domain.CreateEventRequest;
import com.dragonbyte.tickets.domain.UpdateEventRequest;
import com.dragonbyte.tickets.domain.UpdateTicketTypeRequest;
import com.dragonbyte.tickets.domain.entities.Event;
import com.dragonbyte.tickets.domain.entities.EventStatusEnum;
import com.dragonbyte.tickets.domain.entities.TicketType;
import com.dragonbyte.tickets.domain.entities.User;
import com.dragonbyte.tickets.exceptions.EventNotFoundException;
import com.dragonbyte.tickets.exceptions.EventUpdateException;
import com.dragonbyte.tickets.exceptions.UserNotFoundException;
import com.dragonbyte.tickets.repositories.EventRepository;
import com.dragonbyte.tickets.repositories.UserRepository;
import com.dragonbyte.tickets.services.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventService {
    
    private final UserRepository userRepository;
    private final EventRepository eventRepository;


    @Override
    @Transactional
    public Event createEvent(UUID organizerId, CreateEventRequest event) {
      User organizer = userRepository.findById(organizerId)
              .orElseThrow(() -> new UserNotFoundException("User with id " + organizerId + " not found"));

        Event eventToCreate = new Event();

        List<TicketType> ticketTypesToCreate = event.getTicketTypes().stream().map(
                ticketType -> {
                    TicketType ticketTypeToCreate = new TicketType();
                    ticketTypeToCreate.setName(ticketType.getName());
                    ticketTypeToCreate.setPrice(ticketType.getPrice());
                    ticketTypeToCreate.setDescription(ticketType.getDescription());
                    ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                    ticketTypeToCreate.setEvent(eventToCreate);
                    return ticketTypeToCreate;
                }).toList();



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

    @Override
    public Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable) {
       return eventRepository.findByOrganizerId(organizerId, pageable);
    }

    @Override
    public Optional<Event> getEventForOrganizer(UUID organizerId, UUID id) {
        return eventRepository.findByIdAndOrganizerId(id, organizerId);
    }

    @Override
    @Transactional
    public Event updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequest updateEventRequest) {
        if(updateEventRequest.getId()==null){
            throw new EventNotFoundException("Event ID cannot be null");
        }

        if(!id.equals(updateEventRequest.getId())){
            throw new EventUpdateException("Cannot update the Id of an event");
        }

        Event existingEvent = eventRepository
                .findByIdAndOrganizerId(id, organizerId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + id + " not found for organizer with id " + organizerId));

        existingEvent.setName(updateEventRequest.getName());
        existingEvent.setStart(updateEventRequest.getStart());
        existingEvent.setEnd(updateEventRequest.getEnd());
        existingEvent.setVenue(updateEventRequest.getVenue());
        existingEvent.setSalesStart(updateEventRequest.getSalesStart());
        existingEvent.setSalesEnd(updateEventRequest.getSalesEnd());
        existingEvent.setStatus(updateEventRequest.getStatus());

        Set<UUID> requestTIcketTypeIds = updateEventRequest.getTicketTypes().stream()
                .map(UpdateTicketTypeRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEvent.getTicketTypes().removeIf(existingTicketType -> !requestTIcketTypeIds.contains(existingTicketType.getId()));

        Map<UUID, TicketType> existingTicketTypesIndex = existingEvent.getTicketTypes().stream()
                .collect(Collectors.toMap(TicketType::getId, Function.identity()));

        for(UpdateTicketTypeRequest ticketType : updateEventRequest.getTicketTypes()){
            if(null==ticketType.getId()){
                TicketType ticketTypeToCreate = new TicketType();
                ticketTypeToCreate.setName(ticketType.getName());
                ticketTypeToCreate.setPrice(ticketType.getPrice());
                ticketTypeToCreate.setDescription(ticketType.getDescription());
                ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                ticketTypeToCreate.setEvent(existingEvent);
                existingEvent.getTicketTypes().add(ticketTypeToCreate);

            } else if (existingTicketTypesIndex.containsKey(ticketType.getId())) {
                TicketType ticketTypeToUpdate = existingTicketTypesIndex.get(ticketType.getId());
                ticketTypeToUpdate.setName(ticketType.getName());
                ticketTypeToUpdate.setPrice(ticketType.getPrice());
                ticketTypeToUpdate.setDescription(ticketType.getDescription());
                ticketTypeToUpdate.setTotalAvailable(ticketType.getTotalAvailable());
            } else {
                throw new EventUpdateException("Ticket type with id " + ticketType.getId() + " not found in existing event");

            }
        }

    return  eventRepository.save(existingEvent);
    }

    @Override
    @Transactional
    public void deleteEventForOrganizer(UUID organizerId, UUID eventId) {
        getEventForOrganizer(organizerId,eventId).ifPresent(eventRepository::delete);

    }

    @Override
    public Page<Event> listPublishedEvents(Pageable pageable) {
       return eventRepository.findByStatus(EventStatusEnum.PUBLISHED,pageable);
    }

    @Override
    public Page<Event> searchPublishedEvents(String query, Pageable pageable) {
       return eventRepository.searchEvents(query,pageable);
    }
}
