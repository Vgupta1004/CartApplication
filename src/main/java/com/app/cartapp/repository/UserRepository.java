package com.app.cartapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.cartapp.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
