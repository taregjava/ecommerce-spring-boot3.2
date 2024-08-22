package com.halfacode.ecommMaster.config;

import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class JacksonConfig {

   // @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        StreamWriteConstraints constraints = StreamWriteConstraints.builder()
                .maxNestingDepth(2000) // Set your desired depth
                .build();
        mapper.getFactory().setStreamWriteConstraints(constraints);
        return mapper;
    }
}

