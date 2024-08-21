package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoyaltyProgramService {

    @Autowired
    private UserRepository userRepository;

    public void addLoyaltyPoints(User user, int points) {
       // user.setLoyaltyPoints(user.getLoyaltyPoints() + points);
        userRepository.save(user);
    }

    public int redeemLoyaltyPoints(User user, int points) {
        /*if (user.getLoyaltyPoints() >= points) {
            user.setLoyaltyPoints(user.getLoyaltyPoints() - points);*/
        userRepository.save(user);
        return points;

       // Not enough points to redeem
    }
}
