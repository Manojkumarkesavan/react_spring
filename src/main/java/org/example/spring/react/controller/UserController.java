package org.example.spring.react.controller;

import org.example.spring.react.domain.RegisterRequest;
import org.example.spring.react.entity.Users;
import org.example.spring.react.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<RegisterRequest> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.register(registerRequest));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<RegisterRequest> login(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(userService.login(req));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<RegisterRequest> refreshToken(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(userService.refreshToken(req));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<RegisterRequest> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());

    }

    @GetMapping("/admin/get-users/{userId}")
    public ResponseEntity<RegisterRequest> getUSerByID(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.getUsersById(userId));

    }

    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<RegisterRequest> updateUser(@PathVariable Integer userId, @RequestBody Users users) {
        return ResponseEntity.ok(userService.updateUser(userId, users));
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<RegisterRequest> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        RegisterRequest response = userService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<RegisterRequest> deleteUSer(@PathVariable Integer userId) {
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    @GetMapping("/users/search")
    public List<Users> searchUsers(@RequestParam("query") String query) {
        return userService.searchUsers(query);
    }

}
