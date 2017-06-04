package com.ora.assessment.ext.spring;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@RunWith(MockitoJUnitRunner.class)
public class CustomPageableHandlerMethodArgumentResolverTest {

  private CustomPageableHandlerMethodArgumentResolver resolver;

  @Mock
  private MethodParameter methodParameter;
  @Mock
  private ModelAndViewContainer mavContainer;
  @Mock
  private NativeWebRequest webRequest;
  @Mock
  private WebDataBinderFactory binderFactory;


  @Before
  public void setup() throws NoSuchMethodException, SecurityException {
    resolver = new CustomPageableHandlerMethodArgumentResolver();

    when(methodParameter.getMethod()).thenReturn(PageableMethod.getStubMethod());
  }

  @Test
  public void testResolveArgument() {
    Pageable actual =
        resolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);

    assertThat(actual, instanceOf(PageRequestWrapper.class));
    assertEquals(0, actual.getPageNumber());
  }

  @Test
  public void testSetOneIndexParameters() {
    resolver.setOneIndexedParameters(true);

    Pageable actual =
        resolver.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);

    assertThat(actual, instanceOf(PageRequestWrapper.class));
    assertEquals(1, actual.getPageNumber());
  }

  private static class PageableMethod {

    @SuppressWarnings("unused")
    public void stub(Pageable pageable) {

    }

    static Method getStubMethod() throws NoSuchMethodException, SecurityException {
      return PageableMethod.class.getDeclaredMethod("stub", Pageable.class);
    }

  }

}
