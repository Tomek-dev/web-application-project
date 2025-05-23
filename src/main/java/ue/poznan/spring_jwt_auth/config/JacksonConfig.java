package ue.poznan.spring_jwt_auth.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
