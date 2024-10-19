package com.shopperzaar.User_Service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shopperzaar.User_Service.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String token;
    private String userId;
    private List<UserRole> roles;
}
