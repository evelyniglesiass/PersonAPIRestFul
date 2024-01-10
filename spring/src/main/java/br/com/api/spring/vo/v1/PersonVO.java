package br.com.api.spring.vo.v1;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Service
@JsonPropertyOrder({"id", "first_name", "last_name", "address", "gender"}) 
public class PersonVO extends RepresentationModel<PersonVO> implements Serializable { 

    @JsonProperty("id") 
    @Mapping("id") 
    private Long key; 

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String address;

    private String gender;

}
