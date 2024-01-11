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
import br.com.api.spring.models.BookModel;
import br.com.api.spring.repository.BookRepository;
import br.com.api.spring.services.BookService;
import br.com.api.spring.unittests.mapper.mocks.MockBook;
import br.com.api.spring.vo.v1.BookVO;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class) 
public class BookServiceTests {

    MockBook input;

    @InjectMocks 
    private BookService service;

    @Mock
	BookRepository repository;
	
	@BeforeEach
	void setUpMocks() throws Exception {
		input = new MockBook(); 
		MockitoAnnotations.openMocks(this); 
	}

	@Test
	void testFindById() {
		BookModel entity = input.mockEntity(1); 
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity)); 
		
		var result = service.findById(1L); 
		assertNotNull(result); 
		assertNotNull(result.getKey()); 
		assertNotNull(result.getLinks()); 
		
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]")); 
        assertEquals("Author Test1", result.getAuthor()); 
		assertEquals(1D, result.getPrice());
		assertEquals("Title Test1", result.getTitle());
        assertNotNull(result.getLaunchDate());
	}

	@Test
	void testCreate() {
		BookModel entity = input.mockEntity(1); 
		entity.setId(1L);
		
		BookModel persisted = entity; 
		persisted.setId(1L);
		
		BookVO vo = input.mockVO(1); 
		vo.setKey(1L);
		
		lenient().when(repository.save(entity)).thenReturn(persisted); 

		var result = service.create(vo);
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]")); 
        assertEquals("Author Test1", result.getAuthor()); 
		assertEquals(1D, result.getPrice());
		assertEquals("Title Test1", result.getTitle());
        assertNotNull(result.getLaunchDate());
	}
	
	@Test
	void testCreateWithNullBook() { 
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.create(null); 
		});
		
		String expectedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testUpdate() {
		BookModel entity = input.mockEntity(1); 
		
		BookModel persisted = entity;
		persisted.setId(1L);
		
		BookVO vo = input.mockVO(1);
		vo.setKey(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		when(repository.save(entity)).thenReturn(persisted);
		
		var result = service.update(vo);
		
		assertNotNull(result);
		assertNotNull(result.getKey());
		assertNotNull(result.getLinks());
		
		assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]")); 
        assertEquals("Author Test1", result.getAuthor()); 
		assertEquals(1D, result.getPrice());
		assertEquals("Title Test1", result.getTitle());
        assertNotNull(result.getLaunchDate());
	}
	
	@Test
	void testUpdateWithNullBook() { 
		Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
			service.update(null);
		});
		
		String expectedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testDelete() {
		BookModel entity = input.mockEntity(1); 
		entity.setId(1L);
		
		when(repository.findById(1L)).thenReturn(Optional.of(entity));
		
		service.delete(1L);
	}
	
	@Test
	void testFindAll() {
		List<BookModel> list = input.mockEntityList(); 
		
		when(repository.findAll()).thenReturn(list); 
		
		var book = service.findAll();
		
		assertNotNull(book); 
		assertEquals(14, book.size()); 
		
		var BookOne = book.get(1);
		
		assertNotNull(BookOne);
		assertNotNull(BookOne.getKey());
		assertNotNull(BookOne.getLinks());
		
		assertTrue(BookOne.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]")); 
        assertEquals("Author Test1", BookOne.getAuthor()); 
		assertEquals(1D, BookOne.getPrice());
		assertEquals("Title Test1", BookOne.getTitle());
        assertNotNull(BookOne.getLaunchDate());

		var BookFour = book.get(4);
		
		assertNotNull(BookFour);
		assertNotNull(BookFour.getKey());
		assertNotNull(BookFour.getLinks());
		
		assertTrue(BookFour.toString().contains("links: [</api/book/v1/4>;rel=\"self\"]")); 
        assertEquals("Author Test4", BookFour.getAuthor()); 
		assertEquals(4D, BookFour.getPrice());
		assertEquals("Title Test4", BookFour.getTitle());
        assertNotNull(BookFour.getLaunchDate());

		var BookSeven = book.get(7);
		
		assertNotNull(BookSeven);
		assertNotNull(BookSeven.getKey());
		assertNotNull(BookSeven.getLinks());
		
		assertTrue(BookSeven.toString().contains("links: [</api/book/v1/7>;rel=\"self\"]")); 
        assertEquals("Author Test7", BookSeven.getAuthor()); 
		assertEquals(7D, BookSeven.getPrice());
		assertEquals("Title Test7", BookSeven.getTitle());
        assertNotNull(BookSeven.getLaunchDate());

	}
    
}
