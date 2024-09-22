package com.tech.center.order.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import com.tech.center.Order;
import com.tech.center.OrderRepository;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class OrdersRepositoryTest {
	
	@Autowired
	private OrderRepository repository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	private Order testOrder() {
		Order order = new Order("testName", 1000, Date.valueOf("2050-06-10"), "На дому", "Details");
		
		return order;
	}
	
	@Test
	public void testCreateOrder() {
		Order savedOrder = repository.save(testOrder());
		
		Order existOrder = entityManager.find(Order.class, savedOrder.getOrderId());
		
		assertThat(existOrder.getOrderName()).isEqualTo(savedOrder.getOrderName());
		
		repository.delete(savedOrder);
		
	}
	
	@Test
	public void testDurationConstraint() {
		Order testDuration = testOrder();
		testDuration.setDuration(Date.valueOf("2023-06-10"));
		
		Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(testDuration));
		
	} 
	
	@Test
	public void testBudgetConstraint() {
		Order testBudget = testOrder();
		testBudget.setBudget(0);
		
		Assertions.assertThrows(DataIntegrityViolationException.class, () -> repository.save(testBudget));
		
	} 
	
	
}