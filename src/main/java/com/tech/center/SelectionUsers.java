package com.tech.center;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "selectionusers")
public class SelectionUsers {
	@Id
	@Column(name = "selection_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long selectionId;
	
	private long orderId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	
	public SelectionUsers() {}
	
	public SelectionUsers(Long orderId, User user) {
		this.orderId = orderId;
		this.user = user;
	}

	
	public long getSelectionId() {
		return selectionId;
	}

	public void setSelectionId(long selectionId) {
		this.selectionId = selectionId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Long getUserId() {
		return user.getUserId();
	}

	public void setUserId(Long userId) {
		this.user.setUserId(userId);
	}

	
	
	
}
