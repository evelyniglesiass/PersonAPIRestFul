package br.com.api.spring.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtConfigurer extends
		SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	@Autowired
	private JwtTokenProvider tokenProvider; // obj da nossa classe que gera o token

	public JwtConfigurer(JwtTokenProvider tokenProvider) { // construtor
		this.tokenProvider = tokenProvider;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		JwtTokenFilter customFilter = new JwtTokenFilter(tokenProvider); // instanciando um jwtTokenFilter
		http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class); // configurando adicionando o filtro
	}
}
