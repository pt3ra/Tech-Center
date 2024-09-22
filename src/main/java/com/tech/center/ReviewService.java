package com.tech.center;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepository repository;
		// default output
	public List<Review> findAllReviews(){
		return repository.findAll();
	}
	
	public Review findReviewByOrderId(long orderId){
		return repository.findByOrderId(orderId);
	}
	
		// default input
	public void saveReview(Review review) {
		
		repository.save(review);
	}
		
}
