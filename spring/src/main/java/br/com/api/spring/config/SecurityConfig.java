package br.com.api.spring.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import br.com.api.spring.security.jwt.JwtConfigurer;
import br.com.api.spring.security.jwt.JwtTokenProvider;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Bean
	public PasswordEncoder passwordEncoder() { // vai dizer pro spring como ele vai encriptar as senhas para poder fazer a comparação com o banco
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder()); // cria um encoder e seta o pbkdf2
		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
		passwordEncoder.setDefaultPasswordEncoderForMatches(new Pbkdf2PasswordEncoder());
		return passwordEncoder; 
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception { // é necessário para subir a aplicação
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable() // desabilitar a autenticação basic
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // significa que a nossa sessã não vai armazenar estado
				.and()
					.authorizeRequests() // para que seja necesário autorização a cada request
					.antMatchers( // urls permitidas
							"/auth/signin", // login
							"/auth/refresh", // refreh
							"/api-docs/**", // para acessar as páginas do swagger
							"/swagger-ui.html**" // para acessar as páginas do swagger
						).permitAll()
					.antMatchers("/api/**").authenticated() // para acessar essa url será necessário estar autenticado
					.antMatchers("/users").denyAll() // negar todo mundo para acesso em users / pois dependendo da versão o spring pode expor o users e explanar dados
				.and()
					.cors() // permitir cors
				.and()
				.apply(new JwtConfigurer(tokenProvider));
	}
}

