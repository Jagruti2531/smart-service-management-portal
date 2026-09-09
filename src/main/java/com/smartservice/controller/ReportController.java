package com.smartservice.controller;

import com.smartservice.model.*;
import com.smartservice.repository.TicketRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.*;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins="http://localhost:5173", allowCredentials="true")
public class ReportController {
    private final TicketRepository tickets;
    public ReportController(TicketRepository tickets) { this.tickets = tickets; }

    @GetMapping
    public Map<String,Object> reports(HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access required");
        List<Ticket> all = tickets.findAll();
        Map<String,Long> status = Arrays.stream(TicketStatus.values()).collect(Collectors.toMap(Enum::name, s -> all.stream().filter(t -> t.getStatus()==s).count(), (a,b)->a, LinkedHashMap::new));
        Map<String,Long> priority = Arrays.stream(Priority.values()).collect(Collectors.toMap(Enum::name, p -> all.stream().filter(t -> t.getPriority()==p).count(), (a,b)->a, LinkedHashMap::new));
        Map<String,Long> employees = all.stream().filter(t -> t.getAssignedEmployee()!=null && !t.getAssignedEmployee().isBlank()).collect(Collectors.groupingBy(Ticket::getAssignedEmployee, TreeMap::new, Collectors.counting()));
        long unassigned = all.stream().filter(t -> t.getAssignedEmployee()==null || t.getAssignedEmployee().isBlank()).count();
        LocalDate first = LocalDate.now().withDayOfMonth(1).minusMonths(5);
        List<Map<String,Object>> monthly = new ArrayList<>();
        for(int i=0;i<6;i++){ YearMonth ym=YearMonth.from(first.plusMonths(i)); long count=all.stream().filter(t -> t.getCreatedAt()!=null && YearMonth.from(t.getCreatedAt()).equals(ym)).count(); monthly.add(Map.of("month", ym.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)+" "+ym.getYear(), "count", count)); }
        return Map.of("total",all.size(),"status",status,"priority",priority,"employees",employees,"unassigned",unassigned,"monthly",monthly);
    }
}
