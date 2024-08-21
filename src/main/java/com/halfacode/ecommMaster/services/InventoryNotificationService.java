package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.models.InventoryNotification;
import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.repositories.InventoryNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
public class InventoryNotificationService {

   /* @Autowired
    private InventoryNotificationRepository notificationRepository;
*/
 /*   @Autowired
    private EmailService emailService;
*/
   /* public void notifyUsers(Product product) {
        List<InventoryNotification> notifications = notificationRepository.findByProductAndNotifiedFalse(product);
        for (InventoryNotification notification : notifications) {
            User user = notification.getUser();
            emailService.sendEmail(user.getEmail(), "Product Back in Stock", "The product " + product.getName() + " is now back in stock.");
            notification.markAsNotified();
            notificationRepository.save(notification);
        }
    }*/
}
