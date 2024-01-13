package br.com.api.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.api.spring.models.UserModel;


@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
	
	@Query("SELECT u FROM UserModel u WHERE u.userName =:userName") 
	UserModel findByUsername(@Param("userName") String userName); 
}
