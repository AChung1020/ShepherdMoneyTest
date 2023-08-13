package com.shepherdmoney.interviewproject.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// TODO: User's credit card
// HINT: A user can have one or more, or none at all. We want to be able to query credit cards by user
//       and user by a credit card.

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "MyUser")
public class User {

    // -------------------------------------------- Instance Variables -------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private String email;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate DOB;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CreditCard> creditCards = new ArrayList<>();

    // -------------------------------------------- Constructor --------------------------------------------------------
    public User(String name, String email, LocalDate DOB) {
        this.name = name;
        this.email = email;
        this.DOB = DOB;
    }

    // -------------------------------------------- Getters and Setters ------------------------------------------------

    public String getName() {
        return name;
    }
    public  void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDOB() {
        return DOB;
    }
    public void setDOB(LocalDate DOB) {
        this.DOB = DOB;
    }

    public List<CreditCard> getCreditCards() {
        return this.creditCards;
    }
    public void setCreditCards(List<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }


}




