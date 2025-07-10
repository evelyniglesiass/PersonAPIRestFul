package br.com.api.spring.services;

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

import br.com.api.spring.controllers.PersonController;
import br.com.api.spring.exceptions.RequiredObjectIsNullException;
import br.com.api.spring.exceptions.ResourceNotFoundException;
import br.com.api.spring.mapper.DozerMapper;
import br.com.api.spring.models.PersonModel;
import br.com.api.spring.repository.PersonRepository;
import br.com.api.spring.vo.v1.PersonVO;
import jakarta.transaction.Transactional;

@Service
public class PersonService {

    private Logger logger = Logger.getLogger(PersonService.class.getName()); 

    @Autowired
    private PersonRepository perRep;

    @Autowired
    private PagedResourcesAssembler<PersonVO> assembler; 

    public PersonVO create(PersonVO person) { 

        if (person == null) throw new RequiredObjectIsNullException(); 

        logger.info("Creating one person!");

        var entity = DozerMapper.parseObject(person, PersonModel.class); 
        var vo = DozerMapper.parseObject(perRep.save(entity), PersonVO.class); 
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel()); 
        return vo;
    }

    public PersonVO update(PersonVO person) { 

        if (person == null) throw new RequiredObjectIsNullException(); 

        logger.info("Updating one person!");

        var entity = perRep.findById(person.getKey()) 
            .orElseThrow(() -> new ResourceNotFoundException("No records fpund for this ID!")); 

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo = DozerMapper.parseObject(perRep.save(entity), PersonVO.class); 
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel()); 
        return vo;
    }

    @Transactional 
    public PersonVO disablePerson(Long id) {
        
        logger.info("Disabling one person!");
        
        perRep.disablePerson(id); 
        
        var entity = perRep.findById(id) 
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!")); 
        var vo = DozerMapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo; 
    }

    public void delete(Long id) { 
        logger.info("Deleting one person!");

        var entity = perRep.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        perRep.delete(entity); 
    }

    public PagedModel<EntityModel<PersonVO>> findPersonByName(String firstName, Pageable pageable) { // copia do findAll com um parametro a mais
        logger.info("Finding People by Name!"); 

        var personPage = perRep.findPersonByName(firstName, pageable); // chamando a funçao nova do repository
        
        var personVosPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class)); 
        personVosPage.map(
            p -> p.add( 
                linkTo(methodOn(PersonController.class)
                    .findById(p.getKey())).withSelfRel())); 

        Link link = linkTo(
            methodOn(PersonController.class) 
                .findAll(pageable.getPageNumber(), 
                        pageable.getPageSize(),
                        "asc")).withSelfRel(); 

        return assembler.toModel(personVosPage, link); 
    }

    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) { 
        logger.info("Finding all people!"); 

        var personPage = perRep.findAll(pageable); 
        
        var personVosPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class)); 
        personVosPage.map(
            p -> p.add( 
                linkTo(methodOn(PersonController.class)
                    .findById(p.getKey())).withSelfRel())); 

        Link link = linkTo(
            methodOn(PersonController.class) 
                .findAll(pageable.getPageNumber(), 
                        pageable.getPageSize(),
                        "asc")).withSelfRel(); 

        return assembler.toModel(personVosPage, link); 
    }

    public PersonVO findById(Long id) {
        logger.info("Finding one person!"); 

        var entity = perRep.findById(id) 
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!")); 

        var vo = DozerMapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel()); 
        return vo;
    }

}
