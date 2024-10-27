package com.baraNajjar.codeEditor.controllers;

import com.baraNajjar.codeEditor.configuration.UserAuthenticationProvider;
import com.baraNajjar.codeEditor.dtos.CredentialsDto;
import com.baraNajjar.codeEditor.dtos.UserDto;
import com.baraNajjar.codeEditor.entites.User;
import com.baraNajjar.codeEditor.repositories.UserRepository;
import com.baraNajjar.codeEditor.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/editor")
public class EditorController {

    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;
    @PostMapping("/login")
    public ResponseEntity<UserDto> editorLogin(@RequestBody @Valid CredentialsDto credentialsDto) {
        UserDto userDto = userService.login(credentialsDto);
        userDto.setToken(userAuthenticationProvider.createToken(userDto));
        return ResponseEntity.ok(userDto);
    }


    @GetMapping("/getId")
    @PreAuthorize("hasAnyAuthority('EDITOR')")
    public ResponseEntity<Long> getEditorId(@RequestParam String editorName) {
        UserDto editor = userService.findByUsername(editorName);

        if (editor != null) {
            return ResponseEntity.ok(editor.getId());
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

}
