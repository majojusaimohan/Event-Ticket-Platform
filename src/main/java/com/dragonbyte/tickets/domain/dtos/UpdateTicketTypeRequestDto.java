package com.dragonbyte.tickets.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTicketTypeRequestDto {

    private UUID id;

    @NotBlank(message=" Ticket Type name is required")
    private String name;

    @NotBlank(message="Price is required")
    @PositiveOrZero(message = "Price must be a positive number or zero")
    private Double price;

    private String description;


    private Integer totalAvailable;
}
