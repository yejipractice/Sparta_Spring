package com.sparta.week04.repository;

import com.sparta.week04.models.User;
import com.sparta.week04.models.UserTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTimeRepository extends JpaRepository<UserTime, Long> {
    UserTime findByUser(User user);
}