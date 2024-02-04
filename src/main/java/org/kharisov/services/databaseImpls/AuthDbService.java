package org.kharisov.services.databaseImpls;

import org.kharisov.dtos.RoleDto;
import org.kharisov.dtos.UserDto;
import org.kharisov.entities.User;
import org.kharisov.enums.Role;
import org.kharisov.repos.databaseImpls.RoleDbRepo;
import org.kharisov.repos.databaseImpls.UserDbRepo;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.utils.AuthUtils;

import java.util.Objects;
import java.util.Optional;

public class AuthDbService implements AuthService {
    private final UserDbRepo userDbRepo;
    private final RoleDbRepo roleDbRepo;

    public AuthDbService(UserDbRepo userDbRepo, RoleDbRepo roleDbRepo) {
        this.userDbRepo = userDbRepo;
        this.roleDbRepo = roleDbRepo;
    }

    @Override
    public boolean userExists(String accountNum) {
        return userDbRepo.getByAccountNum(accountNum).isPresent();
    }

    @Override
    public Optional<User> getUserByAccountNum(String accountNum) {
        Optional<UserDto> userDtoOptional = userDbRepo.getByAccountNum(accountNum);
        if (userDtoOptional.isPresent()) {
            UserDto userDto = userDtoOptional.get();
            Optional<RoleDto> roleDtoOptional = roleDbRepo.getById(userDto.getRoleId());
            if (roleDtoOptional.isPresent()) {
                RoleDto roleDto = roleDtoOptional.get();
                return Optional.ofNullable(User.builder()
                        .accountNum(userDto.getAccountNum())
                        .password(userDto.getPassword())
                        .role(Role.valueOf(roleDto.getName()))
                        .build());
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> addUser(User user) {
        if (AuthUtils.isValid(user)) {
            Optional<RoleDto> roleDtoOptional = roleDbRepo.getByName(String.valueOf(user.getRole()));
            if (roleDtoOptional.isPresent()) {
                RoleDto roleDto = roleDtoOptional.get();
                UserDto userDto = new UserDto();
                userDto.setAccountNum(user.getAccountNum());
                userDto.setPassword(AuthUtils.hashPassword(user.getPassword()));
                userDto.setRoleId(roleDto.getId());

                Optional<UserDto> userDtoOptional = userDbRepo.add(userDto);
                if (userDtoOptional.isPresent()) {
                    return Optional.of(user);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean logIn(String accountNum, String password) {
        Optional<UserDto> userDtoOptional = userDbRepo.getByAccountNum(accountNum);
        if (userDtoOptional.isPresent()) {
            UserDto userDto = userDtoOptional.get();
            return AuthUtils.checkPassword(password, userDto.getPassword());
        } else {
            return false;
        }
    }

    @Override
    public boolean isAdminByAccountNum(String accountNum) {
        Optional<UserDto> userDtoOptional = userDbRepo.getByAccountNum(accountNum);
        if (userDtoOptional.isPresent()) {
            UserDto userDto = userDtoOptional.get();
            Optional<RoleDto> roleDtoOptional = roleDbRepo.getByName(String.valueOf(Role.ADMIN));
            if (roleDtoOptional.isPresent()) {
                RoleDto roleDto = roleDtoOptional.get();
                return Objects.equals(userDto.getRoleId(), roleDto.getId());
            }
        }
        return false;
    }
}
