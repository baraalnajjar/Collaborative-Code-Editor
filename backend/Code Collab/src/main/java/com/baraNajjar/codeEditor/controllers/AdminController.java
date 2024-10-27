package com.baraNajjar.codeEditor.controllers;

import com.baraNajjar.codeEditor.configuration.UserAuthenticationProvider;
import com.baraNajjar.codeEditor.entites.User;
import com.baraNajjar.codeEditor.enums.Role;
import com.baraNajjar.codeEditor.services.UserService;
import com.baraNajjar.codeEditor.dtos.CredentialsDto;
import com.baraNajjar.codeEditor.dtos.SignUpDto;
import com.baraNajjar.codeEditor.dtos.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @PostMapping("/login")
    public ResponseEntity<UserDto> adminLogin(@RequestBody @Valid CredentialsDto credentialsDto) {
        UserDto userDto = userService.login(credentialsDto);
        userDto.setToken(userAuthenticationProvider.createToken(userDto));
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/registerEditor")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<UserDto> registerEditor(@RequestBody @Valid SignUpDto user) {
        UserDto createdUser = userService.register(user, Role.EDITOR);
        createdUser.setToken(userAuthenticationProvider.createToken(createdUser));
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }


    @PostMapping("/removeEditor/{editorId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> removeEditor(@PathVariable long editorId) {
        boolean isRemoved = userService.removeEditor(editorId);

        if (isRemoved) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/viewEditor")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<User>> viewEditor() {
        List<User> editors = userService.getEditors();

        if (editors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(editors);
    }


    @PostMapping("/registerViewer")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<UserDto> registerViewer(@RequestBody @Valid SignUpDto user) {
        UserDto createdUser = userService.register(user, Role.VIEWER);
        createdUser.setToken(userAuthenticationProvider.createToken(createdUser));
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }

    @GetMapping("/viewViewer")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<User>> viewViewer() {
        List<User> Viewers = userService.getViewers();

        if (Viewers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(Viewers);
    }

    @PostMapping("/removeViewer/{ViewerId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> removeViewer(@PathVariable long ViewerId) {
        boolean isRemoved = userService.removeViewer(ViewerId);

        if (isRemoved) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
