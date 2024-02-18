package org.kharisov.in.controllers;

import lombok.RequiredArgsConstructor;
import org.kharisov.dtos.*;
import org.kharisov.entities.UserRecord;
import org.kharisov.enums.Role;
import org.kharisov.exceptions.*;
import org.kharisov.mappers.UserMapper;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.utils.JwtUtils;
import org.kharisov.validators.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserDto userDto) {
        DtoInValidator.validate(userDto);

        Long roleId = authService.getRoleIdByName(Role.USER);
        UserRecord user = UserMapper.INSTANCE.toEntityWithRole(userDto, roleId);

        user = authService.logIn(user);
        Map<String, String> tokens = jwtUtils.generateTokens(user);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UserDto userDto) {
        DtoInValidator.validate(userDto);

        Long roleId = authService.getRoleIdByName(Role.USER);
        UserRecord user = UserMapper.INSTANCE.toEntityWithRole(userDto, roleId);

        Optional<UserRecord> userRecordOptional = authService.addUser(user);
        if (userRecordOptional.isPresent()) {
            Map<String, String> tokens = jwtUtils.generateTokens(userRecordOptional.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(tokens);
        } else {
            throw new ConflictException("The user already exists");
        }
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        if (refreshTokenDto == null || refreshTokenDto.getRefreshToken().isEmpty()) {
            throw new InvalidDtoException("The refresh token was entered incorrectly");
        }

        String refreshToken = refreshTokenDto.getRefreshToken();
        Long id = jwtUtils.extractUserId(refreshToken);
        Optional<UserRecord> optionalUser = authService.getUserById(id);

        if (optionalUser.isEmpty()) {
            throw new UnauthorizedException("Invalid token");
        }

        Map<String, String> tokens = jwtUtils.generateTokens(optionalUser.get());
        return ResponseEntity.ok(tokens);
    }
}
