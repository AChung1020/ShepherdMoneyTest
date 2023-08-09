package com.shepherdmoney.interviewproject.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@RequiredArgsConstructor
public class BalanceHistoryEntry {

    // -------------------------------------------- Instance Variables -------------------------------------------------
    private LocalDate date;
    private double balance;

    // -------------------------------------------- Constructor --------------------------------------------------------\
    public BalanceHistoryEntry(LocalDate date, double balance) {
        this.balance = balance;
        this.date = date;
    }

    // -------------------------------------------- Getters and Setters ------------------------------------------------
    public LocalDate getDate() {
        return this.date;
    }
    public void setDate() {
        this.date = date;
    }

    public double getBalance() {
        return this.balance;
    }
    public void setBalance() {
        this.balance = balance;
    }

}
