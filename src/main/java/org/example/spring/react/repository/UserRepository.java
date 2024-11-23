package org.example.spring.react.repository;

import org.example.spring.react.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);

    List<Users> findByFirstNameContainingIgnoreCase(String firstName);
    List<Users> findByLastNameContainingIgnoreCase(String lastName);
    List<Users> findByEmailStartingWith(String email);
}
