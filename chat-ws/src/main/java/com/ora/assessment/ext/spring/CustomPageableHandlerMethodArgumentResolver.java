package com.ora.assessment.ext.spring;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CustomPageableHandlerMethodArgumentResolver
    extends PageableHandlerMethodArgumentResolver {

  private boolean indexOffset;

  @Override
  public Pageable resolveArgument(MethodParameter methodParameter,
      ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {

    return new PageRequestWrapper(
        super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory),
        indexOffset);
  }

  @Override
  public void setOneIndexedParameters(boolean oneIndexedParameters) {
    super.setOneIndexedParameters(oneIndexedParameters);
    this.indexOffset = oneIndexedParameters;
  }

}
