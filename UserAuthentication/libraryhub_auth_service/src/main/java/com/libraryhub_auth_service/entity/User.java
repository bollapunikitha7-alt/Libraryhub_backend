package com.libraryhub_auth_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public enum Role {
        ADMIN, LIBRARIAN, STUDENT
    }

   
    public User() {}

    public User(Long id, String username, String password,
                String email, Role role) {
        this.id       = id;
        this.username = username;
        this.password = password;
        this.email    = email;
        this.role     = role;
    }

   
    public Long getId()          { return id; }
    public String getUsername()  { return username; }
    public String getPassword()  { return password; }
    public String getEmail()     { return email; }
    public Role getRole()        { return role; }

    
    public void setId(Long id)              { this.id = id; }
    public void setUsername(String u)       { this.username = u; }
    public void setPassword(String p)       { this.password = p; }
    public void setEmail(String e)          { this.email = e; }
    public void setRole(Role r)             { this.role = r; }

    
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String username;
        private String password;
        private String email;
        private Role role;

        public Builder id(Long id)
            { this.id = id; return this; }
        public Builder username(String username)
            { this.username = username; return this; }
        public Builder password(String password)
            { this.password = password; return this; }
        public Builder email(String email)
            { this.email = email; return this; }
        public Builder role(Role role)
            { this.role = role; return this; }

        public User build() {
            return new User(id, username, password, email, role);
        }
    }
}