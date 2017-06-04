package com.ora.assessment.user;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ora.assessment.GlobalExceptionHandlers;
import com.ora.assessment.resource.DataResource;
import com.ora.assessment.resource.ErrorResource;
import com.ora.assessment.security.AuthenticatedUser;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/users", consumes = APPLICATION_JSON_VALUE)
@Slf4j
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping
  public ResponseEntity<DataResource<UserResource>> create(@RequestBody SaveUser newUser) {
    return ResponseEntity.status(CREATED).body(new DataResource<>(
        userService.save(asUser(newUser), newUser.getPasswordConfirmation()), UserResource::new));
  }

  @PatchMapping("/current")
  public ResponseEntity<DataResource<UserResource>> update(AuthenticatedUser user,
      @RequestBody SaveUser updatedUser) {
    updatedUser.setId(user.getUserId());

    return ResponseEntity.ok(new DataResource<>(
        userService.save(asUser(updatedUser), updatedUser.getPasswordConfirmation()),
        UserResource::new));
  }

  @GetMapping("/current")
  public ResponseEntity<DataResource<UserResource>> get(AuthenticatedUser user) {
    return ResponseEntity
        .ok(new DataResource<>(userService.get(user.getUserId()), UserResource::new));
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorResource duplicateEmail(DataIntegrityViolationException ex) {
    return new ErrorResource(GlobalExceptionHandlers.VAILATION_FAILED).addError("email", "user with email already exists");
  }

  private User asUser(SaveUser user) {
    User u = new User();
    u.setId(user.getId());
    u.setEmail(user.getEmail());
    u.setName(user.getName());
    u.setPassword(user.getPassword());

    log.debug("converted submitted user [{}] to user [{}]", user, u);

    return u;
  }

}