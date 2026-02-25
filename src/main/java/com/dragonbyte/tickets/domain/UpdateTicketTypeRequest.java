package com.dragonbyte.tickets.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTicketTypeRequest {

    private UUID id;
    private String name;
    private Double price;
    private String description;
    private Double totalAvailable;

}
