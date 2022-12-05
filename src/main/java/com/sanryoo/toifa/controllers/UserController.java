package com.sanryoo.toifa.controllers;

import com.sanryoo.toifa.modal.Information;
import com.sanryoo.toifa.modal.ResponseObject;
import com.sanryoo.toifa.modal.User;
import com.sanryoo.toifa.repository.InformationRepository;
import com.sanryoo.toifa.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InformationRepository informationRepository;

    @PostMapping("signup")
    ResponseEntity<ResponseObject> signup(@RequestBody User user) {
        List<User> foundUsers = userRepository.findByUsername(user.getUsername().trim());
        if (foundUsers.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", "Username already exist", new User())
            );
        } else {
            informationRepository.save(new Information());
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Insert user successful", userRepository.save(user))
            );
        }
    }

    @PostMapping("login")
    ResponseEntity<ResponseObject> login(@RequestBody User user) {
        List<User> foundUsers = userRepository.findByUsername(user.getUsername().trim());
        if (foundUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("failed", "Wrong username or password", new User())
            );
        } else {
            if (BCrypt.checkpw(user.getPassword().trim(), foundUsers.get(0).getPassword())) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Log in successful", foundUsers.get(0))
                );
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("failed", "Wrong username or password", new User())
                );
            }
        }
    }

    @PutMapping("")
    ResponseEntity<ResponseObject> changePassword(@RequestParam("id") Long id, @RequestParam("oldpassword") String oldPassword, @RequestParam("newpassword") String newPassword) {
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.map(user -> {
                    if (BCrypt.checkpw(oldPassword, user.getPassword())) {
                        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
                        return ResponseEntity.status(HttpStatus.OK).body(
                                new ResponseObject("ok", "Update user successfully", userRepository.save(user))
                        );
                    } else {
                        return ResponseEntity.status(HttpStatus.OK).body(
                                new ResponseObject("failed", "Wrong password", new User())
                        );
                    }
                })
                .orElseGet(() -> {
                    return ResponseEntity.status(HttpStatus.OK).body(
                            new ResponseObject("failed", "Can't find user", new User())
                    );
                });
    }

}
