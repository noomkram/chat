package com.ora.assessment.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ora.assessment.NotFoundException;

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
  public User save(SaveUser user) {
    if (user.isNew()) {
      return create(user);
    }
    return update(user);
  }

  private User create(SaveUser user) {
    // TODO ModelMapper
    // TODO validation
    User u = new User();
    u.setEmail(user.getEmail());
    u.setName(user.getName());
    u.setPassword(passwordEncoder.encode(user.getPassword()));

    return userRepo.save(u);
  }

  private User update(SaveUser user) {
    // TODO validation
    // TODO ModelMapper
    User existingUser = userRepo.findOne(user.getId());
    if (null == existingUser) {
      throw new NotFoundException("user not found");
    }

    existingUser.setEmail(user.getEmail());
    existingUser.setName(user.getName());
    existingUser.setPassword(user.getPassword());

    return userRepo.save(existingUser);
  }

}
