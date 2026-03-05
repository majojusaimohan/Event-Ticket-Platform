package com.dragonbyte.tickets.repositories;

import com.dragonbyte.tickets.domain.entities.QrCode;
import com.dragonbyte.tickets.domain.entities.QrCodeStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, UUID> {

    Optional<QrCode> findByTicketIdANdTicketPurchaseId(UUID ticketId, UUID ticketPurchaserId );
    Optional<QrCode> findByIdAndStatus(UUID id, QrCodeStatusEnum status);
}
