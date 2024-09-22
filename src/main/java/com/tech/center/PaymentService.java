package com.tech.center;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
	
	@Autowired
	private PaymentRepository repository;
		// default output
	public List<Payment> findAllPayments(){
		return repository.findAll();
	}
	
	public Payment findPaymentByOrderId(long orderId){
		return repository.findByOrderId(orderId);
	}
	
	
		// default input
	public void savePayment(Payment payment) {
		
		repository.save(payment);
	}
		
}
