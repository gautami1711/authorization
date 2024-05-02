package com.gautami.authorization.security;


import com.gautami.authorization.Repository.UserRepository;
import com.gautami.authorization.exception.NotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;
	
	public CustomUserDetailsService(UserRepository userRepository)
	{
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws NotFoundException {
		
		return this.userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User Not Found"));
	}

	
}