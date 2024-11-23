package org.example.spring.react.repository;

import org.example.spring.react.entity.SignUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignUpRepository extends JpaRepository<SignUp,Long> {

    SignUp findByEmailEqualsIgnoreCase(String email);

}
