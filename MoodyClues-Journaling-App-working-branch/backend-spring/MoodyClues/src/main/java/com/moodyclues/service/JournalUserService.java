package com.moodyclues.service;

import com.moodyclues.dto.LoginRequestDto;
import com.moodyclues.dto.RegisterRequestDto;
import com.moodyclues.model.JournalUser;

public interface JournalUserService {

	public JournalUser findJournalUserById (String id);
	
	public JournalUser findJournalUserByEmail(String email);
	
	public boolean loginAttempt(LoginRequestDto request);
	
	public boolean loginAttempt(String email, String passwordInput);
	
	public void registerUser(RegisterRequestDto request);

	// public User updateUser(int userId, UpdateUserRequest request);

	// For Users to delete account
	public void deleteUser(String email, String passwordInput);
	
	
	// Only for dev use
	public void deleteUser(String id);
	
}
