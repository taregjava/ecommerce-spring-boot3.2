package com.halfacode.ecommMaster.dashboard;

import com.halfacode.ecommMaster.models.Order;
import com.halfacode.ecommMaster.models.Product;
import com.halfacode.ecommMaster.models.User;
import com.halfacode.ecommMaster.services.OrderService;
import com.halfacode.ecommMaster.services.ProductService;
import com.halfacode.ecommMaster.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminDashboardService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

  /*  public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public void updateProductStock(Long productId, int newStock) {
        Product product = productService.getProductById(productId);
        product.setStock(newStock);
        productService.saveProduct(product);
    }*/

    public void updateOrderStatus(Long orderId, String status) {
        orderService.updateOrderStatus(orderId, status);
    }
}

