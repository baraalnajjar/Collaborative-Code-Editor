package com.baraNajjar.codeEditor.services;

import com.baraNajjar.codeEditor.entites.User;
import com.baraNajjar.codeEditor.enums.Role;
import com.baraNajjar.codeEditor.exceptions.AppException;
import com.baraNajjar.codeEditor.mappers.UserMapper;
import com.baraNajjar.codeEditor.dtos.CredentialsDto;
import com.baraNajjar.codeEditor.dtos.SignUpDto;
import com.baraNajjar.codeEditor.dtos.UserDto;
import com.baraNajjar.codeEditor.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.events.Event;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByLogin(credentialsDto.getLogin())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto userDto, Role role) {
        Optional<User> optionalUser = userRepository.findByLogin(userDto.getLogin());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(userDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.getPassword())));
        user.setRole(role);
        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }
    public UserDto findByUsername(String firstname) {
        User user = userRepository.findByLogin(firstname)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }

    public boolean removeEditor(long editorId) {
        if (userRepository.existsById(editorId)) {
            userRepository.deleteById(editorId);
            return true;
        }
        return false;
    }


    public List<User> getEditors() {
        return userRepository.findAllByRole(Role.valueOf("EDITOR"));
    }


    public List<User> getViewers() {
        return userRepository.findAllByRole(Role.valueOf("VIEWER"));
    }

    public boolean removeViewer(long editorId) {
        if (userRepository.existsById(editorId)) {
            userRepository.deleteById(editorId);
            return true;
        }
        return false;
    }
}
