package com.example.BookMyNest.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.BookMyNest.enums.BookingStatus;
import com.example.BookMyNest.enums.NotificationType;
import com.example.BookMyNest.enums.PaymentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "bookings")
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "room_id")
	private Room room;

	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;

	private LocalDate checkInDate;
	private LocalDate checkOutDate;

	private BigDecimal totalPrice;
	private String bookingReference;
	private LocalDateTime createdAt;

	@Enumerated(EnumType.STRING)
	private BookingStatus bookingStatus;

}
