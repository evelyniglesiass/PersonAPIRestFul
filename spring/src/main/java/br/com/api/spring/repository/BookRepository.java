package br.com.api.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.api.spring.models.BookModel;

public interface BookRepository extends JpaRepository<BookModel, Long> {
    
}
