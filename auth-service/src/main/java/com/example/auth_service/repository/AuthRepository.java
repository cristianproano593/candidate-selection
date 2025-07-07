package com.example.auth_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.auth_service.model.User;

public interface AuthRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
}