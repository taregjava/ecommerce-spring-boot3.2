package com.halfacode.ecommMaster.repositories;

import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.models.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {
    List<UserActivity> findByUserAndAction(User user, String view);
}
