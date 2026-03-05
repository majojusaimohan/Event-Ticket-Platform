package com.dragonbyte.tickets.services;

import com.dragonbyte.tickets.domain.entities.QrCode;
import com.dragonbyte.tickets.domain.entities.Ticket;

import java.util.UUID;

public interface QrCodeService {

    QrCode generateQrCode(Ticket ticket);

    byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId );
}
