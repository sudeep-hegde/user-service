package com.shopperzaar.User_Service.service;

import com.shopperzaar.User_Service.domain.*;
import com.shopperzaar.User_Service.dto.RegisterDto;
import com.shopperzaar.User_Service.dto.ResponseDto;
import com.shopperzaar.User_Service.dto.UserDto;
import com.shopperzaar.User_Service.exception.AppException;
import com.shopperzaar.User_Service.mappers.UserMapper;
import com.shopperzaar.User_Service.repository.RolesRepository;
import com.shopperzaar.User_Service.repository.UserRepository;
import com.shopperzaar.User_Service.repository.UserRolesRepository;
import com.shopperzaar.User_Service.util.EncryptDecryptUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final EncryptDecryptUtil encryptDecryptUtil;

    private final RolesRepository rolesRepository;

    private final UserRolesRepository userRolesRepository;

    public UserDto validateUser(String username, String password) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        List<UserRole> roles= new ArrayList<>();
        //decrypt the password and compare
        if ((encryptDecryptUtil.decrypt(user.getPassword()).equals(password))) {
            if(user.getUserRoles() != null) {
                roles = user.getUserRoles().stream().map(userRoles -> userRoles.getRoles().getRole()).toList();
            }
            //also check verified column?
            UserDto userDto = userMapper.toUserDto(user);
            userDto.setRoles(roles);
            return userDto;
        }
        throw new AppException("Invalid Password", HttpStatus.BAD_REQUEST);
    }

    //for normal user registration
    public ResponseDto saveUser(RegisterDto registerDto) {
        userRepository.findByEmail(registerDto.getEmail())
                .ifPresent(u -> { throw new AppException("User with email already exists", HttpStatus.BAD_REQUEST); });
        User user = userMapper.toUser(registerDto);
        user.setPassword(encryptDecryptUtil.encrypt(new String(registerDto.getPassword())));
        user = userRepository.save(user);
        Roles userRole = rolesRepository.findByRole(UserRole.USER)
                .orElseThrow(() -> new AppException("role not found", HttpStatus.NOT_FOUND));
        UserRoles userRoles = new UserRoles();
        UserRoleId userRoleId = new UserRoleId(user.getId(), userRole.getRoleId());
        userRoles.setId(userRoleId);
        userRoles.setUser(user);
        userRoles.setRoles(userRole);
        userRolesRepository.save(userRoles);
       //insert the roles in user_roles table.
        return new ResponseDto(HttpStatus.CREATED,"User Created Successfully");
    }

}
