package br.com.api.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.api.spring.models.PersonModel;

@Repository
public interface PersonRepository extends JpaRepository<PersonModel, Long> { 

    @Modifying 
	@Query("UPDATE PersonModel p SET p.enabled = false WHERE p.id =:id") 
	void disablePerson(@Param("id") Long id); 

}
