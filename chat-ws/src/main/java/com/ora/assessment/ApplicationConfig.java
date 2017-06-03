package com.ora.assessment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.ora.assessment.auth.spring.AuthenticatedUserHandlerMethodArgumentResolver;

@Configuration
@Import(ApiConfig.class)
@ComponentScan
public class ApplicationConfig {


  @Configuration
  protected static class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    protected AuthenticatedUserHandlerMethodArgumentResolver authenticatedUserHandlerMethodArgumentResolver() {
      return new AuthenticatedUserHandlerMethodArgumentResolver();
    }

    @Bean
    protected PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver() {
      PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
      resolver.setOneIndexedParameters(true);
      resolver.setSizeParameterName("limit");
      return resolver;
    }

    @Autowired
    public void setArgumentResolvers(RequestMappingHandlerAdapter adapter) {
      List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
      resolvers.add(authenticatedUserHandlerMethodArgumentResolver());
      resolvers.add(pageableHandlerMethodArgumentResolver());
      resolvers.addAll(adapter.getArgumentResolvers());
      adapter.setArgumentResolvers(resolvers);
    }

  }

}
