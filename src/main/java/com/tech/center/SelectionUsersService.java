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
public class SelectionUsersService {
	
	@Autowired
	private SelectionUsersRepository repository;
	
		// default output
	public List<SelectionUsers> findAllSelections(){
		return repository.findAll();
	}
	
	public List<SelectionUsers> findCurrentOrderSelection(Long orderId){
		return repository.currentOrderSelection(orderId);
	}
	
		// default input
	public void saveAssignedUser(SelectionUsers selectionUsers) {
		repository.save(selectionUsers);
	}
		
		// delete
	public void deleteOrderSelection(Long orderId) {
		repository.deleteAllByOrderId(orderId);
	}
	
}
