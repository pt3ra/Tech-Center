package com.tech.center;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository repository;
	
		// default output
	public List<Order> findAllOrders(){
		return repository.findAll();
	}
	public List<Order> findIncompleteOrders(){
		return repository.findIncomplete();
	}
	public Optional<Order> findOrderById(long orderId){
		return repository.findById(orderId);
	}
	
	public List<Order> findCurrentUserOrders(Long userId){
		return repository.currentUserOrders(userId);
	}
	
		// default input
	public void saveOrder(User user, Order order) {
		order.setUser(user);
		order.setStatus("Pending");
		order.setSolutionStatus("undone");
		
		repository.save(order);
	}
	
	public void saveOrderDefault(Order order) {
		repository.save(order);
	}
		// sorting
	public List<Order> findOrderWithSorting(String field, String type){
		switch(type.toString()) {
			case "ASC":
				return repository.findAll(Sort.by(Sort.Direction.ASC, field));
			case "DESC":
				return repository.findAll(Sort.by(Sort.Direction.DESC, field));
		}
		return repository.findAll();
	}

		// pagination
	public Page<Order> findOrderWithPagination(int offset, int pageSize){
		Page<Order> orders = repository.findAll(PageRequest.of(offset, pageSize));
		return orders;
	}
		// pagination with sorting
	public Page<Order> findOrderWithPaginatiobAndSorting(int offset, int pageSize, String field, String type){
		Page<Order> orders; 
		
		switch(type.toString()) {
			case "ASC":
				orders = repository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.ASC, field)));
				break;
			case "DESC":
				orders = repository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(Sort.Direction.DESC, field)));
				break;
			default :
				orders = repository.findAll(PageRequest.of(offset, pageSize));
				break;
		}

		return orders;
	}
		// search with keyword
	public List<Order> findOrderByKeyword(String keyword){
		return repository.findByKeyword(keyword);
	}
	
	
}
