package com.revlearn.team1.controller;

import com.revlearn.team1.dto.UserDTO;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.UserService;
import com.revlearn.team1.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService userService;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public @ResponseBody ResponseEntity<Map<String, Object>> login(@RequestBody UserDTO user) {
        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        if (!(userDetails instanceof User)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid username or password."));
        }

        User u = (User) userDetails;
        String encodedpassword = u.getPassword();
        if (!passwordEncoder.matches(user.getPassword(), encodedpassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid username or password."));
        }
        String token = jwtUtil.generateToken(u);
        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", "Bearer " + token);
        response.put("tokenType", "Bearer");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public @ResponseBody ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            if (userService.checkExisting(user.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
            }
            User u = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(u);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("\"An error occurred while registering the user: " + e.getMessage());
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteUserResponse> deleteUserById(@PathVariable Long id) {
        User user = userService.findById(id).orElseThrow(() -> new RuntimeException("Could not find user with ID: " + id));
        userService.deleteById(id);
        DeleteUserResponse response = new DeleteUserResponse(user, "User deleted successfully");
        return ResponseEntity.ok(response);
    }
}