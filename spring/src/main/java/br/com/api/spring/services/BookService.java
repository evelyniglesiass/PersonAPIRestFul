package br.com.api.spring.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class BookService {

    private Logger logger = Logger.getLogger(BookService.class.getName()); 

    @Autowired
    private BookRepository booRep;

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

    public List<BookVO> findAll() { 
        logger.info("Finding all books!");

        var books = DozerMapper.parseListObjects(booRep.findAll(), BookVO.class); 
        books
            .stream()
            .forEach(b -> b.add(linkTo(methodOn(BookController.class).findById(b.getKey())).withSelfRel())); 
        return books;
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
