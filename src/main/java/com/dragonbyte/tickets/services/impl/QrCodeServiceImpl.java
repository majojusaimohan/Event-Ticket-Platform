package com.dragonbyte.tickets.services.impl;

import com.dragonbyte.tickets.domain.entities.QrCode;
import com.dragonbyte.tickets.domain.entities.QrCodeStatusEnum;
import com.dragonbyte.tickets.domain.entities.Ticket;
import com.dragonbyte.tickets.exceptions.QrCodeGenerationException;
import com.dragonbyte.tickets.exceptions.QrCodeNotFoundException;
import com.dragonbyte.tickets.repositories.QrCodeRepository;
import com.dragonbyte.tickets.services.QrCodeService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class QrCodeServiceImpl implements QrCodeService {

    private static final int QR_HEIGHT=300;
    private static final int QR_WIDTH=300;

    private final QRCodeWriter qeCodeWriter;
    private final QrCodeRepository qrCodeRepository;


    @Override
    public QrCode generateQrCode(Ticket ticket) {

        try {
            UUID uniqueId = UUID.randomUUID();
            String qrCodeImage = generateQrCodeImage(uniqueId);

            QrCode qQrCode = new QrCode();
            qQrCode.setId(uniqueId);
            qQrCode.setStatus(QrCodeStatusEnum.ACTIVE);
            qQrCode.setValue(qrCodeImage);
            qQrCode.setTicket(ticket);

           return qrCodeRepository.saveAndFlush(qQrCode);

        } catch(WriterException | IOException e){
            throw new QrCodeGenerationException("Failed to generate QR code", e);
        }
    }

    @Override
    public byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId) {
        QrCode qrCode = qrCodeRepository.findByTicketIdANdTicketPurchaseId(ticketId, userId)
                .orElseThrow(QrCodeNotFoundException::new);

        try{
            return Base64.getDecoder().decode(qrCode.getValue());
        }catch (IllegalArgumentException ex){
            log.error("Invalid base 64 QR code for Ticket ID:{}", ticketId,ex);
            throw new QrCodeNotFoundException();
        }


    }

    private String generateQrCodeImage(UUID uniqueId) throws WriterException, IOException {
        BitMatrix bitMatrix = qeCodeWriter.encode(uniqueId.toString(),
                BarcodeFormat.QR_CODE,
                QR_HEIGHT,
                QR_WIDTH);
        BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        try(ByteArrayOutputStream baos= new ByteArrayOutputStream()){
            ImageIO.write(qrCodeImage, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        }
    }
}
