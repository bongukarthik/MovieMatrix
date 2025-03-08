package com.MovieMatrix.dtos;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
  private String name;
  private String email;
  private String phoneNumber;
  private String address;
  private Date dateOfBirth;
  private String role;
  private String profilePicture;
  private boolean status;
}

