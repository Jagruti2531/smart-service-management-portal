package com.smartservice.service;

import com.smartservice.model.*;
import com.smartservice.repository.TicketRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class TicketService {
    private final TicketRepository repo;
    public TicketService(TicketRepository repo){this.repo=repo;}

    public List<Ticket> all(String search){
        return search==null||search.isBlank()?repo.findAll():repo.findByTitleContainingIgnoreCaseOrTicketNumberContainingIgnoreCase(search,search);
    }
    public Ticket create(Ticket t){
        if(t.getTicketNumber()==null||t.getTicketNumber().isBlank()) t.setTicketNumber("TKT-"+(1000+(int)(Math.random()*9000)));
        return repo.save(t);
    }
    public Ticket status(Long id,TicketStatus s){
        Ticket t=repo.findById(id).orElseThrow(); t.setStatus(s); return repo.save(t);
    }
    public Ticket assign(Long id,String employee){
        Ticket t=repo.findById(id).orElseThrow(); t.setAssignedEmployee(employee);
        if(t.getStatus()==TicketStatus.OPEN && employee!=null && !employee.isBlank()) t.setStatus(TicketStatus.ASSIGNED);
        return repo.save(t);
    }
    public long total(){return repo.count();}
    public long count(TicketStatus s){return repo.countByStatus(s);}
    public long countByPriority(Priority p){return repo.countByPriority(p);}
}
