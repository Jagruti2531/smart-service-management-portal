package com.smartservice.model;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) Long id;
 String name;
 @Column(nullable=false,unique=true) String email;
 String password;
 @Enumerated(EnumType.STRING) Role role;
}