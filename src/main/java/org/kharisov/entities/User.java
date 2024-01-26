package org.kharisov.entities;

import lombok.*;
import java.util.*;

@Getter
@Setter
@Builder
public class User {
    private String accountNum;
    private String password;
    @Builder.Default
    private List<IndicatorRecord> indicators = new ArrayList<>();
    @Builder.Default
    private boolean isAdmin = false;

    public boolean isAdmin() {
        return isAdmin;
    }
}
