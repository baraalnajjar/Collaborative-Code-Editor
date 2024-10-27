package com.baraNajjar.codeEditor.mappers;

import com.baraNajjar.codeEditor.dtos.SignUpDto;
import com.baraNajjar.codeEditor.dtos.UserDto;
import com.baraNajjar.codeEditor.entites.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-26T01:16:41+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        userDto.id( user.getId() );
        userDto.firstName( user.getFirstName() );
        userDto.lastName( user.getLastName() );
        userDto.login( user.getLogin() );
        if ( user.getRole() != null ) {
            userDto.role( user.getRole().name() );
        }

        return userDto.build();
    }

    @Override
    public User signUpToUser(SignUpDto signUpDto) {
        if ( signUpDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.firstName( signUpDto.getFirstName() );
        user.lastName( signUpDto.getLastName() );
        user.login( signUpDto.getLogin() );

        return user.build();
    }
}
