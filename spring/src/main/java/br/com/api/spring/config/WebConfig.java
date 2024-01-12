package br.com.api.spring.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.api.spring.Serialization.Converter.YamlJackson2HttpMesageConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final MediaType MEDIA_TYPE_YML = MediaType.valueOf("application/x-yaml"); 
    
    @Value("${cors.originPatterns:default}") // vai ler o application.yml e aplicar nesse obj
    private String corsOriginPatterns = ""; // vai ser o default caso não tenha no application.yml
    
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new YamlJackson2HttpMesageConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        var allowedOrigins = corsOriginPatterns.split(",");
        registry.addMapping("/**") // para todas as rotas da api
			//.allowedMethods("GET", "POST", "PUT") // para permitir para verbos especificos
			.allowedMethods("*") // para permitir para todos metodos/verbos
			.allowedOrigins(allowedOrigins) // passar o array que foi splitado separando as urls a partir de ,
		.allowCredentials(true); // possibilitar autenticação
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorParameter(false)
            .ignoreAcceptHeader(false)
            .useRegisteredExtensionsOnly(false)
            .defaultContentType(MediaType.APPLICATION_JSON) 
                .mediaType("json", MediaType.APPLICATION_JSON) 
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("x-yaml", MEDIA_TYPE_YML);
    }

}
