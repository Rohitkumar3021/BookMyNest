package com.example.BookMyNest.services;

import com.example.BookMyNest.dtos.BookingDTO;
import com.example.BookMyNest.dtos.Response;

public interface BookingService {
	Response getAllBookings();
	Response createBooking(BookingDTO bookingDTO);
	Response findBookingByReferenceNo(String bookingReference);
	Response updateBooking(BookingDTO bookingDTO);

}
