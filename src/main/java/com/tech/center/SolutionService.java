package com.tech.center;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolutionService {
	
	@Autowired
	private SolutionRepository repository;
		// default output
	public List<Solution> findAllSolutions(){
		return repository.findAll();
	}
	
	public Solution findSolutionByOrderId(long orderId){
		return repository.findByOrderId(orderId);
	}
	
	
		// default input
	public void saveSolution(Solution solution) {
		
		repository.save(solution);
	}
		
}
