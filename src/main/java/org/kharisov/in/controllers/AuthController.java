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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        DtoInValidator.validate(userDto, PasswordValidationGroup.class);

        Long roleId = authService.getRoleIdByName(Role.USER);
        UserRecord user = UserMapper.INSTANCE.toEntityWithRole(userDto, roleId);

        user = authService.logIn(user);
        Map<String, String> tokens = jwtUtils.generateTokens(user);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        DtoInValidator.validate(userDto, PasswordValidationGroup.class);

        Long roleId = authService.getRoleIdByName(Role.USER);
        UserRecord user = UserMapper.INSTANCE.toEntityWithRole(userDto, roleId);

        Optional<UserRecord> userRecordOptional = authService.addUser(user);
        if (userRecordOptional.isPresent()) {
            Map<String, String> tokens = jwtUtils.generateTokens(userRecordOptional.get());
            return ResponseEntity.ok(tokens);
        } else {
            throw new ConflictException("Пользователь уже существует");
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto) {
        if (refreshTokenDto == null || refreshTokenDto.getRefreshToken().isEmpty()) {
            throw new InvalidDtoException("Некорректно введен токен");
        }

        String refreshToken = refreshTokenDto.getRefreshToken();
        Long id = jwtUtils.extractUserId(refreshToken);
        Optional<UserRecord> optionalUser = authService.getUserById(id);

        if (optionalUser.isEmpty()) {
            throw new UnauthorizedException("Некорректный токен");
        }

        Map<String, String> tokens = jwtUtils.generateTokens(optionalUser.get());
        return ResponseEntity.ok(tokens);
    }
}
