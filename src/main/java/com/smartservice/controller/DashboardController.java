package com.smartservice.controller;
import com.smartservice.model.TicketStatus;
import com.smartservice.model.Priority;
import com.smartservice.service.TicketService;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/dashboard")
public class DashboardController {
 private final TicketService s;
 public DashboardController(TicketService s){this.s=s;}
 @GetMapping public Map<String,Long> data(){return Map.of("total",s.total(),"open",s.count(TicketStatus.OPEN),"inProgress",s.count(TicketStatus.IN_PROGRESS),"resolved",s.count(TicketStatus.RESOLVED),"critical",s.countByPriority(Priority.CRITICAL));}
}
