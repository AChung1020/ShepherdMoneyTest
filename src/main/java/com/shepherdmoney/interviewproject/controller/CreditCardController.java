package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.BalanceHistory;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.AddCreditCardToUserPayload;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class CreditCardController {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/credit-card")
    public ResponseEntity<Integer> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {
        // TODO: Create a credit card entity, and then associate that credit card with user with given userId
        //       Return 200 OK with the credit card id if the user exists and credit card is successfully associated with the user
        //       Return other appropriate response code for other exception cases
        //       Do not worry about validating the card number, assume card number could be any arbitrary format and length
        int userID = payload.getUserId();

        // Check if the user exists
        if (!userRepository.existsById(userID)) {
            return ResponseEntity.notFound().build();
        }

        //get user by userID
        User user = userRepository.getReferenceById(userID);

        // Create and save the credit card
        CreditCard card = new CreditCard(payload.getCardIssuanceBank(), payload.getCardNumber(), user);
        CreditCard savedCard = creditCardRepository.save(card);

        //add credit card to User list of credit cards
        List<CreditCard> list = user.getCreditCards();
        list.add(savedCard);
        user.setCreditCards(list);
        userRepository.save(user);

        return ResponseEntity.ok(savedCard.getId());

        //prob need mroe exceptions
    }


    @GetMapping("/credit-card:all")
    public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@RequestParam int userId) {
        // TODO: return a list of all credit card associated with the given userId, using CreditCardView class
        //       if the user has no credit card, return empty list, never return null

        //try to see if id exist, then executes
        try {
            //get user from userRepository
            User user = userRepository.getReferenceById(userId);

            //check if the list is empty or not, return empty list if true
            if (user.getCreditCards() == null || user.getCreditCards().isEmpty()) {
                return ResponseEntity.ok(new ArrayList<>());
            }

            //creates new list and adds existing cards with the variables specified in CreditCardView
            List<CreditCardView> cardViews = new ArrayList<>();

            for (CreditCard creditCard : user.getCreditCards()) {
                CreditCardView cardView = CreditCardView.builder()
                        .issuanceBank(creditCard.getIssuanceBank())
                        .number(creditCard.getCardNumber())
                        .build();
                cardViews.add(cardView);
            }

            return ResponseEntity.ok(cardViews);

        } catch (EntityNotFoundException ex) {
            // Handle the case when the user entity is not found
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/credit-card:user-id")
    public ResponseEntity<Integer> getUserIdForCreditCard(@RequestParam String creditCardNumber) {
        // TODO: Given a credit card number, efficiently find whether there is a user associated with the credit card
        //       If so, return the user id in a 200 OK response. If no such user exists, return 400 Bad Request

        CreditCard creditCard = creditCardRepository.findByCardNumber(creditCardNumber);

        //checks if creditCardNumber exists, responds accordingly
        if(creditCard == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(creditCard.getUser().getId());
        }
    }

    @PostMapping("/credit-card:update-balance")
    public ResponseEntity<String> updateBalance(@RequestBody UpdateBalancePayload[] payload) {
        //TODO: Given a list of transactions, update credit cards' balance history.
        //      For example: if today is 4/12, a credit card's balanceHistory is [{date: 4/12, balance: 110}, {date: 4/10, balance: 100}],
        //      Given a transaction of {date: 4/10, amount: 10}, the new balanceHistory is
        //      [{date: 4/12, balance: 120}, {date: 4/11, balance: 110}, {date: 4/10, balance: 110}]
        //      Return 200 OK if update is done and successful, 400 Bad Request if the given card number
        //        is not associated with a card.

        for (UpdateBalancePayload transaction : payload) {
            String creditCardNumber = transaction.getCreditCardNumber();
            LocalDate date = transaction.getTransactionTime();
            double amount = transaction.getTransactionAmount();

            // Find the credit card by card number
            CreditCard creditCard = creditCardRepository.findByCardNumber(creditCardNumber);

            if(creditCard != null) {
                List<BalanceHistory> localBalanceHistory = creditCard.getBalanceHistory();
                BalanceHistory historyToUpdate = null;

                for (BalanceHistory history : localBalanceHistory) {
                    if (history.getDate().equals(date)) {
                        historyToUpdate = history;
                        creditCard.getBalanceHistory().remove(historyToUpdate);
                        break;
                    }
                }

                if (historyToUpdate != null) {
                    // Update the balance in the history
                    double newBalance = historyToUpdate.getBalance() + amount;
                    historyToUpdate.setBalance(newBalance);
                    localBalanceHistory.add(historyToUpdate);
                } else {
                    // Create a new balance history if not found
                    BalanceHistory newHistory = new BalanceHistory(date, amount);
                    newHistory.setCreditCard(creditCard);
                    localBalanceHistory.add(newHistory);
                }

                // Sort the balance history list by date
                Collections.sort(localBalanceHistory, Comparator.comparing(BalanceHistory::getDate));

                creditCard.setBalanceHistory(localBalanceHistory);
                creditCardRepository.save(creditCard);
            } else {
                return ResponseEntity.badRequest().body("This card number does not exist!");
            }
        }


        return ResponseEntity.ok("Balance history updated successfully.");
    }
    
}
