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
@Table(name = "payment")
public class Payment {
	@Id
	@Column(name = "payment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long paymentId;
	
	private String description, intentId, clientSecret;
	
	@OneToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	
	public Payment() {}
	
	public Payment(String description, Order order) {
		this.description = description;
		this.order = order;
	}
	
	public Payment(String intentId, String clientSecret) {
		this.intentId = intentId;
		this.clientSecret = clientSecret;
	}
	
	public Payment(String intentId, String clientSecret, Order order) {
		description = "Order successfully processed with intentId:" + intentId;
		this.intentId = intentId;
		this.clientSecret = clientSecret;
		this.order = order;
	}
	
	public Payment(Order order) {
		this.order = order;
	}

	public long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
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

	public String getIntentId() {
		return intentId;
	}

	public void setIntentId(String intentId) {
		this.intentId = intentId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	
	
	
	
	
}
