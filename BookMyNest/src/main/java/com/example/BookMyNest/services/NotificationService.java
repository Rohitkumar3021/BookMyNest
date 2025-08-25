package com.example.BookMyNest.services;

import com.example.BookMyNest.dtos.NotificationDTO;

public interface NotificationService {
	void sendEmail(NotificationDTO notificationDTO);
	void sendSms();
	void sendWhatsapp();

}
