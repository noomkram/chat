package com.ora.assessment;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ApiConfig.class})
public class ApplicationConfig {
}
