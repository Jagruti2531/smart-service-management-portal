package com.smartservice.controller;

import com.smartservice.model.Role;
import com.smartservice.model.User;
import com.smartservice.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins="http://localhost:5173", allowCredentials="true")
public class UserController {
    private final UserRepository users;
    public UserController(UserRepository users) { this.users = users; }

    @GetMapping
    public List<Map<String,Object>> all(@RequestParam(required=false) Role role, HttpSession session) {
        requireAdmin(session);
        List<User> list = role == null ? users.findAll() : users.findByRole(role);
        return list.stream().map(this::view).toList();
    }

    @PostMapping("/employees")
    public Map<String,Object> createEmployee(@RequestBody Map<String,String> body, HttpSession session) {
        requireAdmin(session);
        String name = body.getOrDefault("name", "").trim();
        String email = body.getOrDefault("email", "").trim().toLowerCase();
        if (name.isBlank() || email.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name and email are required");
        if (users.existsByEmailIgnoreCase(email)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        User employee = User.builder().name(name).email(email).role(Role.EMPLOYEE).password(UUID.randomUUID().toString()).build();
        return view(users.save(employee));
    }

    private Map<String,Object> view(User u) { return Map.of("id",u.getId(),"name",u.getName(),"email",u.getEmail(),"role",u.getRole().name()); }
    private void requireAdmin(HttpSession s) { if (!"ADMIN".equals(s.getAttribute("userRole"))) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access required"); }
}
