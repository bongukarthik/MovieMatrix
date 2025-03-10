package com.movieMatrix.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;
import com.movieMatrix.models.Role;

//remove after changing to other class
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {
  private String id;
  @NotBlank(message = "Name is required")
  private String name;
  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  private String email;
  @NotBlank(message = "Password is required")
  private String password;
  @NotBlank(message = "Phone number is required")
  private String phoneNumber;
  private String address;
  private Date dateOfBirth;
  private List<Role> role;
  private String profilePicture;

  


}
