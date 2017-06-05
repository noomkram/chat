package com.ora.assessment.security.spring;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ora.assessment.security.AuthenticatedUser;
import com.ora.assessment.security.spring.UserDetailsService.AuthenticatedUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TokenAuthenticationService {

  private final JwtProperties props;

  @Autowired
  public TokenAuthenticationService(JwtProperties props) {
    this.props = props;
  }

  public void addAuthentication(HttpServletResponse response,
      AuthenticatedUserDetails userDetails) {
    //@formatter:off
    try {
      String jwt = Jwts.builder()
          .setSubject(userDetails.getUsername()) // email
          .claim("name", userDetails.getName())
          .claim("userId", userDetails.getUserId())
          .setExpiration(new Date(System.currentTimeMillis() + props.getExpiration()))
          .signWith(SignatureAlgorithm.HS512, props.getSecret().getBytes("UTF-8"))
          .compact();

      log.debug("adding header [{}] with value [{} {}]", props.getHeader(), props.getTokenPrefix(), jwt);

      response.addHeader(props.getHeader(), props.getTokenPrefix() + " " + jwt);
    } catch (Exception ex) {
      log.warn("exception building jwt", ex);
    }
    //@formatter:on
  }

  public AuthenticatedUser getAuthentication(HttpServletRequest request) {
    String jwt = request.getHeader(props.getHeader());
    if (null == jwt) {
      log.debug("jwt not found");
      return null;
    }

    jwt = jwt.replace(props.getTokenPrefix(), "").trim();

    try {
      Jws<Claims> claims =
          Jwts.parser().setSigningKey(props.getSecret().getBytes("UTF-8")).parseClaimsJws(jwt);

      String email = claims.getBody().getSubject();
      if (null == email) {
        log.warn("subject (email) not found in jwt [{}]", jwt);
        return null;
      }

      Long userId = Long.valueOf(claims.getBody().get("userId").toString());
      String name = claims.getBody().get("name").toString();

      return new AuthenticatedUser(userId, email, name);
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
        | SignatureException | IllegalArgumentException | UnsupportedEncodingException ex) {
      log.warn("exception [{}] parsing jwt", ex.getClass(), ex);
    }

    return null;
  }

  @Component
  @ConfigurationProperties("jwt")
  @Setter
  @Getter
  public static class JwtProperties {

    private String header;
    private String secret;
    private String tokenPrefix;
    private Long expiration;

  }

}
