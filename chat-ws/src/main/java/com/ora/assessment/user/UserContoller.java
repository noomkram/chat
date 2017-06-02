package com.ora.assessment.user;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ora.assessment.auth.AuthenticatedUser;

@RestController
@RequestMapping(value = "/users", consumes = APPLICATION_JSON_VALUE)
public class UserContoller {

  @Autowired
  private UserService userService;

  @PostMapping
  public ResponseEntity<User> create(@RequestBody SaveUser user) {
    return ResponseEntity.ok(userService.save(user));
  }

  @PatchMapping("/current")
  public ResponseEntity<User> update(@RequestBody SaveUser user) {
    user.setId(getLoggedInUser().getUserId());

    return ResponseEntity.ok(userService.save(user));
  }

  @GetMapping("/current")
  public ResponseEntity<User> get() {
    return ResponseEntity.ok(userService.get(getLoggedInUser().getUserId()));
  }

  // TODO replace with HandlerMethodArgumentResolver
  private AuthenticatedUser getLoggedInUser() {
    return (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication();
  }

}
