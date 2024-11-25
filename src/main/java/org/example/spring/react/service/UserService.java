package org.example.spring.react.service;

import org.example.spring.react.config.JWTUtils;
import org.example.spring.react.domain.RegisterRequest;
import org.example.spring.react.entity.Users;
import org.example.spring.react.exception.UserNotFoundException;
import org.example.spring.react.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public UserService(UserRepository userRepository, JWTUtils jwtUtils, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public RegisterRequest register(RegisterRequest registerRequest) {
        RegisterRequest resp = new RegisterRequest();
        try {
            // Check if the email already exists
            Optional<Users> existingUser = userRepository.findByEmail(registerRequest.getEmail());
            if (existingUser.isPresent()) {
                resp.setStatusCode(409); // Conflict status code
                resp.setMessage("Email already exists. Please use a different email.");
                return resp;
            }

            Users ourUser = new Users();
            ourUser.setEmail(registerRequest.getEmail());
            ourUser.setCity(registerRequest.getCity());
            ourUser.setRole(registerRequest.getRole().equalsIgnoreCase("ADMIN") ? "ADMIN" : "USER");
            ourUser.setFirstName(registerRequest.getFirstName());
            ourUser.setLastName(registerRequest.getLastName());
            ourUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

            Users ourUsersResult = userRepository.save(ourUser);

            if (ourUsersResult.getId() > 0) {
                resp.setUsers(ourUsersResult);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }
        } catch (DataIntegrityViolationException e) {
            resp.setStatusCode(409); // Conflict status code
            resp.setMessage("Email already exists. Please use a different email.");
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public RegisterRequest login(RegisterRequest loginRequest) {
        RegisterRequest response = new RegisterRequest();
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                            loginRequest.getPassword()));
            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setUsers(user);
            response.setToken(jwt);
            response.setStatusCode(200);
            response.setRole(user.getRole());
            response.setExpirationTime("24Hrs");
            response.setRefreshToken(refreshToken);
            response.setMessage("Successfully Logged In");

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    public RegisterRequest refreshToken(RegisterRequest refreshTokenReqiest) {
        RegisterRequest response = new RegisterRequest();
        try {
            String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
            Users users = userRepository.findByEmail(ourEmail).orElseThrow();
            if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
                var jwt = jwtUtils.generateToken(users);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenReqiest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
            return response;

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }

    public RegisterRequest getAllUsers() {
        RegisterRequest reqRes = new RegisterRequest();

        try {
            List<Users> result = userRepository.findAll();
            if (!result.isEmpty()) {
                reqRes.setOurUsersList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }


    public RegisterRequest getUsersById(Long id) {
        RegisterRequest reqRes = new RegisterRequest();
        try {
            Users usersById = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            reqRes.setUsers(usersById);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Users with id '" + id + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }


    public RegisterRequest deleteUser(Long userId) {
        RegisterRequest reqRes = new RegisterRequest();
        try {
            Optional<Users> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                userRepository.deleteById(userId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }

    public RegisterRequest updateUser(Long userId, Users updatedUser) {
        RegisterRequest reqRes = new RegisterRequest();
        try {
            Optional<Users> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                Users existingUser = userOptional.get();
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setFirstName(updatedUser.getFirstName());
                existingUser.setLastName(updatedUser.getLastName());
                existingUser.setCity(updatedUser.getCity());
                existingUser.setRole(updatedUser.getRole());

                // Check if password is present in the request
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    // Encode the password and update it
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                Users savedUser = userRepository.save(existingUser);
                reqRes.setUsers(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User updated successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }


    public RegisterRequest getMyInfo(String email) {
        RegisterRequest reqRes = new RegisterRequest();
        try {
            Optional<Users> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setUsers(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }

        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;

    }

    public List<Users> searchUsers(String query) {
        List<Users> result = userRepository.findByFirstNameContainingIgnoreCase(query);
        System.out.println("first result = " + result);
        if (!result.isEmpty()) {
            return result;
        }

        result = userRepository.findByLastNameContainingIgnoreCase(query);
        System.out.println("last result = " + result);
        if (!result.isEmpty()) {
            return result;
        }

        result = userRepository.findByEmailStartingWith(query);
        System.out.println("email result = " + result);
        return result;
    }


    public ResponseEntity<Users> findUser(Long userId) {
        Optional<Users> userOptional = userRepository.findById(userId);
        return userOptional
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }
}
