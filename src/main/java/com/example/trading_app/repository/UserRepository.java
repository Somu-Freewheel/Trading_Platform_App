package com.example.trading_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.trading_app.Entity.User;

public interface UserRepository extends JpaRepository<User,Long>{

}
