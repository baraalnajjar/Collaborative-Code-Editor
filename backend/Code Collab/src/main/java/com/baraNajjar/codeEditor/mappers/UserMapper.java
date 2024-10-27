package com.baraNajjar.codeEditor.mappers;

import com.baraNajjar.codeEditor.dtos.SignUpDto;
import com.baraNajjar.codeEditor.dtos.UserDto;
import com.baraNajjar.codeEditor.entites.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

}
