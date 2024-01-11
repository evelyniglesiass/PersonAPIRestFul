package br.com.api.spring.unittests.mapper.mocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.api.spring.models.BookModel;
import br.com.api.spring.vo.v1.BookVO;

public class MockBook {

    public BookModel mockEntity() { 
    	return mockEntity(0);
    }
    
    public BookVO mockVO() { 
    	return mockVO(0);
    }
    
    public List<BookModel> mockEntityList() { 
        List<BookModel> books = new ArrayList<BookModel>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookVO> mockVOList() { 
        List<BookVO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockVO(i));
        }
        return books;
    }
    
    public BookModel mockEntity(Integer number) {
    	BookModel book = new BookModel();
    	book.setAuthor("Author Test" + number);
        book.setLaunchDate(new Date());
        book.setPrice(number.doubleValue());
        book.setId(number.longValue());
        book.setTitle("Title Test" + number);
        return book;
    }

    public BookVO mockVO(Integer number) {
    	BookVO book = new BookVO();
    	book.setAuthor("Author Test" + number);
        book.setLaunchDate(new Date());
        book.setPrice(number.doubleValue());
        book.setKey(number.longValue());
        book.setTitle("Title Test" + number);
        return book;
    }

    
}

