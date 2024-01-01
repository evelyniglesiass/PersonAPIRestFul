package br.com.api.spring.vo.v2;

import java.io.Serializable;
import java.util.Date;

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
public class PersonVOv2 implements Serializable { 

    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;

    private Date birthDay; 

}
