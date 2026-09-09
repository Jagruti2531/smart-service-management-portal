package com.smartservice.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity @Table(name="tickets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Ticket {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id;
 @Column(nullable=false,unique=true) String ticketNumber;
 @Column(nullable=false) String title;
 @Column(length=3000) String description;
 String category, customerName, customerEmail, assignedEmployee;
 @Enumerated(EnumType.STRING) Priority priority;
 @Enumerated(EnumType.STRING) TicketStatus status;
 LocalDateTime createdAt,updatedAt;
 @PrePersist void create(){createdAt=LocalDateTime.now();updatedAt=createdAt;if(status==null)status=TicketStatus.OPEN;if(priority==null)priority=Priority.MEDIUM;}
 @PreUpdate void update(){updatedAt=LocalDateTime.now();}
}