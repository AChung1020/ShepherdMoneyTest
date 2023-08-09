package com.shepherdmoney.interviewproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

// TODO: Credit card's owner. For detailed hint, please see User class

// TODO: Credit card's balance history. It is a requirement that the dates in the balanceHistory
//       list must be in chronological order, with the most recent date appearing first in the list.
//       Additionally, the first object in the list must have a date value that matches today's date,
//       since it represents the current balance of the credit card. For example:
//       [
//         {date: '2023-04-13', balance: 1500},
//         {date: '2023-04-12', balance: 1200},
//         {date: '2023-04-11', balance: 1000},
//         {date: '2023-04-10', balance: 800}
//       ]

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CreditCard {

    // -------------------------------------------- Instance Variables -------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String issuanceBank;

    private String cardNumber;

    @ManyToOne
    @JoinColumn(name = "user_id") // The foreign key column linking to the User entity
    private User user; // Represents the owner of the credit card

    @ElementCollection
    @CollectionTable(name = "CreditCardBalanceHistory")
    @OrderColumn(name = "history_index")
    private List<BalanceHistoryEntry> balanceHistory;

    // -------------------------------------------- Constructor --------------------------------------------------------\

    public CreditCard(String issuanceBank, String cardNumber, User owner, List<BalanceHistoryEntry> balanceHistory) {
        this.issuanceBank = issuanceBank;
        this.cardNumber = cardNumber;
        this.user = owner;
        this.balanceHistory = balanceHistory;
    }

    // -------------------------------------------- Getters and Setters ------------------------------------------------

    public String getIssuanceBank() {
        return this.issuanceBank;
    }
    public void setIssuanceBank(String issuanceBank) {
        this.issuanceBank = issuanceBank;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }
    public void setCardNumber() {
        this.cardNumber = cardNumber;
    }

    public User getUser() {
        return this.user;
    }
    public void setUser(User suer) {
        this.user = user;
    }

    public List<BalanceHistoryEntry> getBalanceHistory() {
        return this.balanceHistory;
    }
    public void setBalanceHistory(List<BalanceHistoryEntry> balanceHistory) {
        this.balanceHistory = balanceHistory;
    }
}

