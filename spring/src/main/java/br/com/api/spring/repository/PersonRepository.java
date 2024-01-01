package br.com.api.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.api.spring.models.PersonModel;

// @Repository não é mais necessária
public interface PersonRepository extends JpaRepository<PersonModel, Long> { 

}
