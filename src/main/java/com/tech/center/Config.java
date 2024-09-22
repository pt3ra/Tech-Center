package com.tech.center;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;

@Configuration
public class Config {
	@Value("${stripe.api.secretKey}")
	private String secretKey;
	
	@PostConstruct
	public void initSecretKey() {
		Stripe.apiKey = secretKey;
	}
}
