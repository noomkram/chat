package com.ora.assessment.user;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ora.assessment.auth.AuthenticatedUser;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/users", consumes = APPLICATION_JSON_VALUE)
@Slf4j
public class UserContoller {

  @Autowired
  private UserService userService;

  @PostMapping
  public ResponseEntity<User> create(@RequestBody SaveUser newUser) {
    return ResponseEntity.ok(userService.save(asUser(newUser), newUser.getConfirmPassword()));
  }

  @PatchMapping("/current")
  public ResponseEntity<User> update(AuthenticatedUser user, @RequestBody SaveUser updatedUser) {
    updatedUser.setId(user.getUserId());

    return ResponseEntity
        .ok(userService.save(asUser(updatedUser), updatedUser.getConfirmPassword()));
  }

  @GetMapping("/current")
  public ResponseEntity<User> get(AuthenticatedUser user) {
    return ResponseEntity.ok(userService.get(user.getUserId()));
  }

  private User asUser(SaveUser user) {
    User u = new User();
    user.setEmail(user.getEmail());
    user.setName(user.getName());
    user.setPassword(user.getPassword());

    log.debug("converted submitted user [{}] to user [{}]", user, u);

    return u;
  }

}
