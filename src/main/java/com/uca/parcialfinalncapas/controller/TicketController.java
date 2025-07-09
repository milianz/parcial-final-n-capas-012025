package com.uca.parcialfinalncapas.controller;

import com.uca.parcialfinalncapas.dto.request.TicketCreateRequest;
import com.uca.parcialfinalncapas.dto.request.TicketUpdateRequest;
import com.uca.parcialfinalncapas.dto.response.GeneralResponse;
import com.uca.parcialfinalncapas.dto.response.TicketResponse;
import com.uca.parcialfinalncapas.dto.response.TicketResponseList;
import com.uca.parcialfinalncapas.exceptions.BadTicketRequestException;
import com.uca.parcialfinalncapas.security.CustomUserDetails;
import com.uca.parcialfinalncapas.service.TicketService;
import com.uca.parcialfinalncapas.utils.ResponseBuilderUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@AllArgsConstructor
public class TicketController {
    private TicketService ticketService;

    @GetMapping
    @PreAuthorize("hasRole('TECH')")
    public ResponseEntity<GeneralResponse> getAllTickets() {
        List<TicketResponseList> tickets = ticketService.getAllTickets();
        try {
            return ResponseBuilderUtil.buildResponse("Tickets obtenidos correctamente",
                    tickets.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK,
                    tickets);
        } catch (BadTicketRequestException e) {
            return ResponseBuilderUtil.buildResponse(
                    "Error al obtener los tickets: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('TECH') or hasRole('USER')")
    public ResponseEntity<GeneralResponse> getTicketById(@PathVariable Long id, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Si es USER, verificar que sea propietario del ticket
        if (userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
            if (!ticketService.isTicketOwner(id, userDetails.getUsername())) {
                return ResponseBuilderUtil.buildResponse(
                        "No tienes permisos para ver este ticket",
                        HttpStatus.FORBIDDEN,
                        null
                );
            }
        }

        TicketResponse ticket = ticketService.getTicketById(id);
        return ResponseBuilderUtil.buildResponse("Ticket encontrado", HttpStatus.OK, ticket);
    }

    @GetMapping("/my-tickets")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GeneralResponse> getMyTickets(Authentication authentication) {
        String userEmail = authentication.getName();
        List<TicketResponseList> tickets = ticketService.getTicketsByUser(userEmail);
        try {
            return ResponseBuilderUtil.buildResponse("Mis tickets obtenidos correctamente",
                    tickets.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK,
                    tickets);
        } catch (BadTicketRequestException e) {
            return ResponseBuilderUtil.buildResponse(
                    "Error al obtener mis tickets: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GeneralResponse> createTicket(@Valid @RequestBody TicketCreateRequest ticket,
                                                        Authentication authentication) {
        // Asegurar que el usuario solo puede crear tickets para sí mismo
        String userEmail = authentication.getName();
        ticket.setCorreoUsuario(userEmail);

        TicketResponse createdTicket = ticketService.createTicket(ticket);
        try {
            return ResponseBuilderUtil.buildResponse("Ticket creado correctamente", HttpStatus.CREATED, createdTicket);
        } catch (BadTicketRequestException e) {
            return ResponseBuilderUtil.buildResponse(
                    "Error al crear el ticket: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('TECH')")
    public ResponseEntity<GeneralResponse> updateTicket(@Valid @RequestBody TicketUpdateRequest ticket) {
        TicketResponse updatedTicket = ticketService.updateTicket(ticket);
        try {
            return ResponseBuilderUtil.buildResponse("Ticket actualizado correctamente", HttpStatus.OK, updatedTicket);
        } catch (BadTicketRequestException e) {
            return ResponseBuilderUtil.buildResponse(
                    "Error al actualizar el ticket: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TECH')")
    public ResponseEntity<GeneralResponse> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseBuilderUtil.buildResponse("Ticket eliminado correctamente", HttpStatus.OK, null);
    }
}