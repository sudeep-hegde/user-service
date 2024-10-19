package com.shopperzaar.User_Service.mappers;

import com.shopperzaar.User_Service.domain.User;
import com.shopperzaar.User_Service.dto.RegisterDto;
import com.shopperzaar.User_Service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * UserMapper
 * mapper impl at compile time
 * org.mapstruct:mapstruct build.gradle dependency
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    UserDto toUserDto(User user);

    //@Mapping(target = "uuid", expression = "java(\"USR-\" + java.util.UUID.randomUUID().toString())") // not required as of now.
    //@Mapping(target = "dateCreated", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "lastUpdated", ignore = true)
    @Mapping(target = "verified", constant = "false")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "userRoles", ignore = true)
    User toUser(RegisterDto registerDto);

}
