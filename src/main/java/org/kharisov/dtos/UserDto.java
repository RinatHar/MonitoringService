package org.kharisov.dtos;

import lombok.Getter;
import lombok.Setter;
import org.kharisov.enums.Role;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String accountNum;
    private String password;
    private Long roleId;
    private Role role;
}
