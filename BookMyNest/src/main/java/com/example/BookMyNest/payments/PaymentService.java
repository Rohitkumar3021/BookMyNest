package com.example.BookMyNest.payments;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.BookMyNest.dtos.NotificationDTO;
import com.example.BookMyNest.entities.Booking;
import com.example.BookMyNest.entities.PaymentEntity;
import com.example.BookMyNest.enums.NotificationType;
import com.example.BookMyNest.enums.PaymentGateWay;
import com.example.BookMyNest.enums.PaymentStatus;
import com.example.BookMyNest.exception.NotFoundException;
import com.example.BookMyNest.payments.DTO.PaymentRequest;
import com.example.BookMyNest.repositories.BookingRepository;
import com.example.BookMyNest.repositories.PaymentRepository;
import com.example.BookMyNest.services.NotificationService;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
	
	private final BookingRepository bookingRepository;
	private final PaymentRepository paymentRepository;
	private final NotificationService notificationService;
	
	@Value("${stripe.api.secret.key}")
	private String secretKey;
	
	public String createPaymentIntent(PaymentRequest paymentRequest) {
		log.info("Inside createPaymentIntent()");
		Stripe.apiKey = secretKey;
		String bookingReference = paymentRequest.getBookingReference();
		
		Booking booking = bookingRepository.findByBookingReference(bookingReference)
				.orElseThrow(()-> new NotFoundException("Booking Not Found"));
		
		if(booking.getPaymentStatus() == PaymentStatus.COMPLETED) {
			throw new NotFoundException("Payment already made for this booking");
		}
		
		try {
			PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
					.setAmount(paymentRequest.getAmount().multiply(BigDecimal.valueOf(100)).longValue())
					.setCurrency("usd")
					.putMetadata("bookingReference", bookingReference)
					.build();
			PaymentIntent intent = PaymentIntent.create(params);
			return intent.getClientSecret();
			
		} catch (Exception e) {
			throw new RuntimeException("Error creating payment intent");
		}
	}
	
	public void updatePaymentBooking(PaymentRequest paymentRequest){

        log.info("Inside updatePaymentBooking()");
        String bookingReference = paymentRequest.getBookingReference();

        Booking booking = bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(()-> new NotFoundException("Booing Not Found"));

        PaymentEntity payment = new PaymentEntity();
        payment.setPaymentGateway(PaymentGateWay.STRIPE);
        payment.setAmount(paymentRequest.getAmount());
        payment.setTranscationId(paymentRequest.getTransactionId());
        payment.setPaymentStatus(paymentRequest.isSuccess() ? PaymentStatus.COMPLETED : PaymentStatus.FAILED);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setBookingReference(bookingReference);
        payment.setUser(booking.getUser());

        if (!paymentRequest.isSuccess()) {
            payment.setFailureReason(paymentRequest.getFailureReason());
        }

        paymentRepository.save(payment); //save payment to database

        //create and send notifiaction
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(booking.getUser().getEmail())
                .type(NotificationType.EMAIL)
                .bookingReference(bookingReference)
                .build();

        log.info("About to send notification inside updatePaymentBooking  by sms");


        if (paymentRequest.isSuccess()){
            booking.setPaymentStatus(PaymentStatus.COMPLETED);
            bookingRepository.save(booking); //Update the booking

            notificationDTO.setSubject("Booking Payment Successful");
            notificationDTO.setBody("Congratulation!! Your payment for booking with reference: " + bookingReference + "is successful");
            notificationService.sendEmail(notificationDTO); //send email

        }else {

            booking.setPaymentStatus(PaymentStatus.FAILED);
            bookingRepository.save(booking); //Update the booking

            notificationDTO.setSubject("Booking Payment Failed");
            notificationDTO.setBody("Your payment for booking with reference: " + bookingReference + "failed with reason: " + paymentRequest.getFailureReason());
            notificationService.sendEmail(notificationDTO); //send email
        }

    }


}
