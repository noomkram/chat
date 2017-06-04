package com.ora.assessment.security.spring;

import static com.ora.assessment.TestUtils.EMAIL;
import static com.ora.assessment.TestUtils.NAME;
import static com.ora.assessment.TestUtils.USER_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.ora.assessment.security.AuthenticatedUser;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest(SecurityContextHolder.class)
public class AuthenticatedUserHandlerMethodArgumentResolverTest {

  private AuthenticatedUserHandlerMethodArgumentResolver resolver;
  private AuthenticatedUser user;

  @Mock
  private MethodParameter parameter;
  @Mock
  private ModelAndViewContainer container;
  @Mock
  private NativeWebRequest request;
  @Mock
  private WebDataBinderFactory factory;
  @Mock
  private SecurityContext context;

  @Before
  public void setup() {
    resolver = new AuthenticatedUserHandlerMethodArgumentResolver();
    user = new AuthenticatedUser(USER_ID, EMAIL, NAME);

    mockStatic(SecurityContextHolder.class);
    when(SecurityContextHolder.getContext()).thenReturn(context);
    when(context.getAuthentication()).thenReturn(user);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Test
  public void testSupportsParameterWhenNotSupported() {
    when(parameter.getParameterType()).thenReturn((Class) String.class);

    final boolean actual = resolver.supportsParameter(parameter);
    assertFalse(actual);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Test
  public void testSupportsParameterWhenSupported() {
    when(parameter.getParameterType()).thenReturn((Class) AuthenticatedUser.class);

    final boolean actual = resolver.supportsParameter(parameter);
    assertTrue(actual);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Test
  public void testResolveArgumentWhenNotAuthenticated() throws Exception {
    when(parameter.getParameterType()).thenReturn((Class) String.class);

    try {
      final Object actual = resolver.resolveArgument(parameter, container, request, factory);

      assertEquals(WebArgumentResolver.UNRESOLVED, actual);
    } catch (Exception unexpected) {
      fail("unexpected exception thrown");
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Test
  public void testResolveArgumentWhenAuthenticated() throws Exception {
    when(parameter.getParameterType()).thenReturn((Class) AuthenticatedUser.class);

    final Object actual = resolver.resolveArgument(parameter, container, request, factory);
    assertNotNull(actual);
  }

}
