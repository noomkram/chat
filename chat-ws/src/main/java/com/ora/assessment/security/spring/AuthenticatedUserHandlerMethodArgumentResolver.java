package com.ora.assessment.security.spring;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.ora.assessment.security.AuthenticatedUser;


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
    if (AuthenticatedUser.class.isAssignableFrom(parameter.getParameterType())) {
      return SecurityContextHolder.getContext().getAuthentication();
    }
    return WebArgumentResolver.UNRESOLVED;
  }

}
