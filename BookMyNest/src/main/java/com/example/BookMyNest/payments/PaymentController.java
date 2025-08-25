package com.example.BookMyNest.payments;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BookMyNest.payments.DTO.PaymentRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
	
	private final PaymentService paymentService;
	
	@PostMapping("/pay")
	public ResponseEntity<String> createPaymentintent(@RequestBody PaymentRequest paymentRequest){
		return ResponseEntity.ok(paymentService.createPaymentIntent(paymentRequest));
	}
	
	@PutMapping("/update")
	public void updatePaymentBoking(@RequestBody PaymentRequest paymentRequest) {
		paymentService.updatePaymentBooking(paymentRequest);
	}

}
