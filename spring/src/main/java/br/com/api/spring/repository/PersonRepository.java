package br.com.api.spring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	@Query("SELECT p FROM PersonModel p WHERE p.firstName LIKE LOWER(CONCAT ('%',:firstName,'%'))") // busca no banco de dados firstName que possua o texto passado por parametro
																							   // possuindo ou não textos na frente e depois, e o transformando em minusculo para fazer a pesquisa
	Page<PersonModel> findPersonByName(@Param("firstName") String firstName, Pageable pageable); // parametro para pesquisa / retorna um Page de Person
}
