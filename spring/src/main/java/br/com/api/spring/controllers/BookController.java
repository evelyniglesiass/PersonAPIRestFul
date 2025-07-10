package br.com.api.spring.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.spring.services.BookService;
import br.com.api.spring.util.MediaType;
import br.com.api.spring.vo.v1.BookVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/book/v1")
@Tag(name = "Book", description = "Endpoints for Managing books") 
public class BookController {

    @Autowired
    private BookService booSer;
   
    @PostMapping(consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, 
        produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}) 
    @Operation(summary = "Adds a new Book",
		description = "Adds a new Book by passing in a JSON, XML or YML representation of the book!",
		tags = {"Book"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200",
				content = @Content(schema = @Schema(implementation = BookVO.class))
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
		}
	)
    public BookVO create(@RequestBody BookVO book) {
        return booSer.create(book);
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, 
        produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}) 
    @Operation(summary = "Updates a Book",
		description = "Updates a Book by passing in a JSON, XML or YML representation of the book!",
		tags = {"Book"},
		responses = {
			@ApiResponse(description = "Updated", responseCode = "200",
				content = @Content(schema = @Schema(implementation = BookVO.class))
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
		}
	)
    public BookVO update(@RequestBody BookVO book) { 
        return booSer.update(book);
    }
 
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletes a Book",
		description = "Deletes a Book by passing in a JSON, XML or YML representation of the book!",
		tags = {"Book"},
		responses = {
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
		}
	)
    public ResponseEntity<?> delete(@PathVariable Long id) { 
        booSer.delete(id);
        return ResponseEntity.noContent().build();
    }
 
    @GetMapping( produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}) 
    @Operation(summary = "Finds all Books", description = "Finds all Books", 
		tags = {"Book"}, 
		responses = { 
			@ApiResponse(description = "Success", responseCode = "200", 
				content = { 
					@Content( 
						mediaType = "application/json", 
						array = @ArraySchema(schema = @Schema(implementation = BookVO.class)) 
					)
				}),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content), 
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content), 
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content), 
		}
	)
    public ResponseEntity<PagedModel<EntityModel<BookVO>>> findAll( // adicionando o retorno correto para o uso da paginação
		@RequestParam(value = "page", defaultValue = "0") Integer page, // adicionando os novos parâmetros
		@RequestParam(value = "size", defaultValue = "12") Integer size,
		@RequestParam(value = "direction", defaultValue = "asc") String direction
	) { 
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC; // realizando a verificação do direction

		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title")); // criando o pageable de acordo com os parâmetros
        return ResponseEntity.ok(booSer.findAll(pageable));
    }

    @GetMapping(value = "/{id}",
        produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}) 
    @Operation(summary = "Finds a Book", description = "Finds a Book",
		tags = {"Book"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200",
				content = @Content(schema = @Schema(implementation = BookVO.class))
			),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
		}
	)
    public BookVO findById(@PathVariable(value = "id") Long id) {
        return booSer.findById(id);
    }
    
}
