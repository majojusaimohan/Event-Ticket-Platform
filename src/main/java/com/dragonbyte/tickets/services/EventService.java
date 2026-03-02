package com.dragonbyte.tickets.services;

import com.dragonbyte.tickets.domain.CreateEventRequest;
import com.dragonbyte.tickets.domain.UpdateEventRequest;
import com.dragonbyte.tickets.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface EventService {

    Event createEvent(UUID organizerId, CreateEventRequest event);
    Page<Event>  listEventsForOrganizer(UUID organizerId, Pageable pageable);
    Optional<Event> getEventForOrganizer(UUID organizerId, UUID id);
    Event updateEventForOrganizer(UUID organizerId, UUID id, UpdateEventRequest updateEventRequest);
    void deleteEventForOrganizer(UUID organizerId, UUID eventId);
    Page<Event> listPublishedEvents(Pageable pageable);
    Page<Event> searchPublishedEvents( String query, Pageable pageable);
    Optional<Event> getPublishedEventId(UUID eventId);
}
