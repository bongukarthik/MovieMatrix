package com.movieMatrix.dtos;


import lombok.*;

import java.util.Date;
import java.util.List;
import com.movieMatrix.models.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
  private String id;
  private String name;
  private String email;
  private String password;
  private String phoneNumber;
  private String address;
  private Date dateOfBirth;
  private List<Role> role;
  private String profilePicture;

  


}
