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
import com.ora.assessment.security.Token;
import com.ora.assessment.security.TokenRepository;
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
  private final TokenRepository tokenRepo;

  @Autowired
  public TokenAuthenticationService(JwtProperties props, TokenRepository tokenRepo) {
    this.props = props;
    this.tokenRepo = tokenRepo;
  }

  public void addAuthentication(HttpServletResponse response,
      AuthenticatedUserDetails userDetails) {
    //@formatter:off
    try {
      final Date expires = new Date(System.currentTimeMillis() + props.getExpiration());

      final String jwt = Jwts.builder()
          .setSubject(userDetails.getUsername()) // email
          .claim("name", userDetails.getName())
          .claim("userId", userDetails.getUserId())
          .setExpiration(expires)
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
    try {
      final String jwt = getToken(request);
      if (null == jwt) {
        return null;
      }

      final Jws<Claims> claims = getClaims(jwt);
      if (null == claims) {
        return null;
      }

      final String email = claims.getBody().getSubject();
      final Long userId = Long.valueOf(claims.getBody().get("userId").toString());
      final String name = claims.getBody().get("name").toString();

      Token token = tokenRepo.findByToken(jwt);
      if (null != token) {
        log.debug("user [{}] logged out with this token [{}]", email, jwt);
        return null;
      }

      return new AuthenticatedUser(userId, email, name);
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
        | SignatureException | IllegalArgumentException ex) {
      log.warn("exception [{}] parsing jwt", ex.getClass(), ex);
    }

    return null;
  }

  public void invalidate(HttpServletRequest request) {
    try {
      final String jwt = getToken(request);
      if (null == jwt) {
        return;
      }

      final Jws<Claims> claims = getClaims(jwt);
      if (null == claims) {
        return;
      }

      final Token token = new Token();
      token.setToken(jwt);
      token.setExpires(claims.getBody().getExpiration());
      tokenRepo.save(token);

    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException
        | SignatureException | IllegalArgumentException ex) {
      log.warn("exception [{}] parsing jwt", ex.getClass(), ex);
    }
  }

  private String getToken(HttpServletRequest request) {
    final String jwt = request.getHeader(props.getHeader());
    if (null == jwt) {
      log.debug("jwt not found");
      return null;
    }

    return jwt.replace(props.getTokenPrefix(), "").trim();
  }

  private Jws<Claims> getClaims(String jwt) {
    try {
      final Jws<Claims> claims =
          Jwts.parser().setSigningKey(props.getSecret().getBytes("UTF-8")).parseClaimsJws(jwt);

      final String email = claims.getBody().getSubject();
      if (null == email) {
        log.warn("subject (email) not found in jwt [{}]", jwt);
        return null;
      }

      return claims;
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
