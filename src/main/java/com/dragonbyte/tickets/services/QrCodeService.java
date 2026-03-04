package com.dragonbyte.tickets.services;

import com.dragonbyte.tickets.domain.entities.QrCode;
import com.dragonbyte.tickets.domain.entities.Ticket;

public interface QrCodeService {

    QrCode generateQrCode(Ticket ticket);
}
