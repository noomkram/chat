package com.ora.assessment;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ApiConfig.class)
@ComponentScan
public class ApplicationConfig {

}
