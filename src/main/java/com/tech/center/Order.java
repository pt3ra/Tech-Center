package com.tech.center;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@Column(name = "order_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long orderId;
	
	private String orderName;
	private int budget;
	private Date duration;
	private String category;
	private String details, status, solutionStatus;
	
	@OneToOne
	@JoinColumn(name = "solution_id")
	private Solution solution;
	
	@OneToOne
	@JoinColumn(name = "payment_id")
	private Payment payment;
	
	@OneToOne
	@JoinColumn(name = "review_id")
	private Review review;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	
	public Order() {}
	
	public Order(String orderName, int budget, String category, String details) {
		this.orderName = orderName;
		this.budget = budget;
		this.category = category;
		this.details = details;
	}
	
	public Order(String orderName, int budget, Date duration , String category, String details) {
		this.orderName = orderName;
		this.budget = budget;
		this.duration = duration;
		this.category = category;
		this.details = details;
	}
	
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public Long getUserId() {
		return user.getUserId();
	}

	public void setUserId(Long userId) {
		this.user.setUserId(userId);
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public int getBudget() {
		return budget;
	}

	public void setBudget(int budget) {
		this.budget = budget;
	}

	public Date getDuration() {
		return duration;
	}

	public void setDuration(Date duration) {
		this.duration = duration;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSolutionStatus() {
		return solutionStatus;
	}

	public void setSolutionStatus(String solutionStatus) {
		this.solutionStatus = solutionStatus;
	}
	
	public Solution getSolution() {
		return solution;
	}

	public void setSolution(Solution solution) {
		this.solution = solution;
	}
	
	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	@Override
	public String toString() {
		return orderName + "  -  " + budget + "$" + " (" + category + ")";
	}
}
