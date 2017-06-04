package com.ora.assessment.security.spring;

import static org.springframework.http.HttpMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtAuthenticationEntryPoint unauthorizedHandler;
  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private ObjectMapper mapper;
  @Autowired
  private TokenAuthenticationService tokenService;
  @Autowired
  private JwtAuthenticationFilter tokenFilter;

  @Autowired
  public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder)
      throws Exception {
    authenticationManagerBuilder.userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder);
  }

  @Bean
  public JwtLoginFilter jwtLoginFilter() throws Exception {
    return new JwtLoginFilter("/auth/login", authenticationManager(), mapper, tokenService);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers(POST, "/users");
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    //@formatter:off
    httpSecurity
      .csrf().disable()

    .exceptionHandling()
      .authenticationEntryPoint(unauthorizedHandler).and()

    .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    .and()

    .authorizeRequests()
      .antMatchers(POST, "/auth/login").permitAll()
      .anyRequest().authenticated();

    httpSecurity
      .addFilterBefore(jwtLoginFilter(), UsernamePasswordAuthenticationFilter.class)
      .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);

    httpSecurity
      .headers()
        .cacheControl();
    //@formatter:on
  }
}
