package com.tech.center;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "solution")
public class Solution {
	@Id
	@Column(name = "solution_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long solutionId;
	
	private String description;
	
	@OneToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	
	public Solution() {}
	
	public Solution(String description, Order order) {
		this.description = description;
		this.order = order;
	}
	
	public Solution(Order order) {
		this.order = order;
	}

	public long getSolutionId() {
		return solutionId;
	}

	public void setSolutionId(long solutionId) {
		this.solutionId = solutionId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	
	
}
