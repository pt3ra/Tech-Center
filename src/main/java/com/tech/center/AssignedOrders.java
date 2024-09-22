package com.tech.center;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "assignedorders")
public class AssignedOrders {
	@Id
	@Column(name = "assignement_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long assignementId;
	
	private long orderId, userId;
	
	
	public AssignedOrders() {}
	
	public AssignedOrders(Long orderId, Long userId) {
		this.orderId = orderId;
		this.userId = userId;
	}

	public long getAssignementId() {
		return assignementId;
	}

	public void setAssignementId(long assignementId) {
		this.assignementId = assignementId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
}
