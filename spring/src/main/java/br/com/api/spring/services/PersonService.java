package br.com.api.spring.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
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

    public List<PersonVO> findAll() { 
        logger.info("Finding all people!");

        var persons = DozerMapper.parseListObjects(perRep.findAll(), PersonVO.class); 
        persons
            .stream()
            .forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel())); 
        return persons;
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
