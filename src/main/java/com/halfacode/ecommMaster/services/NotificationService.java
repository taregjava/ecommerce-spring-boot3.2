package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void notifyAdmin(String message) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();

            boolean isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                sendEmailToAdmin(message, email);
                sendSMS(message);
                logToAdminDashboard(message);
            } else {
                System.out.println("User is not an admin.");
            }
        } else {
            System.out.println("User is not authenticated or principal is not of type CustomUserDetails.");
        }
    }

    private void sendEmailToAdmin(String message, String adminEmail) {
        System.out.println("Sending email to admin: " + message + " to " + adminEmail);
    //    emailService.sendEmail(adminEmail, "Low Stock Alert", message);
    }

    private void sendSMS(String message) {
        System.out.println("Sending SMS to admin: " + message);
     //   smsService.sendSms("+123456789", message);
    }

    private void logToAdminDashboard(String message) {
        System.out.println("Logging message to admin dashboard: " + message);
       // adminDashboardService.logMessage(message);
    }
}
