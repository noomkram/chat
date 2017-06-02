package com.ora.assessment;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotFoundException extends RuntimeException {

  private static final long serialVersionUID = 8413874224426620804L;

  public NotFoundException(String message) {
    super(message);
  }

}
