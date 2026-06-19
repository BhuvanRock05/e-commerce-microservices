package com.ecommerce.user.controller;

import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.model.User;
import com.ecommerce.user.sevices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/api/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {

        UserResponse userResponse = userService.getUser(id);
        if (userResponse != null) {
            return ResponseEntity.ok(userResponse);
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping("/api/users")
    public void createUser(@RequestBody User user) {
        userService.addUser(user);
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        boolean updated = userService.updateUser(id, updatedUser);
        if (updated) {
            return ResponseEntity.ok("Updated");
        }
        return ResponseEntity.notFound().build();
    }

}
