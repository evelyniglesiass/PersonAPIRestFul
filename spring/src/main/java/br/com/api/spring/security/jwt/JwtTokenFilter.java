package br.com.api.spring.security.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

public class JwtTokenFilter extends GenericFilterBean {

	@Autowired
	private JwtTokenProvider tokenProvider; // obj da nossa classe que gera o token
	
	public JwtTokenFilter(JwtTokenProvider tokenProvider) { // construtor
		this.tokenProvider = tokenProvider;
	}

    // esse filtro vai ser executado a cada requisição
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) // método de GenericFilterBean
			throws IOException, ServletException {
		String token = tokenProvider.resolveToken((HttpServletRequest) request); // vai pegar a autorização e retornar o token formatado
		if (token != null && tokenProvider.validateToken(token)) { // se token não for nulo e for válido
			Authentication auth = tokenProvider.getAuthentication(token); // gera o token e autentica
			if (auth != null) { 
				SecurityContextHolder.getContext().setAuthentication(auth); // acessar o contexto e setar a autenticação do usuario
			}
		}
		chain.doFilter(request, response);		
	}
}
