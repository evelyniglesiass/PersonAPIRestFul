package br.com.api.spring.vo.v1;

import java.io.Serializable;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Service
public class PersonVO implements Serializable { 

    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;

}
