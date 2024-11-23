package org.example.spring.react.service;

import org.example.spring.react.entity.Login;
import org.example.spring.react.entity.SignUp;
import org.example.spring.react.exception.UserAlreadyPresentException;
import org.example.spring.react.repository.SignUpRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ReactService {


    private final SignUpRepository signUpRepository;

    public ReactService(SignUpRepository signUpRepository) {
        this.signUpRepository = signUpRepository;
    }

    public SignUp signUp(SignUp signUp) {
        SignUp signedUser = signUpRepository.findByEmailEqualsIgnoreCase(signUp.getEmail());
        if (Objects.nonNull(signedUser) && signedUser.getEmail().equalsIgnoreCase(signUp.getEmail())){
            throw new UserAlreadyPresentException("User Already Exists", HttpStatus.CONFLICT.toString(),"User Already Exists");
        }
        return signUpRepository.save(signUp);
    }

    public SignUp login(Login login){
        SignUp signedUser = signUpRepository.findByEmailEqualsIgnoreCase(login.getEmail());
        if (!signedUser.getEmail().equalsIgnoreCase(login.getEmail())){
            throw new UserAlreadyPresentException("User not Exists", HttpStatus.NOT_FOUND.toString(),"User not Exists");
        }
        return signedUser;
    }
}
