package org.kharisov.in.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kharisov.auditshared.entities.UserRecord;
import org.kharisov.dtos.*;
import org.kharisov.enums.Role;
import org.kharisov.exceptions.*;
import org.kharisov.mappers.UserMapper;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.utils.JwtUtils;
import org.kharisov.validators.groups.RoleValidationGroup;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Контроллер для работы с аутентификацией.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    /**
     * Вход в систему.
     *
     * @param userDto DTO пользователя.
     * @return Токены доступа и обновления.
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody UserDto userDto) {
        UserRecord user = UserMapper.INSTANCE.toEntity(userDto);

        user = authService.logIn(user);
        Map<String, String> tokens = jwtUtils.generateTokens(user);

        return ResponseEntity.ok(tokens);
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param userDto DTO нового пользователя.
     * @return Токены доступа и обновления.
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody UserDto userDto) {
        UserRecord user = UserMapper.INSTANCE.toEntityWithRole(userDto, Role.USER);

        UserRecord userRecord = authService.addUser(user);
        Map<String, String> tokens = jwtUtils.generateTokens(userRecord);

        return ResponseEntity.status(HttpStatus.CREATED).body(tokens);
    }

    /**
     * Обновление токена доступа.
     *
     * @param refreshTokenDto DTO с токеном обновления.
     * @return Новые токены доступа и обновления.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, String>> refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {

        String refreshToken = refreshTokenDto.getRefreshToken();
        Long id = jwtUtils.extractUserId(refreshToken);

        try {
            UserRecord user = authService.getUserById(id);
            Map<String, String> tokens = jwtUtils.generateTokens(user);
            return ResponseEntity.ok(tokens);
        } catch (EntityNotFoundException | MyDatabaseException e) {
            throw new UnauthorizedException("Invalid token");
        }
    }

    /**
     * Изменить роль пользователя.
     *
     * @param user DTO пользователя.
     * @return Статус операции.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{accountNum}")
    public ResponseEntity<String> updateUserRole(@PathVariable("accountNum") String accountNum,
                                                 @Validated({RoleValidationGroup.class}) @RequestBody UserDto user) {
        Role role = Role.valueOf(UserMapper.INSTANCE.toEntity(user).role());
        UserRecord userToUpdate = authService.getUserByAccountNum(accountNum);
        authService.changeUserRole(userToUpdate, role);
        return ResponseEntity.ok("The user's role has been successfully updated");
    }
}
