package com.shepherdmoney.interviewproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name = "userID")
    private User user;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_card_id")
    private List<BalanceHistory> balanceHistory = new ArrayList<>();

    // -------------------------------------------- Constructor --------------------------------------------------------\

    public CreditCard(String issuanceBank, String cardNumber, User user) {
        this.issuanceBank = issuanceBank;
        this.cardNumber = cardNumber;
        this.user = user;
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
    public void setCardNumber(String cardNumber) {

        this.cardNumber = cardNumber;
    }

    public List<BalanceHistory> getBalanceHistory() {

        return this.balanceHistory;
    }
    public void setBalanceHistory(List<BalanceHistory> balanceHistory) {

        this.balanceHistory = balanceHistory;
    }

    public User getUser() {
        return this.user;
    }
}

