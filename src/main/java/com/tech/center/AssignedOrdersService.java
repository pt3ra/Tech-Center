package com.tech.center;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

@Service
public class AssignedOrdersService {
	
	@Autowired
	private AssignedOrdersRepository repository;
		// default output
	public List<AssignedOrders> findAllOrders(){
		return repository.findAll();
	}
	
	public List<AssignedOrders> findCurrentUserAssignedOrders(Long userId){
		return repository.currentUserOrders(userId);
	}
	
	public AssignedOrders findCurrentOrderAssignedOrders(Long orderId){
		return repository.currentOrderOrders(orderId);
	}
	
		// default input
	public void saveAssignedOrder(AssignedOrders assignedOrders) {
		
		repository.save(assignedOrders);
	}
		
}
