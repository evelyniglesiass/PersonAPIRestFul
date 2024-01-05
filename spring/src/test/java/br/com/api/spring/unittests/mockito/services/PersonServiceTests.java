package br.com.api.spring.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.List;
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

import br.com.api.spring.exceptions.RequiredObjectIsNullException;
import br.com.api.spring.models.PersonModel;
import br.com.api.spring.repository.PersonRepository;
import br.com.api.spring.unittests.mapper.mocks.MockPerson;
import br.com.api.spring.vo.v1.PersonVO;
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

	@Test
	void testCreate() {
		PersonModel entity = input.mockEntity(1); 
		entity.setId(1L);
		
		PersonModel persisted = entity; 
		persisted.setId(1L);
		
		PersonVO vo = input.mockVO(1); 
		vo.setKey(1L);
		
		lenient().when(repository.save(entity)).thenReturn(persisted); 

		var result = service.create(vo);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
		assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
	}
	
	@Test
	void testCreateWithNullPerson() { // para validar se a exception está ocorrendo como esperado no create
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.create(null); // essa chamada precisa retornar a exception 
		});
		
		String expectedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();
		
		// comparando a mensagem esperada com a atual
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testUpdate() {
		PersonModel entity = input.mockEntity(1); 
		
		PersonModel persisted = entity;
		persisted.setId(1L);
		
		PersonVO vo = input.mockVO(1);
		vo.setKey(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.update(vo);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
		assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
	}
	
	@Test
	void testUpdateWithNullPerson() { // para validar se a exception está ocorrendo como esperado no update
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.update(null);
		});
		
		String expectedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testDelete() {
		PersonModel entity = input.mockEntity(1); 
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		service.delete(1L);
	}
	
	@Test
	void testFindAll() {
		List<PersonModel> list = input.mockEntityList(); // mockar uma lista
		
		when(repository.findAll()).thenReturn(list); // quando o findAll for chamado na service, ele irá retornar a lista (para simular consulta no bd)
		
		var people = service.findAll(); // chamando o findAll do service
		
		assertNotNull(people); // a lista não pode ser nula
		assertEquals(14, people.size()); // a lista tem que ter tamanho igual 14
		
		// verificar dados da pessoa 1
		var personOne = people.get(1);
		
		assertNotNull(personOne);
		assertNotNull(personOne.getKey());
		assertNotNull(personOne.getLinks());
		
		assertTrue(personOne.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
		assertEquals("Addres Test1", personOne.getAddress());
		assertEquals("First Name Test1", personOne.getFirstName());
		assertEquals("Last Name Test1", personOne.getLastName());
		assertEquals("Female", personOne.getGender());
		
		// verificar dados da pessoa 4
		var personFour = people.get(4);
		
		assertNotNull(personFour);
		assertNotNull(personFour.getKey());
		assertNotNull(personFour.getLinks());
		
		assertTrue(personFour.toString().contains("links: [</api/person/v1/4>;rel=\"self\"]"));
		assertEquals("Addres Test4", personFour.getAddress());
		assertEquals("First Name Test4", personFour.getFirstName());
		assertEquals("Last Name Test4", personFour.getLastName());
		assertEquals("Male", personFour.getGender());
		
		// verificar dados da pessoa 7
		var personSeven = people.get(7);
		
		assertNotNull(personSeven);
		assertNotNull(personSeven.getKey());
		assertNotNull(personSeven.getLinks());
		
		assertTrue(personSeven.toString().contains("links: [</api/person/v1/7>;rel=\"self\"]"));
		assertEquals("Addres Test7", personSeven.getAddress());
		assertEquals("First Name Test7", personSeven.getFirstName());
		assertEquals("Last Name Test7", personSeven.getLastName());
		assertEquals("Female", personSeven.getGender());

	}
    
}
