package com.ora.assessment.security.spring;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenAuthenticationService {

  @Value("${jwt.expiration}")
  private long expiration = 30000;
  @Value("${jwt.secret}")
  private String secret = "ThisIsASecret";
  @Value("${jwt.tokenPrefix}")
  private String tokenPrefix = "Bearer";
  @Value("${jwt.header}")
  private String headerString = "Authorization";

  public void addAuthentication(HttpServletResponse response,
      AuthenticatedUserDetails userDetails) {
    //@formatter:off
    try {
      String jwt = Jwts.builder()
          .setSubject(userDetails.getUsername()) // email
          .claim("name", userDetails.getName())
          .claim("userId", userDetails.getUserId())
          .setExpiration(new Date(System.currentTimeMillis() + expiration))
          .signWith(SignatureAlgorithm.HS512, secret.getBytes("UTF-8")) // TODO consider unique key for each user, would need key below as well
          .compact();

      log.debug("adding header [{}] with value [{} {}]", headerString, tokenPrefix, jwt);

      response.addHeader(headerString, tokenPrefix + " " + jwt);
    } catch (Exception ex) {
      log.warn("exception building jwt", ex);
    }
    //@formatter:on
  }

  public AuthenticatedUser getAuthentication(HttpServletRequest request) {
    String jwt = request.getHeader(headerString);
    if (null == jwt) {
      log.debug("jwt not found");
      return null;
    }

    jwt = jwt.replace(tokenPrefix, "").trim();

    try {
      Jws<Claims> claims =
          Jwts.parser().setSigningKey(secret.getBytes("UTF-8")).parseClaimsJws(jwt);

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
}
