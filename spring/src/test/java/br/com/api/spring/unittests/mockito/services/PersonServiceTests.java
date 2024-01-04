package br.com.api.spring.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.api.spring.models.PersonModel;
import br.com.api.spring.repository.PersonRepository;
import br.com.api.spring.unittests.mapper.mocks.MockPerson;
import br.com.api.spring.services.PersonService;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class) 
public class PersonServiceTests {

    MockPerson input;

    @InjectMocks 
    private PersonService service;

    @Mock
	PersonRepository repository;
	
	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockPerson(); 
		MockitoAnnotations.openMocks(this); 
	}

	@Test
	void testFindById() {
		PersonModel entity = input.mockEntity(1); 
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity)); 
		
		var result = service.findById(1L); 
		assertNotNull(result); 
		assertNotNull(result.getKey()); 
		assertNotNull(result.getLinks()); 
		
		assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]")); 
        assertEquals("Addres Test1", result.getAddress()); 
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
	}
    
}