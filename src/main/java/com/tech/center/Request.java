package com.tech.center;


public class Request {
    private Long amount;

    private String email;

    private String productName;
    
	public Request() {
		super();
	}
	
	public Request(Long amount, String email, String productName) {
		super();
		this.amount = amount;
		this.email = email;
		this.productName = productName;
	}


	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}
