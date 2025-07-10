package br.com.api.spring.integrationtests.vo.wrappers;

import java.io.Serializable;
import java.util.List;

import br.com.api.spring.integrationtests.vo.PersonVO;

public class PersonEmbeddedVO implements Serializable{

	private static final long serialVersionUID = 1L;
	
    private List<PersonVO> persons;

}
