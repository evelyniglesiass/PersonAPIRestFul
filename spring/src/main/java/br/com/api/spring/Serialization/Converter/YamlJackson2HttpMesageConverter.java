package br.com.api.spring.Serialization.Converter;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class YamlJackson2HttpMesageConverter extends AbstractJackson2HttpMessageConverter {

    public YamlJackson2HttpMesageConverter() {
        super(
            new YAMLMapper(), MediaType.parseMediaType("application/x-yaml")
            );
    }
    
}
