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
@RequestMapping("/viewer")
public class ViewerController {

    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @PostMapping("/login")
    public ResponseEntity<UserDto> viewerLogin(@RequestBody @Valid CredentialsDto credentialsDto) {
        UserDto userDto = userService.login(credentialsDto);
        userDto.setToken(userAuthenticationProvider.createToken(userDto));
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/getId")
    @PreAuthorize("hasAnyAuthority('VIEWER')")
    public ResponseEntity<Long> getEditorId(@RequestParam String viewerName) {
        UserDto editor = userService.findByUsername(viewerName);

        if (editor != null) {
            return ResponseEntity.ok(editor.getId());
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }
}
