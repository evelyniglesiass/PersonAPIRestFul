package br.com.api.spring.controllers;

import org.springframework.web.bind.annotation.RestController;

import br.com.api.spring.services.PersonService;
import br.com.api.spring.vo.v1.PersonVO;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/person/v1")
public class PersonController {

    @Autowired
    private PersonService perSer;
   
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,                                                      
        produces = MediaType.APPLICATION_JSON_VALUE)
    public PersonVO create(@RequestBody PersonVO person) {
        return perSer.create(person);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE) 
    public PersonVO update(@RequestBody PersonVO person) { 
        return perSer.update(person);
    }
 
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) { 
        perSer.delete(id);
        return ResponseEntity.noContent().build();
    }
 
    @GetMapping( produces = MediaType.APPLICATION_JSON_VALUE) 
    public List<PersonVO> findAll() { 
        return perSer.findAll();
    }

    @GetMapping(value = "/{id}",
        produces = MediaType.APPLICATION_JSON_VALUE) 
    public PersonVO findById(@PathVariable(value = "id") Long id) {
        return perSer.findById(id);
    }

}
