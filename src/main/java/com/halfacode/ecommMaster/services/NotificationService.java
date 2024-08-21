package com.halfacode.ecommMaster.services;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void notifyAdmin(String message) {
        // Logic to send notification (email, SMS, etc.) to the admin
        System.out.println("Admin notification: " + message);
    }
}
