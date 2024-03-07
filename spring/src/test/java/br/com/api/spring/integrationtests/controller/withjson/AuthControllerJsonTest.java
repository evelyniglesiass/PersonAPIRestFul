package br.com.api.spring.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.api.spring.configs.TestConfigs;
import br.com.api.spring.integrationtests.testcontainer.AbstractIntegrationTest;
import br.com.api.spring.integrationtests.vo.AccountCredentialsVO;
import br.com.api.spring.integrationtests.vo.TokenVO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // definição da porta
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerJsonTest extends AbstractIntegrationTest { 

	private static TokenVO tokenVO;
	
	@Test
	@Order(1)
	public void testSignin() throws JsonMappingException, JsonProcessingException { // o mesmo que fizemos no teste de Authentication na seção passada
		
		AccountCredentialsVO user = 
				new AccountCredentialsVO("leandro", "admin123");
		
		tokenVO = given() // atribuir ao nosso tokenVO
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(user)
					.when()
				.post()
					.then()
						.statusCode(200)
							.extract()
							.body()
								.as(TokenVO.class); // vamos pegar o obj inteiro e não apenas o token
		
		assertNotNull(tokenVO.getAccessToken()); // verificar se o token não está nulo
		assertNotNull(tokenVO.getRefreshToken()); // verificar se o refresh token não está nulo
	}
	
	@Test
	@Order(2)
	public void testRefresh() throws JsonMappingException, JsonProcessingException { // teste de refresh tokn
		
		var newTokenVO = given()
				.basePath("/auth/refresh") // path do refresh
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.pathParam("username", tokenVO.getUsername()) // passar o username como parametro
					.header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken()) // passar header de authorization
				.when()
					.put("{username}") // passar o parametro
				.then()
					.statusCode(200)
				.extract()
					.body()
						.as(TokenVO.class); // extrair o obj por fim
		
		assertNotNull(newTokenVO.getAccessToken()); // verificar se o token não está nulo
		assertNotNull(newTokenVO.getRefreshToken()); // verificar se o refresh token não está nulo
	}
}

