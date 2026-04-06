package com.parcezza.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class JpaConfig {
   @Bean
   public AuditorAware<String> auditorProvider(){
     return () -> java.util.Optional.ofNullable("system");
   } 
    
}
