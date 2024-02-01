package com.g4l.timesheet_backend.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigs {
    
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
