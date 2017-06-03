package com.ora.assessment.auth.spring;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.ora.assessment.auth.AuthenticatedUser;


public class AuthenticatedUserHandlerMethodArgumentResolver
    implements HandlerMethodArgumentResolver {

  public AuthenticatedUserHandlerMethodArgumentResolver() {
    super();
  }

  @Override
  public boolean supportsParameter(final MethodParameter parameter) {
    return AuthenticatedUser.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(final MethodParameter parameter,
      final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest,
      final WebDataBinderFactory binderFactory) throws Exception {
    if (parameter.getParameterType().equals(AuthenticatedUser.class)) { // TODO revisit
      return SecurityContextHolder.getContext().getAuthentication();
    }
    return WebArgumentResolver.UNRESOLVED;
  }

}
