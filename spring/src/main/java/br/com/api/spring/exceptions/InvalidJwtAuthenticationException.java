package br.com.api.spring.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN) // forbidden = proibido acesso
public class InvalidJwtAuthenticationException extends AuthenticationException { // extend da authentication exception
	
	private static final long serialVersionUID = 1L;
	
	public InvalidJwtAuthenticationException(String ex) {
		super(ex);
	}
}