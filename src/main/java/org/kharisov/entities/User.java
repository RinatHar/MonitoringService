package org.kharisov.entities;

import lombok.*;
import org.kharisov.enums.IndicatorEnum;

import java.util.HashMap;

@Builder
@Getter
@Setter
public class User {
    private String accountNum;
    private String email;
    private String password;
    private String phoneNum;
    @Builder.Default
    private HashMap<IndicatorEnum, Integer> indicators = new HashMap<>();
}
