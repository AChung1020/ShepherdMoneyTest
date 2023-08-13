package com.shepherdmoney.interviewproject.model;

import java.time.Instant;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class BalanceHistory {

    // -------------------------------------------- Instance Variables -------------------------------------------------

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;

    private double balance;

    @ManyToOne
    private CreditCard creditCard; // Many-to-one relationship with CreditCard

    // -------------------------------------------- Constructor --------------------------------------------------------

    public BalanceHistory(LocalDate date, double balance) {
        this.balance = balance;
        this.date = date;
    }

    // -------------------------------------------- Getters and Setters ------------------------------------------------
    public LocalDate getDate() {
        return this.date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getBalance() {
        return this.balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
