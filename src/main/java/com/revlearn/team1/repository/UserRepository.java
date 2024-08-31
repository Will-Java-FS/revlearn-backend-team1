package com.revlearn.team1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revlearn.team1.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
package com.revlearn.team1.repository;

import com.revlearn.team1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}