package ue.poznan.spring_jwt_auth.auth.roles;

import org.springframework.security.core.GrantedAuthority;

import java.util.Locale;

public enum Role implements GrantedAuthority {
    ADMIN,
    USER;

    public static final String PREFIX = "ROLE_";

    @Override
    public String getAuthority() {
        return PREFIX + name();
    }

    public static Role valueOf(Object role) {
        switch (role) {
            case null -> throw new IllegalArgumentException("role must not be null");
            case Role r -> {
                return r;
            }
            case GrantedAuthority ga -> {
                return valueOf(ga.getAuthority());
            }
            case CharSequence cs -> {
                String name = cs.toString().trim().toUpperCase(Locale.ROOT);
                if (name.startsWith(PREFIX)) {
                    name = name.substring(PREFIX.length());
                }
                return Role.valueOf(name);
            }
            default -> throw new IllegalArgumentException("role is not a valid role");
        }
    }
}
