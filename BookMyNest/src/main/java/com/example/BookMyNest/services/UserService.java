package com.example.BookMyNest.services;

import com.example.BookMyNest.dtos.LoginRequest;
import com.example.BookMyNest.dtos.RegistrationRequest;
import com.example.BookMyNest.dtos.Response;
import com.example.BookMyNest.dtos.UserDTO;
import com.example.BookMyNest.entities.User;

public interface UserService {
	
	Response registerUser(RegistrationRequest registrationRequest);
	Response loginUser(LoginRequest loginRequest);
	Response getAllUsers();
	Response getOwnAccountDetails();
	User getCurrentLoggedInUser();
	Response updateOwnAccount(UserDTO userDTO);
	Response deleteOwnAccount();
	Response getMyBookingHistory();

}
