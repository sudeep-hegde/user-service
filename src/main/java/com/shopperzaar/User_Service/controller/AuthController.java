package com.shopperzaar.User_Service.controller;

//import com.shopperzaar.User_Service.domain.UserRole;

import com.shopperzaar.User_Service.dto.CredentialsDto;
import com.shopperzaar.User_Service.dto.RegisterDto;
import com.shopperzaar.User_Service.dto.ResponseDto;
import com.shopperzaar.User_Service.dto.UserDto;
import com.shopperzaar.User_Service.service.JwtService;
import com.shopperzaar.User_Service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v0/auth")
public class AuthController {

    private final JwtService jwtService;

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {
        UserDto userDto = userService.validateUser(credentialsDto.getEmail(), new String(credentialsDto.getPassword()));
        userDto.setToken(jwtService.createToken(userDto));
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> register(@RequestBody RegisterDto registerDto) {
        return ResponseEntity.ok(userService.saveUser(registerDto));
    }

}
