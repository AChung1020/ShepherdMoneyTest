package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.vo.request.CreateUserPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class UserController {

    private UserRepository userRepository;

    // TODO: Create an user entity with information given in the payload, store it in the database
    //       and return the id of the user in 200 OK response
    @PutMapping("/user")
    public ResponseEntity<Integer> createUser(@RequestBody CreateUserPayload payload) {

        User user = new User();

        user.setName(payload.getName());
        user.setEmail(payload.getEmail());
        user.setDOB(payload.getDOB());
        user.setCreditCards(new ArrayList<>());

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(savedUser.getId());
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestParam int userId) {
        // TODO: Return 200 OK if a user with the given ID exists, and the deletion is successful
        //       Return 400 Bad Request if a user with the ID does not exist
        //       The response body could be anything you consider appropriate

        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return ResponseEntity.ok("User deleted successfully.");
        } else {
            return ResponseEntity.badRequest().body("User with ID " + userId + " does not exist.");
        }
    }
}
