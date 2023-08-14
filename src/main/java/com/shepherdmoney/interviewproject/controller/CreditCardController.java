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
import org.springframework.http.HttpStatus;
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
        try {
            //check if payload has card information and issuance bank
            if (payload == null || payload.getCardIssuanceBank() == null || payload.getCardNumber() == null) {
                return ResponseEntity.badRequest().build();
            }

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
        } catch (Exception e) {
                // Handle database, validation, or other exceptions
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
    }


    @GetMapping("/credit-card:all")
    public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@RequestParam int userId) {
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

                // Sort the balance history list by date, from recent to least recent
                Collections.sort(localBalanceHistory, Comparator.comparing(BalanceHistory::getDate));
                Collections.reverse(localBalanceHistory);

                //save updated balance history to creditCardRepository
                creditCard.setBalanceHistory(localBalanceHistory);
                creditCardRepository.save(creditCard);
            } else {
                return ResponseEntity.badRequest().body("This card number does not exist!");
            }
        }

        return ResponseEntity.ok("Balance history updated successfully.");
    }
    
}
