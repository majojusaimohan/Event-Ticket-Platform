package com.dragonbyte.tickets.repositories;

import com.dragonbyte.tickets.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    Page<Event> findByOrganizerId(UUID id, Pageable Pageable);
    Optional<Event> findByIdAndOrganizerId(UUID id, UUID organizerId);
}
