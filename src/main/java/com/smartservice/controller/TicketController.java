package com.smartservice.controller;

import com.smartservice.model.Role;
import com.smartservice.model.Ticket;
import com.smartservice.model.TicketStatus;
import com.smartservice.model.User;
import com.smartservice.repository.TicketRepository;
import com.smartservice.repository.UserRepository;
import com.smartservice.service.TicketService;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(
    origins = "http://localhost:5173",
    allowCredentials = "true"
)
public class TicketController {

    private final TicketService service;
    private final TicketRepository repo;
    private final UserRepository users;

    public TicketController(
            TicketService service,
            TicketRepository repo,
            UserRepository users) {

        this.service = service;
        this.repo = repo;
        this.users = users;
    }

    // Get tickets according to logged-in user's role
    @GetMapping
    public List<Ticket> all(
            @RequestParam(required = false) String search,
            HttpSession session) {

        User user = current(session);

        // Customer → only their own tickets
        if (user.getRole() == Role.CUSTOMER) {
            return repo.findByCustomerEmailIgnoreCase(user.getEmail());
        }

        // Employee → only tickets assigned to them
        if (user.getRole() == Role.EMPLOYEE) {
            return repo.findByAssignedEmployeeIgnoreCase(user.getEmail());
        }

        // Admin → all tickets
        return service.all(search);
    }

    // Customer creates a ticket
    @PostMapping
    public Ticket create(
            @RequestBody Ticket ticket,
            HttpSession session) {

        User user = current(session);

        // Only customers can create tickets
        if (user.getRole() != Role.CUSTOMER) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only customers can create tickets"
            );
        }

        ticket.setCustomerName(user.getName());
        ticket.setCustomerEmail(user.getEmail());
        ticket.setAssignedEmployee(null);
        ticket.setStatus(TicketStatus.OPEN);

        return service.create(ticket);
    }

    // Admin assigns ticket to employee
    @PatchMapping("/{id}/assign")
    public Ticket assign(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            HttpSession session) {

        require(session, Role.ADMIN);

        String employeeEmail = body.getOrDefault("employee", "");

        return service.assign(id, employeeEmail);
    }

    // Employee/Admin updates ticket status
    @PatchMapping("/{id}/status")
    public Ticket status(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            HttpSession session) {

        User user = current(session);

        Ticket ticket = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ticket not found"
                ));

        // Customer cannot update ticket status
        if (user.getRole() == Role.CUSTOMER) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Customers cannot update ticket status"
            );
        }

        // Employee can update ONLY their assigned tickets
        if (user.getRole() == Role.EMPLOYEE) {

            if (ticket.getAssignedEmployee() == null ||
                !user.getEmail().equalsIgnoreCase(
                        ticket.getAssignedEmployee())) {

                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "You can update only tickets assigned to you"
                );
            }
        }

        String statusValue = body.get("status");

        if (statusValue == null || statusValue.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status is required"
            );
        }

        TicketStatus newStatus;

        try {
            newStatus = TicketStatus.valueOf(
                    statusValue.toUpperCase()
            );
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid ticket status: " + statusValue
            );
        }

        return service.status(id, newStatus);
    }

    // Get currently logged-in user
    private User current(HttpSession session) {

        Object userId = session.getAttribute("userId");

        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Please login first"
            );
        }

        return users.findById((Long) userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "User not found"
                ));
    }

    // Check whether user has required role
    private void require(
            HttpSession session,
            Role requiredRole) {

        User user = current(session);

        if (user.getRole() != requiredRole) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied"
            );
        }
    }
}