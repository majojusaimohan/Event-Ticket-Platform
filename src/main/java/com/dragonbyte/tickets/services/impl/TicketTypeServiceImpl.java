package com.dragonbyte.tickets.services.impl;

import com.dragonbyte.tickets.domain.entities.Ticket;
import com.dragonbyte.tickets.domain.entities.TicketStatusEnum;
import com.dragonbyte.tickets.domain.entities.TicketType;
import com.dragonbyte.tickets.domain.entities.User;
import com.dragonbyte.tickets.exceptions.TicketSoldOutException;
import com.dragonbyte.tickets.exceptions.UserNotFoundException;
import com.dragonbyte.tickets.repositories.QrCodeRepository;
import com.dragonbyte.tickets.repositories.TicketRepository;
import com.dragonbyte.tickets.repositories.TicketTypeRepository;
import com.dragonbyte.tickets.repositories.UserRepository;
import com.dragonbyte.tickets.services.QrCodeService;
import com.dragonbyte.tickets.services.TicketTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final QrCodeService qrCodeService;


    @Override
    @Transactional
    public Ticket purchaseTicket(UUID userId, UUID ticketTypeId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                String.format("User with id %s not found", userId)
        ));

        TicketType ticketType = ticketTypeRepository.findByIdWithLock(ticketTypeId).orElseThrow(() -> new UserNotFoundException(
                String.format("Ticket type with ID %s not found", ticketTypeId)));

       int purchasedTicket= ticketRepository.countByTicketTypeId(ticketType.getId());

       Double totalAvailable= ticketType.getTotalAvailable();

       if(purchasedTicket+1>=totalAvailable){
           throw new TicketSoldOutException();
       }

       Ticket ticket = new Ticket();

       ticket.setStatus(TicketStatusEnum.PURCHASED);
       ticket.setTicketType(ticketType);
       ticket.setPurchaser(user);

       Ticket savedTicket = ticketRepository.save(ticket);
       qrCodeService.generateQrCode(savedTicket);

       return ticketRepository.save(savedTicket);


    }
}
