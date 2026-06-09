package com.Mike.Proj.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", length = 500)
    private String password;

    @Column(name = "role")
    private String role;

    public User(String firstName, String lastName, String email, String password, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(){
        
    }

    public Integer getId() 
    {    return id; }

    public void setId(Integer id) 
    {    this.id = id;  }

    public String getFirstName()
    {   return firstName;   }

    public void setFirstName(String firstName) 
    {    this.firstName = firstName; }

    public String getLastName() 
    {    return lastName;   }

    public void setLastName(String lastName) 
    {    this.lastName = lastName;  }

    public String getEmail() 
    {    return email;  }

    public void setEmail(String email) 
    {    this.email = email;    }

    public String getPassword() 
    {    return password;   }

    public void setPassword(String password) 
    {    this.password = password;  }


    /**
     * @return String return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

}
