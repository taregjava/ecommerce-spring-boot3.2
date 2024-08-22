package com.halfacode.ecommMaster.services;

import com.halfacode.ecommMaster.dto.ProductDTO;
import com.halfacode.ecommMaster.models.BackOrder;
import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.repositories.BackOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BackOrderService {

    @Autowired
    private BackOrderRepository backOrderRepository;
    @Autowired
    private ProductService productService;
    public BackOrder createBackOrder(Long productId, Integer quantity, User user) {
        ProductDTO product = productService.getProductById(productId);
        BackOrder backOrder = new BackOrder();
        backOrder.setOrderDate(LocalDateTime.now());
       // backOrder.setProduct(product);
        backOrder.setQuantity(quantity);
        backOrder.setUser(user);

        return backOrderRepository.save(backOrder);
    }

    public void fulfillBackOrders(ProductDTO product) {
        List<BackOrder> backOrders = backOrderRepository.findByProduct(product);
        for (BackOrder backOrder : backOrders) {
            if (product.getStockQuantity() >= backOrder.getQuantity()) {
               //product.reduceStock(backOrder.getQuantity());
                // Notify user and mark backorder as fulfilled
                backOrderRepository.delete(backOrder);
            }
        }
        productService.addProduct(product);
    }
}
