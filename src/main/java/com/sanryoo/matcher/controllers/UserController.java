package com.sanryoo.matcher.controllers;

import com.sanryoo.matcher.authentication.AuthenticationService;
import com.sanryoo.matcher.modal.LogInResponse;
import com.sanryoo.matcher.modal.ResponseObject;
import com.sanryoo.matcher.modal.User;
import com.sanryoo.matcher.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;

    private final AuthenticationService authenticationService;

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> getUser(@PathVariable Long id) {
        Optional<User> foundInformation = userRepository.findById(id);
        return foundInformation
                .map(information -> ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(200, "ok", "Find information successful", information)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(404, "failed", "Can't find information with id = " + id, new User())));
    }

    @PostMapping("/signup")
    ResponseEntity<ResponseObject> signup(@RequestBody User user) {
        Optional<User> foundUser = userRepository.findByUsername(user.getUsername().trim());
        return foundUser
                .map(found -> ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(400, "failed", "Username already exist", new User())))
                .orElseGet(() -> {
                    user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
                    logger.info("User: " + user.getUsername() + " signed up account");
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(200, "ok", "Insert user successful", userRepository.save(user)));
                });
    }

    @PostMapping("/login")
    ResponseEntity<ResponseObject> login(@RequestBody User user) {
        if (user.getIdgoogle() != null && !user.getIdgoogle().equals("")) {
            Optional<User> foundUser = userRepository.findByIdgoogle(user.getIdgoogle().trim());
            return foundUser.map(u -> {
                logger.info("User: " + user.getUsername() + " logged in with Google account");
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(200, "ok", "Log in with Google successful", new LogInResponse(u, authenticationService.generateNewToken(u))));
            }).orElseGet(() -> {
                User userSaved = userRepository.save(user);
                logger.info("User: " + user.getUsername() + " signed up with Google account");
                return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(201, "ok", "Sign up with Google successfully", new LogInResponse(userSaved, authenticationService.generateNewToken(userSaved))));
            });
        } else if (user.getIdfacebook() != null && !user.getIdfacebook().equals("")) {
            Optional<User> foundUser = userRepository.findByIdfacebook(user.getIdfacebook().trim());
            return foundUser.map(u -> {
                logger.info("User: " + user.getUsername() + " logged in with Facebook account");
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(200, "ok", "Log in Facebook successful", new LogInResponse(u, authenticationService.generateNewToken(u))));
            }).orElseGet(() -> {
                User userSaved = userRepository.save(user);
                logger.info("User: " + user.getUsername() + " signed up with Facebook account");
                return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseObject(201, "ok", "Sign up with Facebook successfully", new LogInResponse(userSaved, authenticationService.generateNewToken(userSaved))));
            });
        } else {
            Optional<User> foundUser = userRepository.findByUsername(user.getUsername().trim());
            return foundUser.map(u -> {
                if (BCrypt.checkpw(user.getPassword().trim(), u.getPassword())) {
                    logger.info("User: " + user.getUsername() + " logged in with Matcher account");
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(200, "ok", "Log in successful", new LogInResponse(u, authenticationService.generateNewToken(u))));
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(400, "failed", "Wrong username or password", new LogInResponse()));
                }
            }).orElseGet(() -> ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(400, "failed", "Wrong username or password", new LogInResponse())));
        }
    }

    @PutMapping("/change_password")
    ResponseEntity<ResponseObject> changePassword(@RequestParam("id") Long id, @RequestParam("oldpassword") String oldPassword, @RequestParam("newpassword") String newPassword) {
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser
                .map(user -> {
                    if (BCrypt.checkpw(oldPassword, user.getPassword())) {
                        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
                        logger.info("User: " + user.getUsername() + " changed password");
                        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(200, "ok", "Change password successfully", userRepository.save(user)));
                    } else {
                        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(400, "failed", "Wrong password", new User()));
                    }
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(400, "failed", "Can't find user", new User())));
    }

    @PutMapping("")
    ResponseEntity<ResponseObject> updateUser(@RequestBody User user) {
        Optional<User> foundUser = userRepository.findById(user.getId());
        return foundUser
                .map(user1 -> ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(200, "ok", "Updated information successfully", userRepository.save(user))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(400, "failed", "Can't find information with id = " + user.getId(), new User())));
    }

}
