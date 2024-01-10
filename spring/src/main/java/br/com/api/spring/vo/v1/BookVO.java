package br.com.api.spring.vo.v1;

import java.io.Serializable;
import java.util.Date;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Service
@JsonPropertyOrder({"id", "title", "author", "price", "launch_date"}) 
public class BookVO extends RepresentationModel<BookVO> implements Serializable {

    @JsonProperty("id") 
    @Mapping("id") 
    private Long key; 

    private String author;

    @JsonProperty("launch_date")
    private Date launchDate;

    private Double price;

    private String title;
    
}
