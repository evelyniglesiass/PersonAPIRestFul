package br.com.api.spring.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.api.spring.controllers.BookController;
import br.com.api.spring.exceptions.RequiredObjectIsNullException;
import br.com.api.spring.exceptions.ResourceNotFoundException;
import br.com.api.spring.mapper.DozerMapper;
import br.com.api.spring.models.BookModel;
import br.com.api.spring.repository.BookRepository;
import br.com.api.spring.vo.v1.BookVO;
import br.com.api.spring.vo.v1.PersonVO;

@Service
public class BookService {

    private Logger logger = Logger.getLogger(BookService.class.getName()); 

    @Autowired
    private BookRepository booRep;

    @Autowired
    private PagedResourcesAssembler<BookVO> assembler; // inteligente o suficiente para adicionar os links HATEOAS na aplicação


    public BookVO create(BookVO book) { 

        if (book == null) throw new RequiredObjectIsNullException(); 

        logger.info("Creating one book!");

        var entity = DozerMapper.parseObject(book, BookModel.class); 
        var vo = DozerMapper.parseObject(booRep.save(entity), BookVO.class); 
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel()); 
        return vo;
    }

    public BookVO update(BookVO book) { 

        if (book == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one book!");

        var entity = booRep.findById(book.getKey()) 
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!")); 

        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());

        var vo = DozerMapper.parseObject(booRep.save(entity), BookVO.class); 
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel()); 
        return vo;
    }

    public void delete(Long id) { 
        logger.info("Deleting one book!");

        var entity = booRep.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        booRep.delete(entity); 
    }

    public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) { // alterar o tipo de retorno de dado que suporta a paginação e adicionar o novo parâmetro
        logger.info("Finding all books!");

        var bookPage = booRep.findAll(pageable); // pesquisar no banco de acordo com a paginação requisitada

        var bookVosPage = bookPage.map(b -> DozerMapper.parseObject(b, BookVO.class)); 
        bookVosPage.map( // criando um link HATEOAS para cada book retornado
            b -> b.add(
                linkTo(methodOn(BookController.class) 
                    .findById(b.getKey())).withSelfRel()));
        
        Link link = linkTo( // cria o link HATEOAS incluindo os parâmetros dee paginação
            methodOn(BookController.class)
                .findAll(pageable.getPageNumber(),
                        pageable.getPageSize(),
                        "asc")).withSelfRel();
        
        return assembler.toModel(bookVosPage, link); // retorna os dados, juntamente om os links HATEOAS
    }

    public BookVO findById(Long id) {
        logger.info("Finding one book!"); 

        var entity = booRep.findById(id) 
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!")); 

        var vo = DozerMapper.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel()); 
        return vo;
    }
    
}
