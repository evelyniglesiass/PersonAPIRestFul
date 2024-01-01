package br.com.api.spring.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.spring.exceptions.ResourceNotFoundException;
import br.com.api.spring.mapper.DozerMapper;
import br.com.api.spring.models.PersonModel;
import br.com.api.spring.repository.PersonRepository;
import br.com.api.spring.vo.v1.PersonVO;

@Service
public class PersonService {

    private Logger logger = Logger.getLogger(PersonService.class.getName()); 

    @Autowired
    private PersonRepository perRep;

    public PersonVO create(PersonVO person) { 
        logger.info("Creating one person!");

        var entity = DozerMapper.parseObject(person, PersonModel.class); 
        var vo = DozerMapper.parseObject(perRep.save(entity), PersonVO.class); 

        return vo;
    }

    public PersonVO update(PersonVO person) { 
        logger.info("Updating one person!");

        var entity = perRep.findById(person.getId()) 
            .orElseThrow(() -> new ResourceNotFoundException("No records fpund for this ID!")); 

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo = DozerMapper.parseObject(perRep.save(entity), PersonVO.class); 
        return vo;
    }

    public void delete(Long id) { 
        logger.info("deleting one person!");

        var entity = perRep.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records fpund for this ID!"));

        perRep.delete(entity); 
    }

    public List<PersonVO> findAll() { 
        logger.info("Finding all people!");

        return DozerMapper.parseListObjects(perRep.findAll(), PersonVO.class); 
    }

    public PersonVO findById(Long id) {
        logger.info("Finding one person!"); 

        var entity = perRep.findById(id) 
            .orElseThrow(() -> new ResourceNotFoundException("No records fpund for this ID!")); 

        return DozerMapper.parseObject(entity, PersonVO.class);
    }

}
