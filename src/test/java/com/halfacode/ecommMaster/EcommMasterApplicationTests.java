package com.halfacode.ecommMaster;

import com.halfacode.ecommMaster.models.CartItem;
import com.halfacode.ecommMaster.models.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EcommMasterApplicationTests {

	@Test
	void contextLoads() {
	}
	@Test
	public void testOrderManagement() {
		Order order = new Order();
		CartItem item1 = new CartItem();
		CartItem item2 = new CartItem();

		order.addItem(item1);
		order.addItem(item2);

		assertEquals(2, order.getItems().size());

		order.removeItem(item1);
		assertEquals(1, order.getItems().size());
	}

}
