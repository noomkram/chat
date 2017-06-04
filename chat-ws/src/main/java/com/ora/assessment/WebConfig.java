package com.ora.assessment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.ora.assessment.ext.spring.CustomPageableHandlerMethodArgumentResolver;
import com.ora.assessment.resource.DataListResource.PageMixIn;
import com.ora.assessment.security.spring.AuthenticatedUserHandlerMethodArgumentResolver;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.addMixIn(Page.class, PageMixIn.class);
    objectMapper.setDateFormat(new ISO8601DateFormat());

    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(objectMapper);

    converters.add(converter);
    super.configureMessageConverters(converters);
  }

  @Bean
  protected AuthenticatedUserHandlerMethodArgumentResolver authenticatedUserHandlerMethodArgumentResolver() {
    return new AuthenticatedUserHandlerMethodArgumentResolver();
  }

  @Bean
  protected PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver() {
    PageableHandlerMethodArgumentResolver resolver =
        new CustomPageableHandlerMethodArgumentResolver();
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
