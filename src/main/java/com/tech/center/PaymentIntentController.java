package com.tech.center;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@RestController
public class PaymentIntentController {
	
	@PostMapping("/create-payment-intent")
	public Payment orderCreatePayment(@RequestBody Request request) throws StripeException{
		
		PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
			.setAmount(request.getAmount() * 100L)
			.putMetadata("productName", request.getProductName())
			.setCurrency("usd")
			.setAutomaticPaymentMethods(
					PaymentIntentCreateParams
						.AutomaticPaymentMethods
						.builder()
						.setEnabled(true)
						.build()
			)
			.build();
		
		PaymentIntent intent = PaymentIntent.create(params);
		
		// add checks for current user/ order / set like payment status to done
		
		return new Payment(intent.getId(), intent.getClientSecret());
	}
}
