package com.vintagecars.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vintagecars.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

}