package com.cal.yughistore.security;

import com.cal.yughistore.model.applicaitonuser.ApplicationUser;
import com.cal.yughistore.repository.user.ApplicationUserRepository;
import com.cal.yughistore.security.exceptions.AuthenticationException;
import com.cal.yughistore.security.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthProvider implements AuthenticationProvider{
	private final PasswordEncoder passwordEncoder;
	private final ApplicationUserRepository applicationUserRepository;

	@Override
	public Authentication authenticate(Authentication authentication) {
		ApplicationUser user = loadUserByEmail(authentication.getPrincipal().toString());
		validateAuthentication(authentication, user);
		return new UsernamePasswordAuthenticationToken(
			user.getEmail(),
			user.getPassword(),
			user.getAuthorities()
		);
	}

	@Override
	public boolean supports(Class<?> authentication){
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

	private ApplicationUser loadUserByEmail(String email) throws UsernameNotFoundException{
		return applicationUserRepository.findApplicationUserByEmail(email)
			.orElseThrow(() -> new UserNotFoundException("Étudiant introuvable avec email " + email));
	}

	private void validateAuthentication(Authentication authentication, ApplicationUser user){
		if(!passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword()))
			throw new AuthenticationException(HttpStatus.FORBIDDEN, "Incorrect username or password");
	}
}
