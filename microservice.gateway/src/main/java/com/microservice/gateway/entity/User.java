package com.microservice.gateway.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;  // Usamos String para el ID en MongoDB

    private String username;
    private String password;

    @JsonIgnore
    @DBRef
    private Set<Authority> authorities = new HashSet<>();

    public User(final String username) {
        this.username = username.toLowerCase();
    }

    public void setAuthorities(final Collection<Authority> authorities) {
        this.authorities = new HashSet<>(authorities);
    }
}
