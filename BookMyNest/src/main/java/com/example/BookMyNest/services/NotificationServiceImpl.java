package com.example.BookMyNest.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.BookMyNest.dtos.NotificationDTO;
import com.example.BookMyNest.entities.Notification;
import com.example.BookMyNest.enums.NotificationType;
import com.example.BookMyNest.repositories.NotificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
	
	private final JavaMailSender javaMailSender;
	private final NotificationRepository notificationRepository;
	
	@Override
	@Async
	public void sendEmail(NotificationDTO notificationDTO) {
		log.info("Sending Email");
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(notificationDTO.getRecipient());
		simpleMailMessage.setSubject(notificationDTO.getSubject());
		simpleMailMessage.setText(notificationDTO.getBody());
		
		javaMailSender.send(simpleMailMessage);
		
		//Save to database
		Notification notificationToSave = Notification.builder()
				.recipient(notificationDTO.getRecipient())
				.subject(notificationDTO.getSubject())
				.body(notificationDTO.getBody())
				.bookingReference(notificationDTO.getBookingReference())
				.type(NotificationType.EMAIL)
				.build();
		
		notificationRepository.save(notificationToSave);
		
	}
	@Override
	public void sendSms() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sendWhatsapp() {
		// TODO Auto-generated method stub
		
	}
	
	

}
