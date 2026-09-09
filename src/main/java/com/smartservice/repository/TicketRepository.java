package com.smartservice.repository;
import com.smartservice.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TicketRepository extends JpaRepository<Ticket,Long>{
 long countByStatus(TicketStatus status);
 long countByPriority(Priority priority);
 List<Ticket> findTop10ByOrderByCreatedAtDesc();
 List<Ticket> findByTitleContainingIgnoreCaseOrTicketNumberContainingIgnoreCase(String title,String number);
 List<Ticket> findByCustomerEmailIgnoreCase(String email);
 List<Ticket> findByAssignedEmployeeIgnoreCase(String email);
}