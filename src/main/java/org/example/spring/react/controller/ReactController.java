package org.example.spring.react.controller;

import org.example.spring.react.entity.Login;
import org.example.spring.react.entity.SignUp;
import org.example.spring.react.service.ReactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/app")
public class ReactController {

    public final ReactService reactService;

    public ReactController(ReactService reactService) {
        this.reactService = reactService;
    }


    @PostMapping("/signup")
    public ResponseEntity<SignUp> signup(@RequestBody SignUp signUp){
        System.out.println("signUp = " + signUp);
        return ResponseEntity.ok(reactService.signUp(signUp));
    }

    @PostMapping("/login")
    public ResponseEntity<SignUp> login(@RequestBody Login login){
        return ResponseEntity.ok(reactService.login(login));
    }
}
