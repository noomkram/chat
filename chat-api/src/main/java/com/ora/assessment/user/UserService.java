package com.ora.assessment.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;

import com.ora.assessment.Creating;
import com.ora.assessment.NotFoundException;
import com.ora.assessment.Updating;
import com.ora.assessment.ValidationException;

@Service
@Transactional(readOnly = true)
public class UserService {

  @Autowired
  private UserRepository userRepo;
  @Autowired
  private PasswordEncoder passwordEncoder;


  public User get(Long id) {
    return userRepo.findOne(id);
  }

  public User getByEmail(String email) {
    return userRepo.findByEmailIgnoreCase(email);
  }

  @Transactional(readOnly = false, rollbackFor = Throwable.class)
  public User save(User user, String confirmPassword) {
    if (user.isNew()) {
      return create(user, confirmPassword);
    }
    return update(user, confirmPassword);
  }

  private User create(@Validated(Creating.class) User user, String confirmPassword) {
    validateConfirmPassword(user, confirmPassword);

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    return userRepo.save(user);
  }

  private User update(@Validated(Updating.class) User user, String confirmPassword) {
    User existingUser = userRepo.findOne(user.getId());
    if (null == existingUser) {
      throw new NotFoundException("user not found");
    }

    validateConfirmPassword(user, confirmPassword);
    validateExistingPassword(existingUser, user.getPassword());

    existingUser.setEmail(user.getEmail());
    existingUser.setName(user.getName());
    existingUser.setPassword(user.getPassword());

    return userRepo.save(existingUser);
  }

  private void validateExistingPassword(User user, String confirmPassword) {
    if (!passwordEncoder.matches(user.getPassword(), confirmPassword)) {
      Errors errors = new BeanPropertyBindingResult(user, "user");
      errors.rejectValue("password", "passwords do not match");
      throw new ValidationException(errors);
    }
  }

  private void validateConfirmPassword(User user, String confirmPassword) {
    if (!user.getPassword().equals(confirmPassword)) {
      Errors errors = new BeanPropertyBindingResult(user, "user");
      errors.rejectValue("password", "passwords do not match");
      throw new ValidationException(errors);
    }
  }

}
